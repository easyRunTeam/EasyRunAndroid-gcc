// Generated code from Butter Knife. Do not modify!
package easyrun.shopping.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FilterView$$ViewBinder<T extends easyrun.shopping.view.FilterView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131625218, "field 'tvCategory'");
    target.tvCategory = finder.castView(view, 2131625218, "field 'tvCategory'");
    view = finder.findRequiredView(source, 2131625219, "field 'ivCategoryArrow'");
    target.ivCategoryArrow = finder.castView(view, 2131625219, "field 'ivCategoryArrow'");
    view = finder.findRequiredView(source, 2131625221, "field 'tvSort'");
    target.tvSort = finder.castView(view, 2131625221, "field 'tvSort'");
    view = finder.findRequiredView(source, 2131625222, "field 'ivSortArrow'");
    target.ivSortArrow = finder.castView(view, 2131625222, "field 'ivSortArrow'");
    view = finder.findRequiredView(source, 2131625224, "field 'tvFilter'");
    target.tvFilter = finder.castView(view, 2131625224, "field 'tvFilter'");
    view = finder.findRequiredView(source, 2131625225, "field 'ivFilterArrow'");
    target.ivFilterArrow = finder.castView(view, 2131625225, "field 'ivFilterArrow'");
    view = finder.findRequiredView(source, 2131625217, "field 'llCategory'");
    target.llCategory = finder.castView(view, 2131625217, "field 'llCategory'");
    view = finder.findRequiredView(source, 2131625220, "field 'llSort'");
    target.llSort = finder.castView(view, 2131625220, "field 'llSort'");
    view = finder.findRequiredView(source, 2131625223, "field 'llFilter'");
    target.llFilter = finder.castView(view, 2131625223, "field 'llFilter'");
    view = finder.findRequiredView(source, 2131625228, "field 'lvLeft'");
    target.lvLeft = finder.castView(view, 2131625228, "field 'lvLeft'");
    view = finder.findRequiredView(source, 2131625229, "field 'lvRight'");
    target.lvRight = finder.castView(view, 2131625229, "field 'lvRight'");
    view = finder.findRequiredView(source, 2131625216, "field 'llHeadLayout'");
    target.llHeadLayout = finder.castView(view, 2131625216, "field 'llHeadLayout'");
    view = finder.findRequiredView(source, 2131625227, "field 'llContentListView'");
    target.llContentListView = finder.castView(view, 2131625227, "field 'llContentListView'");
    view = finder.findRequiredView(source, 2131625226, "field 'viewMaskBg'");
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
