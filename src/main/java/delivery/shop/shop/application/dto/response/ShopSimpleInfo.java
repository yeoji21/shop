package delivery.shop.shop.application.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import delivery.shop.shop.domain.Shop;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    가게 간략 정보 :
    가게 ID, 가게 이름, 최소 주문 금액, 가게 썸네일 파일 저장 경로, 기본 배달료
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopSimpleInfo {
    private long shopId;
    private String shopName;
    private int minOrderAmount;
    private String thumbnail;
    private int deliveryFee;

    @QueryProjection
    public ShopSimpleInfo(Shop shop, String thumbnail) {
        this.shopId = shop.getId();
        this.shopName = shop.getShopName();
        this.minOrderAmount = shop.getMinOrderAmount().toInt();
        this.thumbnail = thumbnail;
        // getDefaultDeliveryFees 내부로 이동
        this.deliveryFee = shop.getDefaultDeliveryFees()
                .getDeliveryFees()
                .get(0)
                .getFee()
                .toInt();
    }
}
