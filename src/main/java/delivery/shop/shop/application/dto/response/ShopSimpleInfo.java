package delivery.shop.shop.application.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShopSimpleInfo {
    private String shopName;
    private int minOrderPrice;
    private String thumbnail;

    @QueryProjection
    public ShopSimpleInfo(String shopName, int minOrderPrice, String thumbnail) {
        this.shopName = shopName;
        this.minOrderPrice = minOrderPrice;
        this.thumbnail = thumbnail;
    }
}
