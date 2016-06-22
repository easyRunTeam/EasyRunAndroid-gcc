package easyrun.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.special.ResideMenu.ResideMenu;

import easyrun.bean.UserBean;
import easyrun.shopping.manager.ImageManager;
import easyrun.util.R;
import easyrun.util.ServerData;

/**
 * Created by J_Crocodile on 2016/6/21.
 */
public class MasterUploadSuccessFragment extends Fragment{
    private TextView Tname;
    private ImageView pic;
    private TextView TaID;
    private View parentView;
    private ResideMenu resideMenu;
    private String name;
    private String path;
    private int aID;
    private ImageManager imageManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // getActivity().getActionBar().setTitle("上传成功");
        // getActivity().getActionBar().show();
        parentView = inflater.inflate(R.layout.master_uploadpic_success, container, false);
        MainActivity parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        imageManager=new ImageManager(parentActivity.getBaseContext());
        Bundle bundle = getArguments();
        name=bundle.getString("name");
        aID=bundle.getInt("aID");
        path=bundle.getString("path");
        System.out.println(name+" "+aID+" "+path);
        Tname=(TextView)parentView.findViewById(R.id.Name);
        TaID=(TextView)parentView.findViewById(R.id.aID);
        // add gesture operation's ignored views
        pic=(ImageView)parentView.findViewById(R.id.athlete);
        Tname.setText("姓名：" + name);
        TaID.setText("号码牌：" + aID);
        //System.out.println(ServerData.BaseURL + "Athlete/pic/" + path);
        imageManager.loadUrlImage("http://120.27.106.188:8088/"+"Athlete/pic/"+path,pic);
        return parentView;
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
