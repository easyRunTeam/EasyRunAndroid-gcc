// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HeaderFilterViewView$$ViewBinder<T extends easyrun.shopping.view.HeaderFilterViewView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625094, "field 'fvFilter'");
    target.fvFilter = finder.castView(view, 2131625094, "field 'fvFilter'");
  }

  @Override public void unbind(T target) {
    target.fvFilter = null;
  }
}
