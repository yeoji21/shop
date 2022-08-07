package delivery.shop.shop.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.shop.application.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static delivery.shop.file.domain.QFile.file;
import static delivery.shop.shop.domain.QCategoryShop.categoryShop;
import static delivery.shop.shop.domain.QOrderAmountDeliveryFee.orderAmountDeliveryFee;
import static delivery.shop.shop.domain.QShop.shop;

@RequiredArgsConstructor
@Repository
public class ShopQueryDao {
    private final JPAQueryFactory queryFactory;

    public Optional<ShopSimpleInfo> findSimpleInfo(long shopId){
        return Optional.ofNullable(
                queryFactory
                        .from(shop)
                        .where(shop.id.eq(shopId))
                        .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                        .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                        .transform(
                                groupBy(shop.id).as(
                                        new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
                                                list(orderAmountDeliveryFee.fee.value))
                                )
                        ).get(shopId)
        );
    }

    public Optional<ShopDetailInfo> findDetailInfo(long shopId) {
        return Optional.ofNullable(
                queryFactory.select(new QShopDetailInfo(shop))
                        .from(shop)
                        .where(shop.id.eq(shopId))
                        .fetchOne()
        );
    }

    public ShopListQueryResult findShopListByCategory(long categoryId) {
        List<ShopSimpleInfo> shopSimpleInfoList = queryFactory
                .from(shop)
                .innerJoin(categoryShop).on(categoryShop.shop.eq(shop))
                .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                .where(categoryShop.categoryId.eq(categoryId))
                .transform(
                        groupBy(shop).list(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
                                list(orderAmountDeliveryFee.fee.value)))
                );

        return new ShopListQueryResult(shopSimpleInfoList);
    }
}
