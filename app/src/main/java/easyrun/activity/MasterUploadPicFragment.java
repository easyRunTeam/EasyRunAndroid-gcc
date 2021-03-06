package easyrun.activity;

import android.app.AlertDialog;

import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.special.ResideMenu.ResideMenu;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import easyrun.bean.ConfirmData;
import easyrun.bean.EventBean;
import easyrun.bean.FreePicBean;
import easyrun.bean.UserBean;
import easyrun.server.SendDataToServerByOKHttp;
import easyrun.server.SendDateToServer;
import easyrun.util.R;
import easyrun.util.ServerData;
import easyrun.util.Tools;

public class MasterUploadPicFragment extends Fragment {

    private View mMainView;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private UserBean user;
    private ImageView pic;
    private String [] events;
    private Spinner bEvent;
    private Button submit;
    private String account;
    private final OkHttpClient client = new OkHttpClient();
    private String  event;
    private String PicPath;
    private static final String IMAGE_FILE_NAME = "eImage.jpg";
    private String[] items = new String[] { "选择本地图片", "拍照"};
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private Button choosePicture;
    private Button deletePicture;
    private EditText Eprice;
    private String price;
    private ResideMenu resideMenu;



    HandlerThread handlerThread = new HandlerThread("handlerThread");
    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            switch (msg.what) {
                case SendDateToServer.SEND_SUCCESS:
                    Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();

                    String [] events=bundle.getStringArray("events");
                    ArrayAdapter<String> adapterEvents=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,events);
                    adapterEvents.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bEvent.setAdapter(adapterEvents);
                    break;
                case SendDateToServer.SEND_FAIL:
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                    break;
                case SendDateToServer.SEND_NULL:
                    Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                case SendDataToServerByOKHttp.SEND_PIC_SECCESS:
                    MasterUploadSuccessFragment masterUploadSuccessFragment=new MasterUploadSuccessFragment();
                    masterUploadSuccessFragment.setArguments(bundle);
                    changeFragment(masterUploadSuccessFragment);
                default:
                    break;
            }

        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("用户上传界面");
        getActivity().getActionBar().hide();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决软键盘遮挡
        mMainView = inflater.inflate(R.layout.master_upload_pic, container, false);
        MainActivity parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        handlerThread.start();
        Bundle bundle = getArguments();
        user = bundle.getParcelable("userInfo");
        System.out.println(user.getAccount());
        account = user.getAccount();//账号
        init();
        bEvent=(Spinner)mMainView.findViewById(R.id.bEvent);
        submit=(Button)mMainView.findViewById(R.id.submit);
        Eprice=(EditText)mMainView.findViewById(R.id.price);

        ArrayAdapter<CharSequence> adapterEvents=ArrayAdapter.createFromResource(getActivity(),R.array.bootstrap_dropdown_example_data,android.R.layout.simple_spinner_dropdown_item);
        adapterEvents.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bEvent.setAdapter(adapterEvents);
        bEvent.setOnItemSelectedListener(spnListener);
        submit.setOnClickListener(mylistener);
        pic=(ImageView)mMainView.findViewById(R.id.pic);
        choosePicture=(Button)mMainView.findViewById(R.id.choosePicture);
        deletePicture=(Button)mMainView.findViewById(R.id.deletePicture);
        deletePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic.setImageResource(R.drawable.frame11);
                PicPath=null;
                deletePicture.setVisibility(View.INVISIBLE);
            }
        });
        choosePicture.setOnClickListener(new View.OnClickListener() {
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
                    if (!ServerData.checkNetwork(getActivity())) {// 检测网络
                        Toast toast = Toast.makeText(getActivity(),"网络未连接", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), NonetActivity.class);
                        startActivity(intent);
                        break;
                    }

                    // bundle.putString("pic",);
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            File file = new File(PicPath);
                            String path=ServerData.BaseURL+"UploadforMaster";
                            //OKHttp
                            price=Eprice.getText().toString();

                            RequestBody requestBody = new MultipartBuilder()
                                    .type(MultipartBuilder.FORM)
                                    .addFormDataPart("account", user.getAccount())
                                    .addFormDataPart("event", event)
                                    .addFormDataPart("price",price)
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
                                    } else {
                                        byte[] b = response.body().bytes(); //获取数据的bytes
                                        String result = new String(b, "GB2312"); //然后将其转为gb2312
                                        if (result.equals("failed")) {
                                            handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_FAIL);
                                        } else {
                                            Gson gson = new Gson();
                                            ArrayList<ConfirmData>  data = gson.fromJson(result,
                                                    new TypeToken< ArrayList<ConfirmData>>() {
                                                    }.getType());
                                            System.out.println("result:"+result);
                                            if(data.get(0).getIdentityPic().equals(null))
                                                System.out.println("证件照为空");
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name", data.get(0).getName());
                                            bundle.putInt("aID", data.get(0).getAthleteID());
                                            bundle.putString("path",data.get(0).getIdentityPic());
                                            Message msg = new Message();
                                            msg.what = SendDataToServerByOKHttp.SEND_PIC_SECCESS;
                                            msg.setData(bundle);
                                            handler.sendMessage(msg);

                                        }
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
                String path=ServerData.BaseURL + "UploadforUser1";
                Request request = new Request.Builder()
                        .url(path).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Toast toast = Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            byte[] b = response.body().bytes(); //获取数据的bytes
                            String Result = new String(b, "GB2312"); //然后将其转为gb2312
                            if (Result.equals("failed")) {
                                handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_FAIL);
                            } else {
                                try {
                                    Gson gson = new Gson();
                                    ArrayList<EventBean> list;
                                    list = gson.fromJson(Result, new TypeToken<ArrayList<EventBean>>() {
                                    }.getType());
                                    int i = 0;
                                    events = new String[list.size()];
                                    for (; i < list.size(); i++) {
                                        events[i] = list.get(i).getEventName();
                                        //  System.out.println(event);
                                    }
                                } catch (Exception e) {
                                    System.out.println("----------<ERROR>--------");
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.what = SendDataToServerByOKHttp.SEND_SUCCESS;
                                Bundle bundle = new Bundle();
                                bundle.putStringArray("events", events);

                                // bundle.putParcelableArrayList("events", (ArrayList<? extends Parcelable>) eevents);//传递对象数组
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
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
                        deletePicture.setVisibility(View.VISIBLE);
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
    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
