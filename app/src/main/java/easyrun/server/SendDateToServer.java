package easyrun.server;

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * Created by gecongcong on 2016/6/1.
 */
public class SendDateToServer {
    public static final int SEND_SUCCESS=0x123;
    public static final int SEND_FAIL=0x124;
    public static final int SEND_ERROR=0x125;
    public static final int SEND_NULL=0x122;
    private HttpURLConnection conn;
    private URL url;
    public String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private DataOutputStream ds;
    private Handler handler;

    public SendDateToServer(Handler handler) {
        // TODO Auto-generated constructor stub
        this.handler=handler;
    }

    /**
     * doGet方法*/
    public Boolean doGet(Map<String, String> map, String path)
            throws Exception {
        // 拼凑出请求地址
        StringBuilder sb = new StringBuilder(path);
        sb.append("?");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        String str = sb.toString();
        System.out.println(str);
        URL Url = new URL(str);
        HttpURLConnection HttpConn = (HttpURLConnection) Url.openConnection();
        HttpConn.setRequestMethod("GET");
        HttpConn.setReadTimeout(5000);
        // GET方式的请求不用设置什么DoOutPut()之类的吗？
        if (HttpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return true;
        }
        return false;
    }

    /**只有Text的
     * doPost方法*/
    public String doPost(Map<String, String> map, String path){
        String result ="";
        try{
            // 注意Post地址中是不带参数的，所以newURL的时候要注意不能加上后面的参数
            URL Url = new URL(path);
            // Post方式提交的时候参数和URL是分开提交的，参数形式是这样子的：name=y&age=6
            StringBuilder sb = new StringBuilder();
            // sb.append("?");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            String str = sb.toString();
            HttpURLConnection HttpConn = (HttpURLConnection) Url.openConnection();
            HttpConn.setRequestMethod("POST");
            HttpConn.setReadTimeout(5000);
            HttpConn.setDoOutput(true);
            HttpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            HttpConn.setRequestProperty("Charset","utf-8");
            HttpConn.setRequestProperty("Content-Length",
                    String.valueOf(str.getBytes().length));
            OutputStream os = HttpConn.getOutputStream();
            os.write(str.getBytes());
            if (HttpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 获取响应的输入流对象
                InputStream is = HttpConn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream byteos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    byteos.write(buffer, 0, len);
                }
                is.close();// 释放资源
                byteos.close();
                result = new String(byteos.toByteArray());// 返回字符串
                System.out.println("\n" + result + "\n");
            }else{
                handler.sendEmptyMessage(SEND_FAIL);
            }
            if(result.equals("succeed")){
                handler.sendEmptyMessage(SEND_SUCCESS);//通知主线程数据发送成功
            }else{
                handler.sendEmptyMessage(SEND_FAIL);
            }
            return result;
        }catch (Exception e){
            handler.sendEmptyMessage(SEND_ERROR);
            e.printStackTrace();
            return result;
        }
    }

    public void setUrl(String url) throws Exception {
        this.url = new URL(url);
    }

    /**包含文件的
     * doPost方法*/
    public String doPostMulti(Map<String, String> textParams,Map<String,File> fileParams, String path){
        String result="";
        try {
            initConnection(path);
            conn.connect();
            ds = new DataOutputStream(conn.getOutputStream());
            writeFileParams(fileParams,ds);
            writeStringParams(textParams,ds);
            paramsEnd(ds);
            int code = conn.getResponseCode();
            if (code == 200) {  //成功代码
                InputStream in = conn.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024 * 8];
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                conn.disconnect();
                result = new String(out.toByteArray(), "utf-8");// 返回字符串
            }
            if(result.equals("succeed")){
                handler.sendEmptyMessage(SEND_SUCCESS);//通知主线程数据发送成功
                return result;
            }else{
                handler.sendEmptyMessage(SEND_FAIL);
                return null;
            }
        }catch (Exception e){
            handler.sendEmptyMessage(SEND_ERROR);
            e.printStackTrace();
            return result;
        }
    }

    /** 文件上传的connection的一些必须设置
     * @throws Exception
     */
    private void initConnection(String path) throws Exception {
        setUrl(path);
        System.out.println("\nURL="+this.url+"\n");
        conn = (HttpURLConnection) this.url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(10000); // 连接超时为10秒
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
    }

    /**
     * 普通字符串数据
     * @throws Exception
     * */
    private void writeStringParams(Map<String, String> textParams,DataOutputStream ds) throws Exception {
        Set<String> keySet = textParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String name = it.next();
            String value = textParams.get(name);
            ds.writeBytes("--" + BOUNDARY + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
            ds.writeBytes("\r\n");
            ds.writeBytes(encode(value) + "\r\n");
        }
    }

    /**
     * 文件数据
     * @throws Exception
     * */
    private void writeFileParams(Map<String, File> fileparams,DataOutputStream ds) throws Exception {
        Set<String> keySet = fileparams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String name = it.next();
            File value = fileparams.get(name);
            ds.writeBytes("--" + BOUNDARY + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + encode(value.getName()) + "\"\r\n");
            ds.writeBytes("Content-Type: " + getContentType(value) + "\r\n");
            ds.writeBytes("\r\n");
            ds.write(getBytes(value));
            ds.writeBytes("\r\n");
        }
    }

    /**获取文件的上传类型
     * 图片格式为image/png,image/jpeg等
     * 非图片为application /octet-stream
     * @param f
     * @return
     * @throws Exception
     * */
    private String getContentType(File f) throws Exception {
        return "application/octet-stream";
    }

    /** 把文件转换成字节数组
     * @param f
     * @return
     * @throws Exception
     */
    private byte[] getBytes(File f) throws Exception {
        FileInputStream in = new FileInputStream(f.getAbsoluteFile());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        in.close();
        return out.toByteArray();
    }

    /*** 添加结尾数据
     * @throws Exception
     */
    private void paramsEnd(DataOutputStream ds) throws Exception {
        ds.writeBytes("--" + BOUNDARY + "--" + "\r\n");
        ds.writeBytes("\r\n");
    }

    /** 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
     * @param value
     * @return
     * @throws Exception
     */
    private String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }
}