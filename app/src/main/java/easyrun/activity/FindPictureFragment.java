package easyrun.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import easyrun.bean.FreePicBean;
import easyrun.bean.UserBean;
import easyrun.server.SendDataToServerByOKHttp;
import easyrun.server.SendDateToServer;
import easyrun.util.GsonTools;
import easyrun.util.R;
import easyrun.util.ServerData;

public class FindPictureFragment extends Fragment {

    private List<FreePicBean> pictureList=new ArrayList<>();
    private View mMainView;
    private UserBean user;
    private OkHttpClient client=new OkHttpClient();

    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SendDateToServer.SEND_SUCCESS:
                    Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
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
        getActivity().getActionBar().hide();
        mMainView = inflater.inflate(R.layout.find_picture, container, false);
        ListView picList = (ListView)mMainView.findViewById(R.id.picListView);

        //获取传递来的用户信息
        Bundle bundle = getArguments();
        user = bundle.getParcelable("userInfo");

        init();//初始化照片列表
        final PictureListAdapter adapter = new PictureListAdapter(mMainView.getContext(), pictureList);

        picList.setAdapter(adapter);
        picList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                ((FoldingCell) view).toggle(false);
                adapter.registerToggle(pos);
            }
        });
        return mMainView;
    }

    protected void init(){
        //从服务器获取照片流跟相关数据
        Thread thread = new Thread() {
            public void run() {
                String account = user.getAccount();//账号
                String password = user.getPassword();//密码
                final String path= ServerData.BaseURL+"FindFreePicture";
                try {
                    //OKHttp
                    //普通键值对post用法
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("account", account)
                            .add("password", password)
                            .build();

                    Request request = new Request.Builder()
                            .url(path)
                            .post(formBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            //Looper.prepare();
                            Toast toast = Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            //  Looper.loop();
                            e.printStackTrace();
                            // handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_NOTCONNECT);
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
                                    Gson gson = new Gson();
                                    List<FreePicBean> list;
                                    list = gson.fromJson(Result,
                                            new TypeToken<List<FreePicBean>>() {
                                            }.getType());
                                    pictureList=list;
                                    for(int i=0;i<list.size();i++)
                                    {
                                        // String path2=ServerData+list.get(i).getEventName()+"/"+

                                        String path1=ServerData.BaseURL+"UserPic/"+list.get(i).getEventName()+"/"+list.get(i).getPicID();
                                        System.out.println("照片Id:"+path1);
                                        Request request = new Request.Builder().url(path1).build();
                                        //获取响应体
                                        ResponseBody body = client.newCall(request).execute().body();
                                        //获取流
                                        InputStream in = body.byteStream();
                                        //转化为bitmap
                                        Bitmap bitmap = BitmapFactory.decodeStream(in);

                                        list.get(i).setPic(bitmap);
                                        System.out.println(bitmap);
                                    }
                                }
                            }
                        }
                    });
                    //OKHttp

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("getUserName-------->" + pictureList.get(0).getUserName());
    }

    @Override
    public void onStop() {
        Log.v("test", "Find Picture Fragment-->onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy(){
        Log.v("test", "Find Picture Fragment-->onDestroy()");
        super.onDestroy();
    }

}
