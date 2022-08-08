package delivery.shop.shop.application.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<Integer> defaultDeliveryFees;


    @QueryProjection
    public ShopSimpleInfo(long shopId, String shopName, int minOrderAmount, String thumbnail, List<Integer> defaultDeliveryFees) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.thumbnail = thumbnail;
        this.defaultDeliveryFees = defaultDeliveryFees;
    }

    @QueryProjection
    public ShopSimpleInfo(long shopId, String shopName, int minOrderAmount, String thumbnail) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.thumbnail = thumbnail;
    }

    public void setDefaultDeliveryFees(List<Integer> defaultDeliveryFees) {
        this.defaultDeliveryFees = defaultDeliveryFees;
    }
}
