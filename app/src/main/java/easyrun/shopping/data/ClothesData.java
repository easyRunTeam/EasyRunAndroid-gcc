package easyrun.shopping.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import easyrun.shopping.model.ChannelEntity;
import easyrun.shopping.model.ClothesEntity;
import easyrun.shopping.model.FilterEntity;
import easyrun.shopping.model.FilterTwoEntity;
import easyrun.shopping.model.OperationEntity;
import easyrun.shopping.model.ClothesEntityComparator;

/**
 * 商城数据
 *
 * Created by gecongcong
 */
public class ClothesData {

    public static final String type_woman = "女装";
    public static final String type_man = "男装";
    public static final String type_child = "童装";
    public static final String type_international = "国际大牌";
    public static final String type_sale = "精品特卖";
    public static final String type_suggest = "达人推荐";

    // 广告数据
    public static List<String> getAdData() {
        List<String> adList = new ArrayList<>();
        adList.add("http://120.27.106.188:8088/Shopping/advertise/scroll_default.png");
        adList.add("http://120.27.106.188:8088/Shopping/advertise/scroll_three.png");
        adList.add("http://120.27.106.188:8088/Shopping/advertise/scroll_four.png");
        return adList;
    }

    // 频道数据
    public static List<ChannelEntity> getChannelData() {
        List<ChannelEntity> channelList = new ArrayList<>();
        channelList.add(new ChannelEntity("女装", "女装", "http://120.27.106.188:8088/Shopping/womanLogo/woman_clothes_logo.jpg"));
        channelList.add(new ChannelEntity("男装", "男装", "http://120.27.106.188:8088/Shopping/manLogo/man_clothes_logo.jpg"));
        channelList.add(new ChannelEntity("童装", "童装", "http://120.27.106.188:8088/Shopping/childLogo/child_clothes_logo.png"));
        channelList.add(new ChannelEntity("国际大牌", "国际", "http://120.27.106.188:8088/Shopping/internationalLogo/internationalLogo.jpg"));
        channelList.add(new ChannelEntity("精品特卖", "特卖", "http://120.27.106.188:8088/Shopping/jingpinLogo/temai.png"));
        channelList.add(new ChannelEntity("达人推荐", "达人推荐", "http://120.27.106.188:8088/Shopping/suggestLogo/suggestLogo.png"));
        return channelList;
    }

    // 运营数据
    public static List<OperationEntity> getOperationData() {
        List<OperationEntity> operationList = new ArrayList<>();
        operationList.add(new OperationEntity("Adidas", "阿迪达斯", "http://120.27.106.188:8088/Shopping/yunyingLogo/adidas.png"));
        operationList.add(new OperationEntity("Nike", "耐克", "http://120.27.106.188:8088/Shopping/yunyingLogo/nike.jpg"));
        return operationList;
    }

    // ListView数据
    public static List<ClothesEntity> getTravelingData(List<ClothesEntity> clothesEntityList) {
        /*travelingList.add(new ClothesEntity(type_woman, "", "西班牙", 17, "http://img4.imgtn.bdimg.com/it/u=620137884,621556624&fm=21&gp=0.jpg"));
        travelingList.add(new ClothesEntity(type_man, "", "意大利", 22, "http://img0.imgtn.bdimg.com/it/u=3631118072,1530723002&fm=206&gp=0.jpg"));*/
        return clothesEntityList;
    }

    // 分类数据
    public static List<FilterTwoEntity> getCategoryData() {
        List<FilterTwoEntity> list = new ArrayList<>();
        list.add(new FilterTwoEntity(type_woman, getFilterData()));
        list.add(new FilterTwoEntity(type_man, getFilterData()));
        list.add(new FilterTwoEntity(type_child, getFilterData()));
        list.add(new FilterTwoEntity(type_international, getFilterData()));
        list.add(new FilterTwoEntity(type_sale, getFilterData()));
        list.add(new FilterTwoEntity(type_suggest, getFilterData()));
        return list;
    }

    // 排序数据
    public static List<FilterEntity> getSortData() {
        List<FilterEntity> list = new ArrayList<>();
        list.add(new FilterEntity("从高到低", "1"));
        list.add(new FilterEntity("从低到高", "2"));
        return list;
    }

    // 筛选数据
    public static List<FilterEntity> getFilterData() {
        List<FilterEntity> list = new ArrayList<>();
        list.add(new FilterEntity("PEACEBIRD", "1"));
        list.add(new FilterEntity("男装", "2"));
        list.add(new FilterEntity("童装", "3"));
        list.add(new FilterEntity("国际大牌", "4"));
        list.add(new FilterEntity("精品特卖", "5"));
        list.add(new FilterEntity("达人推荐", "6"));
        return list;
    }

    // ListView分类数据
    public static List<ClothesEntity> getCategoryData(FilterTwoEntity entity,List<ClothesEntity> list) {
        List<ClothesEntity> newList = new ArrayList<>();
        int size = list.size();
        for (int i=0; i<size; i++) {
            if (list.get(i).getType().equals(entity.getType()) &&
                    list.get(i).getBrand().equals(entity.getSelectedFilterEntity().getKey())) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    // ListView排序数据
    public static List<ClothesEntity> getSortData(FilterEntity entity,List<ClothesEntity> list) {
        Comparator<ClothesEntity> ascComparator = new ClothesEntityComparator();
        if (entity.getKey().equals("从高到低")) {
            Collections.sort(list);
        } else {
            Collections.sort(list, ascComparator);
        }
        return list;
    }

    // ListView筛选数据
    public static List<ClothesEntity> getFilterData(FilterEntity entity,List<ClothesEntity> list) {
        List<ClothesEntity> travelingList = new ArrayList<>();
        int size = list.size();
        for (int i=0; i<size; i++) {
            if (list.get(i).getBrand().equals(entity.getKey())) {
                travelingList.add(list.get(i));
            }
        }
        return travelingList;
    }

    // 暂无数据
    public static List<ClothesEntity> getNoDataEntity(int height) {
        List<ClothesEntity> list = new ArrayList<>();
        ClothesEntity entity = new ClothesEntity();
        entity.setNoData(true);
        entity.setHeight(height);
        list.add(entity);
        return list;
    }

}
