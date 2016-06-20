package easyrun.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import easyrun.activity.NonetActivity;

/**
 * Created by gecongcong on 2016/6/1.
 */
public class ServerData {
    /*public static String BaseURL = "http://120.27.106.188/easyRunServer/";*/
    public static String BaseURL = "http://120.27.106.188:8088/EasyRunServer/";

    public static boolean checkNetwork(Activity activity) {// 检测网络
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
           return connManager.getActiveNetworkInfo().isAvailable();

        }
        return false;
    }
}
