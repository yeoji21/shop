package delivery.shop.shop.application.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import delivery.shop.shop.domain.DefaultDeliveryFees;
import delivery.shop.shop.domain.Shop;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShopSimpleInfo {
    private String shopName;
    private int minOrderAmount;
    private String thumbnail;
    private int deliveryFee;

    @QueryProjection
    public ShopSimpleInfo(String shopName, int minOrderAmount, String thumbnail, int deliveryFee) {
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.thumbnail = thumbnail;
        this.deliveryFee = deliveryFee;
    }

    @QueryProjection
    public ShopSimpleInfo(String shopName, int minOrderAmount, String thumbnail, DefaultDeliveryFees deliveryFees) {
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.thumbnail = thumbnail;
        this.deliveryFee = deliveryFees.getDeliveryFees().get(0).getFee().toInt();
    }

    @QueryProjection
    public ShopSimpleInfo(Shop shop, String thumbnail) {
        this.shopName = shop.getShopName();
        this.minOrderAmount = shop.getMinOrderAmount().toInt();
        this.thumbnail = thumbnail;
        this.deliveryFee = shop.getDefaultDeliveryFees()
                .getDeliveryFees()
                .get(0)
                .getFee()
                .toInt();
    }
}
