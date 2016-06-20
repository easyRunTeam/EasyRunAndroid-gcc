// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HeaderChannelViewView$$ViewBinder<T extends easyrun.shopping.view.HeaderChannelViewView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625093, "field 'gvChannel'");
    target.gvChannel = finder.castView(view, 2131625093, "field 'gvChannel'");
  }

  @Override public void unbind(T target) {
    target.gvChannel = null;
  }
}
