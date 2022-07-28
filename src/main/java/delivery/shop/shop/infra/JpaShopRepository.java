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
        // 1. 기본 배달비별로 조회한 후 orderBy, limit으로 하나만 출력
//        ShopSimpleInfo info = queryFactory.select(
//                        new QShopSimpleInfo(shop.shopName, shop.minOrderAmount.value,
//                                file.filePath, orderAmountDeliveryFee.fee.value))
//                .from(shop)
//                .join(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.id.eq(shopId))
//                .leftJoin(file).on(shop.shopThumbnailFileId.eq(file.id))
//                .where(shop.id.eq(shopId))
//                .orderBy(orderAmountDeliveryFee.orderAmount.value.asc())
//                .limit(1)
//                .fetchOne();

        // 2. DTO 생성자에서 매핑 -> 1+1 쿼리 발생
        ShopSimpleInfo info =
                queryFactory.select(new QShopSimpleInfo(shop, file.filePath))
                        .from(shop)
                        .leftJoin(file).on(shop.shopThumbnailFileId.eq(file.id))
                        .where(shop.id.eq(shopId))
                        .fetchOne();

        return Optional.ofNullable(info);
    }


    @Override
    public Shop save(Shop shop) {
        return shopDao.save(shop);
    }

}
