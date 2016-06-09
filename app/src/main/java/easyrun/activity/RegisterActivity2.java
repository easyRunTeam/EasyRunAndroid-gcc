package easyrun.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import easyrun.server.SendDateToServer;
import easyrun.util.R;
import easyrun.util.ServerData;
import easyrun.util.Tools;

/**
 * Created by gecongcong on 2016/6/2.
 */
public class RegisterActivity2 extends Activity {

    private BootstrapEditText e_username;
    private BootstrapEditText e_email;
    private Button submit;
    private BootstrapCircleThumbnail faceImage;
    private String headIMG_path;//头像路径
    private String account;
    private String password;

    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SendDateToServer.SEND_SUCCESS:
                    Toast.makeText(RegisterActivity2.this, "服务器成功接收", Toast.LENGTH_SHORT).show();
                    break;
                case SendDateToServer.SEND_ERROR:
                    Toast.makeText(RegisterActivity2.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case SendDateToServer.SEND_FAIL:
                    Toast.makeText(RegisterActivity2.this, "注册失败，用户名已存在", Toast.LENGTH_SHORT).show();
                    break;
                case SendDateToServer.SEND_NULL:
                    Toast.makeText(RegisterActivity2.this, "亲，请填写完上述内容", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActionBar().hide();
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//解决软键盘遮挡
        setContentView(R.layout.register2);
        e_username = (BootstrapEditText) findViewById(R.id.username);
        e_email = (BootstrapEditText) findViewById(R.id.email);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(ButtonListener);
        faceImage = (BootstrapCircleThumbnail) findViewById(R.id.userIMG);
        Bundle bundle = getIntent().getExtras();
        headIMG_path = bundle.getString("headIMG_path");
        account = bundle.getString("account");
        password = bundle.getString("password");
        Bitmap photo = Tools.getPic(headIMG_path);
        Drawable drawable = new BitmapDrawable(photo);
        faceImage.setImageDrawable(drawable);//读取本地头像显示
    }

    Button.OnClickListener ButtonListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (!ServerData.checkNetwork(RegisterActivity2.this)) {// 检测网络
                Toast toast = Toast.makeText(RegisterActivity2.this,"网络未连接", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            new Thread() {
                public void run() {
                    String username = e_username.getText().toString();
                    String email = e_email.getText().toString();
                    final String path= ServerData.BaseURL+"RegisterBase2";
                    if (username.equals("")||email.equals("")) {
                        handler.sendEmptyMessage(SendDateToServer.SEND_NULL);
                    }else{
                        Map<String, String> textParams = new HashMap<String, String>();
                        textParams.put("username", username);
                        textParams.put("password",password);
                        textParams.put("account",account);
                        textParams.put("email",email);
                        try {
                            String result = new SendDateToServer(handler).doPost(textParams, path);
                            if(result.equals("succeed")){
                                //服务器接收数据成功，跳转到详细信息填写页面
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("headIMG_path",headIMG_path);
                                intent.putExtras(bundle);
                                intent.setClass(RegisterActivity2.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0, 0);//取消切换的动画效果
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    };
}
