package delivery.shop.shop.application.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ShopListQueryResult {
    private List<ShopSimpleInfo> shopList;
    private boolean hasNext;
    private String nextCursor;

    public ShopListQueryResult(List<ShopSimpleInfo> shopList, boolean hasNext, String nextCursor) {
        this.shopList = shopList;
        this.hasNext = hasNext;
        this.nextCursor = nextCursor;
    }
}
