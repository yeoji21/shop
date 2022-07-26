package delivery.shop.shop.domain;

import delivery.shop.shop.application.dto.response.ShopSimpleInfo;

import java.util.Optional;

public interface ShopRepository{
    Optional<ShopSimpleInfo> findSimpleInfo(long shopId);
    Shop save(Shop shop);
}
