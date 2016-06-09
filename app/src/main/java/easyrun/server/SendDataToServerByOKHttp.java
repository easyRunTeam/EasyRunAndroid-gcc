package easyrun.server;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import easyrun.activity.MainActivity;
import easyrun.util.ServerData;

/**
 * Created by J_Crocodile on 2016/6/8.
 */
public class SendDataToServerByOKHttp {
    public static final int SEND_SUCCESS = 0x123;
    public static final int SEND_FAIL = 0x124;
    public static final int SEND_NOTCONNECT = 0x125;
    public static final int SEND_NULL = 0x122;
    private Handler handler;
    private final OkHttpClient client = new OkHttpClient();
    private String result;
    public SendDataToServerByOKHttp(Handler handler) {
        // TODO Auto-generated constructor stub
        this.handler = handler;
    }

    public String generatePath(Map<String, String> map, String path)
    {
        StringBuilder sb = new StringBuilder(path);
        sb.append("?");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
       return  sb.toString();
    }

    /**
     * doGet方法
     */

    public String Get(Map<String, String> map, String path) {
        // 拼凑出请求地址
        StringBuilder sb = new StringBuilder(path);
        sb.append("?");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        String path1 = sb.toString();
        try {
            Request request = new Request.Builder()
                    .url(path1)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                byte[] b = response.body().bytes(); //获取数据的bytes
                result = new String(b, "GB2312"); //然后将其转为gb2312
                if (result.equals("failed")) {
                    handler.sendEmptyMessage(SEND_FAIL);
                }
                return result;
            } else {
                handler.sendEmptyMessage(SEND_NOTCONNECT);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


