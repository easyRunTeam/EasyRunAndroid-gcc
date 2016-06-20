package easyrun.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.special.ResideMenu.ResideMenu;
import easyrun.adapter.ShoppingPicAdapter;
import easyrun.shopping.ui.Sp_shoes_subFragment;
import easyrun.util.R;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by gecongcong on 2016/5/26.
 */
public class ShoppingFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;
    private ImageView clothes;//菜单项——服饰
    private ImageView shoes;//菜单项——鞋子
    private ImageView hotel;//菜单项——住宿
    private ImageView bus;//菜单项——交通
    private ImageView new_things;//菜单项——新品

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("易跑商城");
        getActivity().getActionBar().show();
        parentView = inflater.inflate(R.layout.shopping, container, false);
        MainActivity parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        clothes = (ImageView) parentView.findViewById(R.id.clothes);
        shoes = (ImageView) parentView.findViewById(R.id.shoes);
        hotel = (ImageView) parentView.findViewById(R.id.hotel);
        bus = (ImageView) parentView.findViewById(R.id.bus);
        new_things = (ImageView) parentView.findViewById(R.id.new_things);
        clothes.setOnClickListener(ItemListener);
        // add gesture operation's ignored views
        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
        return parentView;
    }

    ImageView.OnClickListener ItemListener = new ImageView.OnClickListener(){
        @Override
        public void onClick(View v) {
            //鼠标点击菜单项进入相应的子商城
            switch (v.getId()){
                case R.id.clothes:
                    changeFragment(new Sp_shoes_subFragment());
                    break;
                case R.id.shoes:
                    changeFragment(new Sp_shoes_subFragment());
                    break;
                case R.id.hotel:
                    break;
                case R.id.bus:
                    break;
                case R.id.new_things:
                    break;
            }
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(new ShoppingPicAdapter());
        indicator.setViewPager(viewpager);
        viewpager.setCurrentItem(0);
    }

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
