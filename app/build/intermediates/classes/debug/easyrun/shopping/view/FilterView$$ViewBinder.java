// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FilterView$$ViewBinder<T extends easyrun.shopping.view.FilterView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625207, "field 'tvCategory'");
    target.tvCategory = finder.castView(view, 2131625207, "field 'tvCategory'");
    view = finder.findRequiredView(source, 2131625208, "field 'ivCategoryArrow'");
    target.ivCategoryArrow = finder.castView(view, 2131625208, "field 'ivCategoryArrow'");
    view = finder.findRequiredView(source, 2131625210, "field 'tvSort'");
    target.tvSort = finder.castView(view, 2131625210, "field 'tvSort'");
    view = finder.findRequiredView(source, 2131625211, "field 'ivSortArrow'");
    target.ivSortArrow = finder.castView(view, 2131625211, "field 'ivSortArrow'");
    view = finder.findRequiredView(source, 2131625213, "field 'tvFilter'");
    target.tvFilter = finder.castView(view, 2131625213, "field 'tvFilter'");
    view = finder.findRequiredView(source, 2131625214, "field 'ivFilterArrow'");
    target.ivFilterArrow = finder.castView(view, 2131625214, "field 'ivFilterArrow'");
    view = finder.findRequiredView(source, 2131625206, "field 'llCategory'");
    target.llCategory = finder.castView(view, 2131625206, "field 'llCategory'");
    view = finder.findRequiredView(source, 2131625209, "field 'llSort'");
    target.llSort = finder.castView(view, 2131625209, "field 'llSort'");
    view = finder.findRequiredView(source, 2131625212, "field 'llFilter'");
    target.llFilter = finder.castView(view, 2131625212, "field 'llFilter'");
    view = finder.findRequiredView(source, 2131625217, "field 'lvLeft'");
    target.lvLeft = finder.castView(view, 2131625217, "field 'lvLeft'");
    view = finder.findRequiredView(source, 2131625218, "field 'lvRight'");
    target.lvRight = finder.castView(view, 2131625218, "field 'lvRight'");
    view = finder.findRequiredView(source, 2131625205, "field 'llHeadLayout'");
    target.llHeadLayout = finder.castView(view, 2131625205, "field 'llHeadLayout'");
    view = finder.findRequiredView(source, 2131625216, "field 'llContentListView'");
    target.llContentListView = finder.castView(view, 2131625216, "field 'llContentListView'");
    view = finder.findRequiredView(source, 2131625215, "field 'viewMaskBg'");
    target.viewMaskBg = view;
  }

  @Override public void unbind(T target) {
    target.tvCategory = null;
    target.ivCategoryArrow = null;
    target.tvSort = null;
    target.ivSortArrow = null;
    target.tvFilter = null;
    target.ivFilterArrow = null;
    target.llCategory = null;
    target.llSort = null;
    target.llFilter = null;
    target.lvLeft = null;
    target.lvRight = null;
    target.llHeadLayout = null;
    target.llContentListView = null;
    target.viewMaskBg = null;
  }
}
