package easyrun.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
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
public class RegisterActivity extends Activity {

    private BootstrapEditText e_account;
    private BootstrapEditText e_password;
    private Button nextStep;
    private BootstrapCircleThumbnail faceImage;
    private String[] items = new String[] { "选择本地图片", "拍照"};
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";
    private String picPath;//头像在本机的存储路径
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SendDateToServer.SEND_SUCCESS:
                    Toast.makeText(RegisterActivity.this, "服务器成功接收", Toast.LENGTH_SHORT).show();
                    break;
                case SendDateToServer.SEND_ERROR:
                    Toast.makeText(RegisterActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case SendDateToServer.SEND_FAIL:
                    Toast.makeText(RegisterActivity.this, "注册失败，用户名已存在", Toast.LENGTH_SHORT).show();
                    break;
                case SendDateToServer.SEND_NULL:
                    Toast.makeText(RegisterActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.register);
        e_account = (BootstrapEditText) findViewById(R.id.account);
        e_password = (BootstrapEditText) findViewById(R.id.password);
        nextStep = (Button) findViewById(R.id.next_step);
        nextStep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 检测网络
                if (!ServerData.checkNetwork(RegisterActivity.this)) {
                    Toast toast = Toast.makeText(RegisterActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                new Thread() {
                    public void run() {
                        String account = e_account.getText().toString();
                        String password = e_password.getText().toString();
                        final String path= ServerData.BaseURL+"RegisterBase";
                        if (account.equals("")||password.equals("")) {
                            handler.sendEmptyMessage(SendDateToServer.SEND_NULL);
                        }else{
                            Map<String, String> textParams = new HashMap<String, String>();
                            textParams.put("account",account);
                            textParams.put("password",password);
                            Map<String,File> fileParams = new HashMap<String,File>();
                            File file = new File(picPath);//picPath:头像在本机的存储路径
                            System.out.println("\npicPath="+picPath+"\n");
                            System.out.println("\nfile="+file+"\n");
                            fileParams.put("headIMG", file);
                            try {
                                String result = new SendDateToServer(handler).doPostMulti(textParams,fileParams,path);
                                if(result.equals("succeed")){
                                    //服务器接收数据成功，跳转到详细信息填写页面
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("account",account);
                                    bundle.putString("password",password);
                                    bundle.putString("headIMG_path",picPath);
                                    intent.putExtras(bundle);
                                    intent.setClass(RegisterActivity.this, RegisterActivity2.class);
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
        });

        faceImage = (BootstrapCircleThumbnail)findViewById(R.id.userIMG);//头像
        faceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (Tools.hasSdcard()) {
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory()
                                        + IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(RegisterActivity.this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }

                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 显示选择对话框
     */
    private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); 	//设置文件类型
                                intentFromGallery
                                        .setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery,
                                        IMAGE_REQUEST_CODE);
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

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 1024);
        intent.putExtra("outputY", 1024);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 2);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            faceImage.setImageDrawable(drawable);

            //将头像存入文件
            Bitmap imgBit = Tools.drawableToBitmap(faceImage.getDrawable());
            String img_url = Tools.getSdCardPath()+"/headIMG/";
            picPath = img_url+"icon.jpg";
            Tools.setPicToView(imgBit, img_url);
        }
    }
}
