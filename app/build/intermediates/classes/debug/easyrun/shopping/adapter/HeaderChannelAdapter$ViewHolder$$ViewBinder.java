// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HeaderChannelAdapter$ViewHolder$$ViewBinder<T extends easyrun.shopping.adapter.HeaderChannelAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625090, "field 'ivImage'");
    target.ivImage = finder.castView(view, 2131625090, "field 'ivImage'");
    view = finder.findRequiredView(source, 2131625091, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131625091, "field 'tvTitle'");
    view = finder.findRequiredView(source, 2131625092, "field 'tvTips'");
    target.tvTips = finder.castView(view, 2131625092, "field 'tvTips'");
  }

  @Override public void unbind(T target) {
    target.ivImage = null;
    target.tvTitle = null;
    target.tvTips = null;
  }
}
