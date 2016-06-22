// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FilterView$$ViewBinder<T extends easyrun.shopping.view.FilterView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625227, "field 'tvCategory'");
    target.tvCategory = finder.castView(view, 2131625227, "field 'tvCategory'");
    view = finder.findRequiredView(source, 2131625228, "field 'ivCategoryArrow'");
    target.ivCategoryArrow = finder.castView(view, 2131625228, "field 'ivCategoryArrow'");
    view = finder.findRequiredView(source, 2131625230, "field 'tvSort'");
    target.tvSort = finder.castView(view, 2131625230, "field 'tvSort'");
    view = finder.findRequiredView(source, 2131625231, "field 'ivSortArrow'");
    target.ivSortArrow = finder.castView(view, 2131625231, "field 'ivSortArrow'");
    view = finder.findRequiredView(source, 2131625233, "field 'tvFilter'");
    target.tvFilter = finder.castView(view, 2131625233, "field 'tvFilter'");
    view = finder.findRequiredView(source, 2131625234, "field 'ivFilterArrow'");
    target.ivFilterArrow = finder.castView(view, 2131625234, "field 'ivFilterArrow'");
    view = finder.findRequiredView(source, 2131625226, "field 'llCategory'");
    target.llCategory = finder.castView(view, 2131625226, "field 'llCategory'");
    view = finder.findRequiredView(source, 2131625229, "field 'llSort'");
    target.llSort = finder.castView(view, 2131625229, "field 'llSort'");
    view = finder.findRequiredView(source, 2131625232, "field 'llFilter'");
    target.llFilter = finder.castView(view, 2131625232, "field 'llFilter'");
    view = finder.findRequiredView(source, 2131625237, "field 'lvLeft'");
    target.lvLeft = finder.castView(view, 2131625237, "field 'lvLeft'");
    view = finder.findRequiredView(source, 2131625238, "field 'lvRight'");
    target.lvRight = finder.castView(view, 2131625238, "field 'lvRight'");
    view = finder.findRequiredView(source, 2131625225, "field 'llHeadLayout'");
    target.llHeadLayout = finder.castView(view, 2131625225, "field 'llHeadLayout'");
    view = finder.findRequiredView(source, 2131625236, "field 'llContentListView'");
    target.llContentListView = finder.castView(view, 2131625236, "field 'llContentListView'");
    view = finder.findRequiredView(source, 2131625235, "field 'viewMaskBg'");
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
