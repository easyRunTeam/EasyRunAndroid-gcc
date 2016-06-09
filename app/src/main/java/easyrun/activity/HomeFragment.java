package easyrun.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.special.ResideMenu.ResideMenu;
import easyrun.util.R;

public class HomeFragment extends Fragment {
    private WebView webView;
    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        getActivity().getActionBar().hide();
        setUpViews();
        openURL();
        return parentView;
    }

    private void setUpViews() {
        MainActivity parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

    }

    private void openURL(){
        webView = (WebView) parentView.findViewById(R.id.webView);
        //WebView加载web资源
        webView.loadUrl("http://www.baidu.com");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
}
