package delivery.shop.shop.infra;

import delivery.shop.shop.domain.Shop;
import delivery.shop.shop.domain.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JpaShopRepository implements ShopRepository {
    private final ShopDao shopDao;

    @Override
    public Shop save(Shop shop) {
        return shopDao.save(shop);
    }

}
