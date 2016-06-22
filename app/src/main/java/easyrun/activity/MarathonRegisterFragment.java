package easyrun.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.special.ResideMenu.ResideMenu;


import easyrun.bean.UserBean;
import easyrun.util.R;

public class MarathonRegisterFragment extends Fragment {

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private WebView webView;
    private String url;
    private int FILECHOOSER_RESULTCODE = 1;
    private View parentView;
    private ResideMenu resideMenu;
    private UserBean user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.marathon_register2, container, false);
        getActivity().getActionBar().hide();
        setUpViews();
        //获取传递来的用户信息
        webView=(WebView)parentView.findViewById(R.id.webView);
        Bundle bundle = getArguments();
        user = bundle.getParcelable("userInfo");
        String account=user.getAccount();
        url="http://120.27.106.188:8088/EasyRunServer/marathonRegister.jsp?account="+account;
        openURL();
        return parentView;
    }

    private void setUpViews() {
        MainActivity parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

    }

    private void openURL(){

        setWebView();
        //webView.loadUrl("http://www.baidu.com");
    }
    private void setWebView() {
        WebSettings ws = webView.getSettings();
        ws.setBuiltInZoomControls(true);


        ws.setDatabaseEnabled(true);
        String dir = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //设置数据库路径
        ws.setDatabasePath(dir);


        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setAllowFileAccess(true);//允许访问文件数据
        ws.setSaveFormData(true);
        ws.setJavaScriptEnabled(true);


        ws.setDefaultTextEncodingName("UTF-8");
        ws.setSupportZoom(true);
        ws.supportMultipleWindows();


       /* //启用地理定位
        ws.setGeolocationEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);
        //webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }


            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
*/

        webView.setWebChromeClient(new WebChromeClient() {
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                //  Log.e(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                //如果不需要其他对点击链接事件的处理返回true，否则返回false

                return true;

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                // Log.e(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                //    Log.e(TAG, "4.1 openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }
            // For Android 5.0+


            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                // Log.e(TAG, "5.0 onShowFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                mUploadCallbackAboveL = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
                return true;
            }
        });
        //  String url="http://112.74.65.20/ylsy/ylsy.html";


        webView.loadUrl(url);
        //webView.loadUrl();
    }


    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
//有的5.0的机型只显示一次选择图片的对话框  解决办法是加上这句
        if(mUploadCallbackAboveL!=null){
            mUploadCallbackAboveL.onReceiveValue(null);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        webView = null;
    }


    //@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * 这里监控的是物理返回或者设置了该接口的点击事件
         * 当按钮事件为返回时，且WebView可以返回，即触发返回事件
         */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if( webView.canGoBack())
                webView.goBack(); // goBack()表示返回WebView的上一页面
            else
                getActivity().finish();
            return true; // 返回true拦截事件的传递
        }
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            }
            else  if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }


        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {


            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();


                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }


                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }

}
