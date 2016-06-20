// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HeaderOperationAdapter$ViewHolder$$ViewBinder<T extends easyrun.shopping.adapter.HeaderOperationAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625097, "field 'ivImage'");
    target.ivImage = finder.castView(view, 2131625097, "field 'ivImage'");
    view = finder.findRequiredView(source, 2131625083, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131625083, "field 'tvTitle'");
    view = finder.findRequiredView(source, 2131625106, "field 'tvSubtitle'");
    target.tvSubtitle = finder.castView(view, 2131625106, "field 'tvSubtitle'");
  }

  @Override public void unbind(T target) {
    target.ivImage = null;
    target.tvTitle = null;
    target.tvSubtitle = null;
  }
}
