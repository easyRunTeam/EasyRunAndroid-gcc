// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FilterLeftAdapter$ViewHolder$$ViewBinder<T extends easyrun.shopping.adapter.FilterLeftAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625099, "field 'llRootView'");
    target.llRootView = finder.castView(view, 2131625099, "field 'llRootView'");
    view = finder.findRequiredView(source, 2131625083, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131625083, "field 'tvTitle'");
  }

  @Override public void unbind(T target) {
    target.llRootView = null;
    target.tvTitle = null;
  }
}
