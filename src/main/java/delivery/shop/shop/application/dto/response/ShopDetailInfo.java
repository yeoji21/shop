package delivery.shop.shop.application.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import delivery.shop.shop.domain.Shop;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
    가게 상세 정보 :
    가게 ID, 가게 이름, 최소 주문 금액, 소개글, 전화번호, 영업 시간, 휴무일, 도로명 주소
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopDetailInfo {
    private long shopId;
    private String shopName;
    private int minOrderAmount;
    private String introduction;
    private String phoneNumber;
    private String openingHours;
    private String dayOff;
    private String streetAddress;

    @QueryProjection
    public ShopDetailInfo(Shop shop) {
        this.shopId = shop.getId();
        this.shopName = shop.getName();
        this.minOrderAmount = shop.getMinOrderAmount().toInt();
        this.introduction = shop.getIntroduction();
        this.phoneNumber = shop.getPhoneNumber().toString();
        this.openingHours = shop.getBusinessTimeInfo().getOpeningHours();
        this.dayOff = shop.getBusinessTimeInfo().getDayOff();
        this.streetAddress = shop.getLocation().getStreetAddress();
    }
}
