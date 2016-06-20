// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Sp_shoes_subFragment$$ViewBinder<T extends easyrun.shopping.ui.Sp_shoes_subFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625080, "field 'smoothListView'");
    target.smoothListView = finder.castView(view, 2131625080, "field 'smoothListView'");
    view = finder.findRequiredView(source, 2131625086, "field 'fvTopFilter'");
    target.fvTopFilter = finder.castView(view, 2131625086, "field 'fvTopFilter'");
    view = finder.findRequiredView(source, 2131625081, "field 'rlBar'");
    target.rlBar = finder.castView(view, 2131625081, "field 'rlBar'");
    view = finder.findRequiredView(source, 2131625083, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131625083, "field 'tvTitle'");
    view = finder.findRequiredView(source, 2131625082, "field 'viewTitleBg'");
    target.viewTitleBg = view;
    view = finder.findRequiredView(source, 2131625085, "field 'viewActionMoreBg'");
    target.viewActionMoreBg = view;
  }

  @Override public void unbind(T target) {
    target.smoothListView = null;
    target.fvTopFilter = null;
    target.rlBar = null;
    target.tvTitle = null;
    target.viewTitleBg = null;
    target.viewActionMoreBg = null;
  }
}
