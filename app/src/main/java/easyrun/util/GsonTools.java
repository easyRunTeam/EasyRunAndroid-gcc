package easyrun.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import easyrun.bean.FreePicBean;

/**
 * Created by gecongcong on 2016/6/5.
 */
public class GsonTools {
     public GsonTools() {
          // TODO Auto-generated constructor stub
     }

    //使用Gson进行解析Object
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
        // TODO: handle exception
        }
        return t;
    }


     // 使用Gson进行解析 List<Object>
     public static ArrayList<FreePicBean> getFreePicList(String jsonString) {
         ArrayList<FreePicBean> list = new ArrayList<FreePicBean>();
         try {
             Gson gson = new Gson();
             list = gson.fromJson(jsonString, new TypeToken<List<FreePicBean>>() {
             }.getType());
         } catch (Exception e) {
         }
         return list;
     }

}