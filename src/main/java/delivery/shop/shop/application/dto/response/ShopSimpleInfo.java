package delivery.shop.shop.application.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import delivery.shop.common.domain.Money;
import delivery.shop.shop.domain.Shop;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/*
    가게 간략 정보 :
    가게 ID, 가게 이름, 최소 주문 금액, 가게 썸네일 파일 저장 경로, 기본 배달료
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopSimpleInfo {
    private long shopId;
    private String shopName;
    private int minOrderAmount;
    private String thumbnail;
    private List<Integer> defaultDeliveryFees;

    @QueryProjection
    public ShopSimpleInfo(Shop shop, String thumbnail) {
        this.shopId = shop.getId();
        this.shopName = shop.getShopName();
        this.minOrderAmount = shop.getMinOrderAmount().toInt();
        this.thumbnail = thumbnail;
        this.defaultDeliveryFees = shop.getDefaultDeliveryFees()
                .stream()
                .map(Money::toInt)
                .collect(Collectors.toList());
    }
}
