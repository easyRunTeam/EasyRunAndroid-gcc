package easyrun.activity;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import easyrun.bean.FreePicBean;
import easyrun.bean.UserBean;
import easyrun.server.SendDateToServer;
import easyrun.util.GsonTools;
import easyrun.util.R;
import easyrun.util.ServerData;

public class FindPictureFragment extends Fragment {

    private List<FreePicBean> pictureList;
    private View mMainView;
    private UserBean user;

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
                Map<String, String> map = new HashMap<String, String>();
                map.put("account",account);
                map.put("password",password);
                try {
                    String jsonResult = new SendDateToServer(handler).doPost(map,path);//返回的结果是Json格式
                    Gson gson = new Gson();
                    List<FreePicBean> list;
                    list = gson.fromJson(jsonResult,
                            new TypeToken<List<FreePicBean>>() {
                            }.getType());
                    System.out.println("getUserName-------->"+list.get(0).getUserName());
                    pictureList=list;
                } catch (Exception e) {
                    System.out.println("----------<ERROR>--------");
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
