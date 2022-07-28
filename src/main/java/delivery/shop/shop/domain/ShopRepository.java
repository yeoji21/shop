package delivery.shop.shop.domain;

import delivery.shop.shop.application.dto.response.ShopSimpleInfo;

import java.util.List;
import java.util.Optional;

public interface ShopRepository{
    List<ShopSimpleInfo> findAllSimpleInfo();
    Optional<ShopSimpleInfo> findSimpleInfo(long shopId);
    Shop save(Shop shop);
}
