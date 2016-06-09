package easyrun.activity;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import easyrun.util.R;

/**
 * Created by 91337 on 2016/5/26.
 */
public class ShoppingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("易跑商城");
        getActivity().getActionBar().show();
        return inflater.inflate(R.layout.shopping, container, false);
    }

}
