package easyrun.shopping.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import easyrun.bean.FreePicBean;
import easyrun.server.SendDataToServerByOKHttp;
import easyrun.shopping.adapter.ClothesAdapter;
import easyrun.shopping.model.ChannelEntity;
import easyrun.shopping.model.ClothesEntity;
import easyrun.shopping.model.FilterData;
import easyrun.shopping.model.FilterEntity;
import easyrun.shopping.model.FilterTwoEntity;
import easyrun.shopping.model.OperationEntity;
import easyrun.util.ColorUtil;
import easyrun.util.DensityUtil;
import easyrun.util.ModelUtil;
import easyrun.shopping.view.*;
import easyrun.shopping.view.SmoothListView.SmoothListView;
import easyrun.util.R;
import easyrun.util.ServerData;

/**
 * Created by gecongcong on 2016/6/19.
 */
public class Sp_shoes_subFragment extends Fragment implements SmoothListView.ISmoothListViewListener {

    @Bind(R.id.listView)
    SmoothListView smoothListView;
    @Bind(R.id.fv_top_filter)
    FilterView fvTopFilter;
    @Bind(R.id.rl_bar)
    RelativeLayout rlBar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.view_title_bg)
    View viewTitleBg;
    @Bind(R.id.view_action_more_bg)
    View viewActionMoreBg;
    private final OkHttpClient client = new OkHttpClient();
    private byte[] responseByte;
    private String Result="failed";

    private View mMainView;
    private Context mContext;
    private Activity mActivity;
    private int mScreenHeight; // 屏幕高度

    private List<String> adList = new ArrayList<>(); // 广告数据
    private List<ChannelEntity> channelList = new ArrayList<>(); // 频道数据
    private List<OperationEntity> operationList = new ArrayList<>(); // 运营数据
    private List<ClothesEntity> clothesList = new ArrayList<>(); // ListView数据

    private HeaderAdViewView listViewAdHeaderView; // 广告视图
    private HeaderChannelViewView headerChannelView; // 频道视图
    private HeaderOperationViewView headerOperationViewView; // 运营视图
    private HeaderDividerViewView headerDividerViewView; // 分割线占位图
    private HeaderFilterViewView headerFilterViewView; // 分类筛选视图
    private FilterData filterData; // 筛选数据
    private ClothesAdapter mAdapter; // 主页数据

    private View itemHeaderAdView; // 从ListView获取的广告子View
    private View itemHeaderFilterView; // 从ListView获取的筛选子View
    private boolean isScrollIdle = true; // ListView是否在滑动
    private boolean isStickyTop = false; // 是否吸附在顶部
    private boolean isSmooth = false; // 没有吸附的前提下，是否在滑动
    private int titleViewHeight = 50; // 标题栏的高度
    private int filterPosition = -1; // 点击FilterView的位置：分类(0)、排序(1)、筛选(2)

    private int adViewHeight = 180; // 广告视图的高度
    private int adViewTopSpace; // 广告视图距离顶部的距离

    private int filterViewPosition = 4; // 筛选视图的位置
    private int filterViewTopSpace; // 筛选视图距离顶部的距离

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SendDataToServerByOKHttp.SEND_SUCCESS:

                    Gson gson = new Gson();
                    clothesList = gson.fromJson(Result,
                            new TypeToken<List<ClothesEntity>>() {}.getType());
                    initView();
                    initListener();
                    System.out.println("-----------<success>-----------");
                    break;
                case SendDataToServerByOKHttp.SEND_FAIL:
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                    break;
                case SendDataToServerByOKHttp.SEND_NOTCONNECT:
                    Toast.makeText(getActivity(), "网络故障，请重试", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this,mMainView);
        initData();
        return mMainView;
    }

    private void getDataFromServer(){

        new Thread() {
            public void run() {
                try {
                    //OKHttp
                    String path=ServerData.BaseURL + "GetClothesPageResource";
                    Request request = new Request.Builder()
                            .url(path)
                            .get()
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
                                    handler.sendEmptyMessage(SendDataToServerByOKHttp.SEND_SUCCESS);
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

    private void initData() {
        mContext = getActivity();
        mActivity = getActivity();
        mScreenHeight = DensityUtil.getWindowHeight(getActivity());

        //从服务器端获取数据
        getDataFromServer();

        // 筛选数据
        filterData = new FilterData();
        filterData.setCategory(ModelUtil.getCategoryData());
        filterData.setSorts(ModelUtil.getSortData());
        filterData.setFilters(ModelUtil.getFilterData());

        // 广告数据
        adList = ModelUtil.getAdData();

        // 频道数据
        channelList = ModelUtil.getChannelData();

        // 运营数据
        operationList = ModelUtil.getOperationData();

        // ListView数据
        //clothesList = ModelUtil.getTravelingData(clothesList);
        for(int i=0;i<clothesList.size();i++) {
            ClothesEntity clothes = clothesList.get(i);
            System.out.println("Brand:"+clothes.getBrand());
            System.out.println("Name:" + clothes.getName());
        }
    }

    private void initView() {
        fvTopFilter.setVisibility(View.INVISIBLE);

        // 设置筛选数据
        fvTopFilter.setFilterData(mActivity, filterData);

        // 设置广告数据
        listViewAdHeaderView = new HeaderAdViewView(getActivity());
        listViewAdHeaderView.fillView(adList, smoothListView);

        // 设置频道数据
        headerChannelView = new HeaderChannelViewView(getActivity());
        headerChannelView.fillView(channelList, smoothListView);

        // 设置运营数据
        headerOperationViewView = new HeaderOperationViewView(getActivity());
        headerOperationViewView.fillView(operationList, smoothListView);

        // 设置分割线
        headerDividerViewView = new HeaderDividerViewView(getActivity());
        headerDividerViewView.fillView("", smoothListView);

        // 设置筛选数据
        headerFilterViewView = new HeaderFilterViewView(getActivity());
        headerFilterViewView.fillView(new Object(), smoothListView);

        // 设置ListView数据
        mAdapter = new ClothesAdapter(getActivity(), clothesList);
        smoothListView.setAdapter(mAdapter);

        filterViewPosition = smoothListView.getHeaderViewsCount() - 1;
    }

    private void initListener() {

        // (假的ListView头部展示的)筛选视图点击
        headerFilterViewView.setOnFilterClickListener(new HeaderFilterViewView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                isSmooth = true;
                smoothListView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(mContext, titleViewHeight));
            }
        });

        // (真正的)筛选视图点击
        fvTopFilter.setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                if (isStickyTop) {
                    filterPosition = position;
                    fvTopFilter.showFilterLayout(position);
                    if (titleViewHeight - 3 > filterViewTopSpace || filterViewTopSpace > titleViewHeight + 3) {
                        smoothListView.smoothScrollToPositionFromTop(filterViewPosition, DensityUtil.dip2px(mContext, titleViewHeight));
                    }
                }
            }
        });

        // 分类Item点击
        fvTopFilter.setOnItemCategoryClickListener(new FilterView.OnItemCategoryClickListener() {
            @Override
            public void onItemCategoryClick(FilterTwoEntity entity) {
                fillAdapter(ModelUtil.getCategoryData(entity, clothesList));
            }
        });

        // 排序Item点击
        fvTopFilter.setOnItemSortClickListener(new FilterView.OnItemSortClickListener() {
            @Override
            public void onItemSortClick(FilterEntity entity) {
                fillAdapter(ModelUtil.getSortData(entity, clothesList));
            }
        });

        // 筛选Item点击
        fvTopFilter.setOnItemFilterClickListener(new FilterView.OnItemFilterClickListener() {
            @Override
            public void onItemFilterClick(FilterEntity entity) {
                fillAdapter(ModelUtil.getFilterData(entity, clothesList));
            }
        });

        smoothListView.setRefreshEnable(true);
        smoothListView.setLoadMoreEnable(true);
        smoothListView.setSmoothListViewListener(this);
        smoothListView.setOnScrollListener(new SmoothListView.OnSmoothScrollListener() {
            @Override
            public void onSmoothScrolling(View view) {}

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrollIdle = (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScrollIdle && adViewTopSpace < 0) return;

                // 获取广告头部View、自身的高度、距离顶部的高度
                if (itemHeaderAdView == null) {
                    itemHeaderAdView = smoothListView.getChildAt(1-firstVisibleItem);
                }
                if (itemHeaderAdView != null) {
                    adViewTopSpace = DensityUtil.px2dip(mContext, itemHeaderAdView.getTop());
                    adViewHeight = DensityUtil.px2dip(mContext, itemHeaderAdView.getHeight());
                }

                // 获取筛选View、距离顶部的高度
                if (itemHeaderFilterView == null) {
                    itemHeaderFilterView = smoothListView.getChildAt(filterViewPosition - firstVisibleItem);
                }
                if (itemHeaderFilterView != null) {
                    filterViewTopSpace = DensityUtil.px2dip(mContext, itemHeaderFilterView.getTop());
                }

                // 处理筛选是否吸附在顶部
                if (filterViewTopSpace > titleViewHeight) {
                    isStickyTop = false; // 没有吸附在顶部
                    fvTopFilter.setVisibility(View.INVISIBLE);
                } else {
                    isStickyTop = true; // 吸附在顶部
                    fvTopFilter.setVisibility(View.VISIBLE);
                }

                if (firstVisibleItem > filterViewPosition) {
                    isStickyTop = true;
                    fvTopFilter.setVisibility(View.VISIBLE);
                }

                if (isSmooth && isStickyTop) {
                    isSmooth = false;
                    fvTopFilter.showFilterLayout(filterPosition);
                }

                fvTopFilter.setStickyTop(isStickyTop);

                // 处理标题栏颜色渐变
                handleTitleBarColorEvaluate();
            }
        });
    }

    // 填充数据
    private void fillAdapter(List<ClothesEntity> list) {
        if (list == null || list.size() == 0) {
            smoothListView.setLoadMoreEnable(false);
            int height = mScreenHeight - DensityUtil.dip2px(mContext, 95); // 95 = 标题栏高度 ＋ FilterView的高度
            mAdapter.setData(ModelUtil.getNoDataEntity(height));
        } else {
            smoothListView.setLoadMoreEnable(list.size() > ClothesAdapter.ONE_REQUEST_COUNT);
            mAdapter.setData(list);
        }
    }

    // 处理标题栏颜色渐变
    private void handleTitleBarColorEvaluate() {
        float fraction;
        if (adViewTopSpace > 0) {
            fraction = 1f - adViewTopSpace * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            rlBar.setAlpha(fraction);
            return ;
        }

        float space = Math.abs(adViewTopSpace) * 1f;
        fraction = space / (adViewHeight - titleViewHeight);
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        rlBar.setAlpha(1f);

        if (fraction >= 1f || isStickyTop) {
            isStickyTop = true;
            viewTitleBg.setAlpha(0f);
            viewActionMoreBg.setAlpha(0f);
            rlBar.setBackgroundColor(mContext.getResources().getColor(R.color.ios_purple));
        } else {
            viewTitleBg.setAlpha(1f - fraction);
            viewActionMoreBg.setAlpha(1f - fraction);
            rlBar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(mContext, fraction, R.color.transparent, R.color.ios_purple));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listViewAdHeaderView != null) {
            listViewAdHeaderView.stopADRotate();
        }
    }


    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothListView.stopRefresh();
                smoothListView.setRefreshTime("刚刚");
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothListView.stopLoadMore();
            }
        }, 2000);
    }

}
