package easyrun.shopping.model;

import java.util.Comparator;

/**
 * Created by sunfusheng on 16/4/25.
 */
public class ClothesEntityComparator implements Comparator<ClothesEntity> {

    @Override
    public int compare(ClothesEntity lhs, ClothesEntity rhs) {
        return Integer.parseInt(lhs.getSaleNumber()) - Integer.parseInt(rhs.getSaleNumber());
    }
}
