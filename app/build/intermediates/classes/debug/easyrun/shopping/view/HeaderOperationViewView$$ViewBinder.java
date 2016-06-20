// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HeaderOperationViewView$$ViewBinder<T extends easyrun.shopping.view.HeaderOperationViewView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625088, "field 'gvOperation'");
    target.gvOperation = finder.castView(view, 2131625088, "field 'gvOperation'");
  }

  @Override public void unbind(T target) {
    target.gvOperation = null;
  }
}
