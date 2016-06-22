package easyrun.shoppingDetailFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import easyrun.bean.UserBean;
import easyrun.server.SendDataToServerByOKHttp;
import easyrun.shopping.model.ClothesEntity;
import easyrun.util.R;
import easyrun.util.ServerData;

public class ShoppingDetailsFragment extends Fragment {

    private VerticalFragment1 fragment1;
    private VerticalFragment3 fragment3;
    private DragLayout draglayout;
    private UserBean userInfo;
    private View rootView;
    private String Result;
    private String itemID;  //商品主键ID
    private byte[] responseByte;
    private final OkHttpClient client = new OkHttpClient();
    private ClothesEntity clothesEntity = new ClothesEntity();
    private List<ClothesEntity> clothesList = new ArrayList<>();

    private TextView buyButton;//购买按钮
    private TextView shoppingCartButton;//加入购物车按钮

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SendDataToServerByOKHttp.INIT_SUCCESS:

                    Gson gson = new Gson();
                    clothesList = gson.fromJson(Result,
                            new TypeToken<List<ClothesEntity>>() {}.getType());
                    if(null!=clothesList && clothesList.size()>0){
                        clothesEntity = clothesList.get(0);
                    }
                    initView();
                    System.out.println("-----------<get details succeed>-----------");

                    break;
                case SendDataToServerByOKHttp.SEND_FAIL:
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                    break;
                case SendDataToServerByOKHttp.SEND_NOTCONNECT:
                    Toast.makeText(getActivity(), "网络故障，请重试", Toast.LENGTH_SHORT).show();
                case SendDataToServerByOKHttp.BUY_SUCCESS:
                    Toast.makeText(getActivity(),"购买成功",Toast.LENGTH_SHORT).show();
                    break;
                case SendDataToServerByOKHttp.BUY_NOT_ENOUGH:
                    Toast.makeText(getActivity(),"库存不足",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().hide();
        rootView = inflater.inflate(R.layout.shopping_details, container, false);
        buyButton = (TextView) rootView.findViewById(R.id.buyButton);
        shoppingCartButton = (TextView) rootView.findViewById(R.id.shoppingCartButton);
        buyButton.setOnClickListener(ButtonListener);
        shoppingCartButton.setOnClickListener(ButtonListener);
        Bundle bundle = getArguments();
        itemID = bundle.getString("itemID");
        userInfo = bundle.getParcelable("userInfo");
        getDataFromServer();
        return rootView;
    }

    TextView.OnClickListener ButtonListener = new TextView.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.buyButton://购买商品
                    new Thread() {
                        public void run() {
                            try {
                                String path= ServerData.BaseURL + "BuyClothes";
                                RequestBody formBody = new FormEncodingBuilder()
                                        .add("itemID", itemID)
                                        .add("account", userInfo.getAccount())//账号
                                        .add("saleNumber", clothesEntity.getSaleNumber())//目前销量
                                        .add("repertory",clothesEntity.getRepertory())//剩余库存
                                        .build();
                                Request request = new Request.Builder()
                                        .url(path)
                                        .post(formBody)
                                        .build();

                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Request request, IOException e) {
                                        handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_NOTCONNECT);
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(Response response) throws IOException {
                                        if (!response.isSuccessful()) {
                                            throw new IOException("Unexpected code " + response);
                                        } else {
                                            responseByte = response.body().bytes(); //获取数据的bytes
                                            Result = new String(responseByte, "GB2312"); //然后将其转为gb2312
                                            if (Result.equals("succeed")) {
                                                handler.sendEmptyMessage(SendDataToServerByOKHttp.BUY_SUCCESS);
                                            } else if(Result.equals("failed")) {
                                                handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_FAIL);
                                            }else{
                                                handler.sendEmptyMessage(SendDataToServerByOKHttp.BUY_NOT_ENOUGH);
                                            }
                                        }
                                    }
                                });
                                //OKHttp
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    break;
                case R.id.shoppingCartButton:
                    Toast.makeText(getActivity(),"加入购物车成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 连接数据库，获得数据*/
    private void getDataFromServer(){
        new Thread() {
            public void run() {
                try {
                    //OKHttp
                    String path= ServerData.BaseURL + "GetItemDetails";

                    //普通键值对post用法
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("itemID", itemID)
                            .build();

                    Request request = new Request.Builder()
                            .url(path)
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_NOTCONNECT);
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            } else {
                                responseByte = response.body().bytes(); //获取数据的bytes
                                Result = new String(responseByte, "GB2312"); //然后将其转为gb2312
                                if (!Result.equals("failed")) {
                                    handler.sendEmptyMessage(SendDataToServerByOKHttp.INIT_SUCCESS);
                                } else {
                                    handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_FAIL);
                                }
                            }
                        }
                    });
                    //OKHttp
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 初始化View
     */
    private void initView() {
        TextView price = (TextView)rootView.findViewById(R.id.price);
        price.setText("积分  "+clothesEntity.getPrice());
        fragment1 = new VerticalFragment1();
        //传递商品数据到fragment1
        Bundle bundle = new Bundle();
        bundle.putSerializable("clothesEntity",clothesEntity);
        fragment1.setArguments(bundle);

        fragment3 = new VerticalFragment3();

        getFragmentManager().beginTransaction()
                .add(R.id.first, fragment1).add(R.id.second, fragment3)
                .commit();

        DragLayout.ShowNextPageNotifier nextIntf = new DragLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext() {
                fragment3.initView();
            }
        };
        draglayout = (DragLayout) rootView.findViewById(R.id.draglayout);
        draglayout.setNextPageListener(nextIntf);
    }

}
