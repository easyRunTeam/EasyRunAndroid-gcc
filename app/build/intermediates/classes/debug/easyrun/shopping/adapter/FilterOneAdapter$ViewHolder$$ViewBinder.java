// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FilterOneAdapter$ViewHolder$$ViewBinder<T extends easyrun.shopping.adapter.FilterOneAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625090, "field 'ivImage'");
    target.ivImage = finder.castView(view, 2131625090, "field 'ivImage'");
    view = finder.findRequiredView(source, 2131625091, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131625091, "field 'tvTitle'");
  }

  @Override public void unbind(T target) {
    target.ivImage = null;
    target.tvTitle = null;
  }
}
