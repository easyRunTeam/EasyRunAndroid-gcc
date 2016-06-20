package easyrun.adapter;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import easyrun.util.R;

/**
 * Created by gecongcong on 2016/6/19.
 */
public class ShoppingPicAdapter extends PagerAdapter {
    private final Random random = new Random();
    private int mSize;

    public ShoppingPicAdapter() {
        mSize = 5;
    }

    public ShoppingPicAdapter(int count) {
        mSize = count;
    }

    @Override public int getCount() {
        return mSize;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override public Object instantiateItem(ViewGroup view, int position) {
        TextView textView = new TextView(view.getContext());
        switch (position){
            case 0:
                textView.setBackgroundResource(R.drawable.scroll_default);
                break;
            case 1:
                textView.setBackgroundResource(R.drawable.scroll_one);
                break;
            case 2:
                textView.setBackgroundResource(R.drawable.scroll_four);
                break;
            case 3:
                textView.setBackgroundResource(R.drawable.scroll_five);
                break;
            case 4:
                textView.setBackgroundResource(R.drawable.scroll_three);
                break;
            default:
                textView.setBackgroundResource(R.drawable.scroll_default);
                break;
        }
        textView.setGravity(Gravity.CENTER);
        view.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return textView;
    }

    public void addItem() {
        mSize++;
        notifyDataSetChanged();
    }

    public void removeItem() {
        mSize--;
        mSize = mSize < 0 ? 0 : mSize;

        notifyDataSetChanged();
    }
}