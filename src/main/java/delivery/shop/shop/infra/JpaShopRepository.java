package delivery.shop.shop.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.shop.application.dto.response.QShopSimpleInfo;
import delivery.shop.shop.application.dto.response.ShopSimpleInfo;
import delivery.shop.shop.domain.Shop;
import delivery.shop.shop.domain.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static delivery.shop.file.domain.QFile.file;
import static delivery.shop.shop.domain.QShop.shop;

@RequiredArgsConstructor
@Repository
public class JpaShopRepository implements ShopRepository {
    private final ShopDao shopDao;
    private final JPAQueryFactory queryFactory;

    public Optional<ShopSimpleInfo> findSimpleInfo(long shopId){
        return Optional.ofNullable(
                queryFactory.select(new QShopSimpleInfo(shop.shopName, shop.minOrderPrice.value, file.filePath))
                        .from(shop)
                        .leftJoin(shop.shopThumbnail, file)
                        .where(shop.id.eq(shopId))
                        .fetchOne()
        );
    }

    @Override
    public Shop save(Shop shop) {
        return shopDao.save(shop);
    }

}
