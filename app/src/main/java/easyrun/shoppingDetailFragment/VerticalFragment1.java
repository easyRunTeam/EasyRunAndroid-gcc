package easyrun.shoppingDetailFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import easyrun.shopping.manager.ImageManager;
import easyrun.shopping.model.ClothesEntity;
import easyrun.util.R;

/**
 * 商城真正的详细数据页面
 *
 * */
public class VerticalFragment1 extends Fragment {

    private ClothesEntity clothesEntity = new ClothesEntity();
    private View rootView;

    static class ViewHolder {
        @Bind(R.id.itemIMG)
        ImageView itemIMG;
        @Bind(R.id.itemName)
        TextView itemName;
        @Bind(R.id.itemPrice)
        TextView itemPrice;
        @Bind(R.id.itemDescribe)
        TextView itemDescribe;
        @Bind(R.id.itemSaleNum)
        TextView itemSaleNum;
        @Bind(R.id.customer_send_IMG)
        ImageView customer_send_IMG;
        @Bind(R.id.repertory)
        TextView repertory;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        clothesEntity = (ClothesEntity)bundle.getSerializable("clothesEntity");
        rootView = inflater.inflate(R.layout.vertical_fragment1, null);
        initView();
        return rootView;
    }


    public void initView(){
        final ViewHolder holder = new ViewHolder(rootView);
        ImageManager mImageManager = new ImageManager(getActivity());
        mImageManager.loadUrlImage(clothesEntity.getImgURL(), holder.itemIMG);
        mImageManager.loadUrlImage(clothesEntity.getImgURL(), holder.customer_send_IMG);
        holder.itemName.setText(clothesEntity.getName());
        holder.repertory.setText("库存："+clothesEntity.getRepertory());
        holder.itemDescribe.setText("品牌：" + clothesEntity.getBrand() + "\n"
                + "描述：" + clothesEntity.getDescribe());
        holder.itemPrice.setText("积分  "+clothesEntity.getPrice());
        holder.itemSaleNum.setText("销量："+clothesEntity.getSaleNumber()+"件");
    }
}
