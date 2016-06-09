package easyrun.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import easyrun.bean.EventBean;
import easyrun.bean.UserBean;
import easyrun.server.SendDateToServer;
import easyrun.util.R;
import easyrun.util.ServerData;
import easyrun.server.SendDataToServerByOKHttp;
import easyrun.util.Tools;

/**
 * Created by j_crocodile on 2016/6/6.
 */
public class UserUploadPicFragment extends Fragment {
    private View mMainView;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private UserBean user;
    private ImageView pic;
    private String [] events;
    private Spinner bEvent;
    private BootstrapButton submit;
    private String account;
    private final OkHttpClient client = new OkHttpClient();
    private String  event;
    private String PicPath;
    private static final String IMAGE_FILE_NAME = "eImage.jpg";
    private String[] items = new String[] { "选择本地图片", "拍照"};
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private  BootstrapDropDown bbEvent;



    HandlerThread handlerThread = new HandlerThread("handlerThread");
    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SendDateToServer.SEND_SUCCESS:
                    Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                    Bundle bundle=msg.getData();
                    String [] events=bundle.getStringArray("events");
                    System.out.println(events[0]);
                    ArrayAdapter<String> adapterEvents=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,events);
                    adapterEvents.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bEvent.setAdapter(adapterEvents);
                    break;
                case SendDateToServer.SEND_FAIL:
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                    break;
                case SendDateToServer.SEND_NULL:
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }

        }
    };



        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("用户上传界面");
        getActivity().getActionBar().show();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决软键盘遮挡
        mMainView = inflater.inflate(R.layout.user_upload_pic, container, false);
            handlerThread.start();
            Bundle bundle = getArguments();
            user = bundle.getParcelable("userInfo");
            String account = user.getAccount();//账号
            init();

            bEvent=(Spinner)mMainView.findViewById(R.id.bEvent);
        submit=(BootstrapButton)mMainView.findViewById(R.id.submit);
        ArrayAdapter<CharSequence> adapterEvents=ArrayAdapter.createFromResource(getActivity(),R.array.bootstrap_dropdown_example_data,android.R.layout.simple_spinner_item);
        adapterEvents.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bEvent.setAdapter(adapterEvents);
        bEvent.setOnItemSelectedListener(spnListener);
            submit.setOnClickListener(mylistener);
        pic=(ImageView)mMainView.findViewById(R.id.pic);
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });

        return mMainView;
    }

    private Button.OnClickListener mylistener=new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.submit:
                    Bundle bundle = new Bundle();
                    bundle.putString("event", event);
                    bundle.putString("account", user.getAccount());
                    bundle.putString("path",PicPath);
                    // bundle.putString("pic",);
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            File file = new File(PicPath);
                            String path=ServerData.BaseURL+"UploadforUser2";
                            //OKHttp
                            RequestBody requestBody = new MultipartBuilder()
                                    .type(MultipartBuilder.FORM)
                                    .addFormDataPart("account", user.getAccount())
                                    .addFormDataPart("event", event)
                                    .addFormDataPart("image", file.getName(),
                                            RequestBody.create(MEDIA_TYPE_PNG, file))
                                    .build();

                            Request request = new Request.Builder()
                                    .url(path)
                                    .post(requestBody)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Response response) throws IOException {
                                    if (!response.isSuccessful()) {
                                        throw new IOException("Unexpected code " + response);
                                    } else if (response.body().toString() == "failed") {
                                        handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_FAIL);
                                    } else {
                                        System.out.println("succcess");

                                    }
                                }
                            });
                            //OKHttp
                        }
                    });
                    thread.start();
                    break;
            }
        }
    };

    private Spinner.OnItemSelectedListener spnListener=new Spinner.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            event=parent.getSelectedItem().toString();
            System.out.println(event);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            event=parent.getSelectedItem().toString();
        }
        };
    protected void init(){
        //从服务器获取马拉松比赛信息
        Thread thread = new Thread(new Runnable() {
            public void run() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("account", account);
              String path=new SendDataToServerByOKHttp(handler).generatePath(map,ServerData.BaseURL+"UploadforUser1");
                System.out.println(path);
                Request request = new Request.Builder()
                        .url(path)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else if (response.body().toString() == "failed") {
                            handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_FAIL);
                        } else {
                            try {

                                //String jsonResult = URLDecoder.decode();
                                byte[] b = response.body().bytes(); //获取数据的bytes
                                String jsonResult = new String(b, "GB2312"); //然后将其转为gb2312
                                Gson gson = new Gson();
                                ArrayList<EventBean> list;
                                list = gson.fromJson(jsonResult, new TypeToken< ArrayList<EventBean>>() {
                                }.getType());
                                int i=0;
                              events=new String[list.size()];
                                for(;i<list.size();i++) {
                                    events[i]=list.get(i).getEventName();
                                  //  System.out.println(event);
                                }
                            } catch (Exception e) {
                                System.out.println("----------<ERROR>--------");
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what=SendDataToServerByOKHttp.SEND_SUCCESS;
                            Bundle bundle=new Bundle();
                            bundle.putStringArray("events",events);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                });
            }
        });
       thread.start();
         try {
            thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 显示选择对话框
     */
    private void showDialog() {

        new AlertDialog.Builder(getActivity())
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE);
                                intentFromGallery.setType("image/*"); 	//设置文件类型
                                intentFromGallery
                                        .setAction(Intent.ACTION_GET_CONTENT);

                                    startActivityForResult(intentFromGallery,IMAGE_REQUEST_CODE);

                                break;
                            case 1:

                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                if (Tools.hasSdcard()) {
                                    // 判断存储卡是否可以用，可用进行存储
                                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                                    IMAGE_FILE_NAME)));
                                }
                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (resultCode != getActivity().RESULT_CANCELED) {

           switch (requestCode) {
               case IMAGE_REQUEST_CODE:
                   try {
                       Uri uri = data.getData();
                       Log.e("uri", uri.toString());
                       PicPath=Tools.getPath(getActivity(),uri);
                       ContentResolver cr = getActivity().getContentResolver();
                       Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                /* 将Bitmap设定到ImageView */
                           pic.setImageBitmap(bitmap);
                       } catch (FileNotFoundException e) {
                           Log.e("Exception", e.getMessage(),e);
                       }
                   break;
               case CAMERA_REQUEST_CODE:
                   PicPath=Environment.getExternalStorageDirectory()+"/"+IMAGE_FILE_NAME;
                   Bitmap camorabitmap = BitmapFactory.decodeFile(PicPath);
                   pic.setImageBitmap(camorabitmap);
                   break;
           }
       }
       super.onActivityResult(requestCode, resultCode, data);
   }
}
