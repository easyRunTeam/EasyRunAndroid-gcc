package easyrun.shopping.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import easyrun.shopping.model.ClothesEntity;
import easyrun.util.R;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class ClothesAdapter extends BaseListAdapter<ClothesEntity> {

    private boolean isNoData;
    private int mHeight;
    public static final int ONE_SCREEN_COUNT = 7; // 一屏能显示的个数，这个根据屏幕高度和各自的需求定
    public static final int ONE_REQUEST_COUNT = 10; // 一次请求的个数

    public ClothesAdapter(Context context) {
        super(context);
    }

    public ClothesAdapter(Context context, List<ClothesEntity> list) {
        super(context, list);
    }

    // 设置数据
    public void setData(List<ClothesEntity> list) {
        clearAll();
        addALL(list);

        isNoData = false;
        if (list.size() == 1 && list.get(0).isNoData()) {
            // 暂无数据布局
            isNoData = list.get(0).isNoData();
            mHeight = list.get(0).getHeight();
        } else {
            // 添加空数据
            if (list.size() < ONE_SCREEN_COUNT) {
                addALL(createEmptyList(ONE_SCREEN_COUNT - list.size()));
            }
        }
        notifyDataSetChanged();
    }

    // 创建不满一屏的空数据
    public List<ClothesEntity> createEmptyList(int size) {
        List<ClothesEntity> emptyList = new ArrayList<>();
        if (size <= 0) return emptyList;
        for (int i=0; i<size; i++) {
            emptyList.add(new ClothesEntity());
        }
        return emptyList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 暂无数据
        if (isNoData) {
            System.out.println("-----------<暂无数据>-----------");
            convertView = mInflater.inflate(R.layout.item_no_data_layout, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
            RelativeLayout rootView = ButterKnife.findById(convertView, R.id.rl_root_view);
            rootView.setLayoutParams(params);
            return convertView;
        }

        // 正常数据
        final ViewHolder holder;
        if (convertView != null && convertView instanceof LinearLayout) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_clothes, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        ClothesEntity entity = getItem(position);

        holder.llRootView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(entity.getType())) {
            holder.llRootView.setVisibility(View.INVISIBLE);
            return convertView;
        }

        //holder.llRootView.setOnClickListener(itemListener);
        holder.itemID.setText(entity.getId());//主键id
        holder.tvTitle.setText(entity.getBrand()+"  "+ entity.getName());
        holder.tvRank.setText("销量：" + entity.getSaleNumber());
        holder.tvDescribe.setText(entity.getDescribe());
        holder.tvPrice.setText("价格：" + entity.getPrice());
        mImageManager.loadUrlImage(entity.getImgURL(), holder.ivImage);

        return convertView;
    }

    /*LinearLayout.OnClickListener itemListener = new LinearLayout.OnClickListener(){
        @Override
        public void onClick(View v) {
            System.out.println("------------1232312--------------");
        }
    };*/

    static class ViewHolder {
        @Bind(R.id.ll_root_view)
        LinearLayout llRootView;
        @Bind(R.id.iv_image)
        ImageView ivImage;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_rank)
        TextView tvRank;
        @Bind(R.id.tv_describe)
        TextView tvDescribe;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.itemID)
        TextView itemID;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
