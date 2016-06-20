// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HeaderAdViewView$$ViewBinder<T extends easyrun.shopping.view.HeaderAdViewView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625091, "field 'vpAd'");
    target.vpAd = finder.castView(view, 2131625091, "field 'vpAd'");
    view = finder.findRequiredView(source, 2131625092, "field 'llIndexContainer'");
    target.llIndexContainer = finder.castView(view, 2131625092, "field 'llIndexContainer'");
  }

  @Override public void unbind(T target) {
    target.vpAd = null;
    target.llIndexContainer = null;
  }
}
