package delivery.shop.common.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DisplayInfo {
    @Column(name = "is_display")
    private boolean isDisplay;

    @Column(name ="display_order")
    private int displayOrder;

    public DisplayInfo(int displayOrder) {
        this.displayOrder = displayOrder;
        this.isDisplay = true;
    }

//    Query query = entityManager
//            .createNativeQuery("select s.shop_id from category_shop cs join shop s on cs.shop_id = s.shop_id " +
//                    "where cs.category_id = ?")
//            .setParameter(1, categoryId);
//
//    List<Long> shopIds = new ArrayList<>();
//        for (Object o : query.getResultList()) {
//        long id = ((Number) o).longValue();
//        System.out.println(id);
//        shopIds.add(id);
//    }
//
//    List<ShopSimpleInfo> shopSimpleInfoList = queryFactory
//            .from(shop)
//            .where(shop.id.in(shopIds))
//            .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
//            .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
//            .transform(
//                    groupBy(shop).list(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
//                            list(orderAmountDeliveryFee.fee.value)))
//            );
}
