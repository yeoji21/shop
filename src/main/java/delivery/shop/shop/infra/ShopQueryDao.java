package delivery.shop.shop.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.shop.application.dto.response.QShopDetailInfo;
import delivery.shop.shop.application.dto.response.QShopSimpleInfo;
import delivery.shop.shop.application.dto.response.ShopDetailInfo;
import delivery.shop.shop.application.dto.response.ShopSimpleInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static delivery.shop.file.domain.QFile.file;
import static delivery.shop.shop.domain.QShop.shop;

@RequiredArgsConstructor
@Repository
public class ShopQueryDao {
    private final JPAQueryFactory queryFactory;

    public Optional<ShopSimpleInfo> findSimpleInfo(long shopId){
        return Optional.ofNullable(
                queryFactory.select(new QShopSimpleInfo(shop, file.filePath))
                        .from(shop)
                        .leftJoin(file).on(shop.shopThumbnailFileId.eq(file.id))
                        .where(shop.id.eq(shopId))
                        .fetchOne()
        );
    }

    public List<ShopSimpleInfo> findAllSimpleInfo() {
        return queryFactory.select(new QShopSimpleInfo(shop, file.filePath))
                .from(shop)
                .leftJoin(file).on(shop.shopThumbnailFileId.eq(file.id))
                .fetch();
    }

    public Optional<ShopDetailInfo> findDetailInfo(long shopId) {
        return Optional.ofNullable(
                queryFactory.select(new QShopDetailInfo(shop))
                        .from(shop)
                        .where(shop.id.eq(shopId))
                        .fetchOne()
        );
    }
}
