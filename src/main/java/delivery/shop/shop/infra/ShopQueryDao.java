package delivery.shop.shop.infra;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.shop.application.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 카테고리별 가게 목록 (간략 정보) -> 배달비 낮은 순 (가게별 기본 배달비 중 가장 낮은 배달비를 기준으로 정렬)
    public ShopListQueryResult findByCategoryOrderByDeliveryFee(long categoryId, String cursor, int size) {
        List<Long> shopIds = queryFactory
                .select(orderAmountDeliveryFee.shop.id, orderAmountDeliveryFee.fee.value.min())
                .from(orderAmountDeliveryFee)
                .join(categoryShop).on(categoryShop.shop.eq(orderAmountDeliveryFee.shop))
                .groupBy(orderAmountDeliveryFee.shop)
                .having(deliveryFeeCursorCondition(cursor))
                .where(categoryShop.categoryId.eq(categoryId))
                .orderBy(orderAmountDeliveryFee.fee.value.min().asc())
                .limit(size + 1)
                .fetch()
                .stream()
                .map(t -> t.get(orderAmountDeliveryFee.shop.id))
                .collect(Collectors.toList());

        List<ShopSimpleInfo> infoList = queryFactory
                .from(shop)
                .innerJoin(categoryShop).on(categoryShop.shop.eq(shop))
                .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                .where(shop.id.in(shopIds))
                .transform(
                        groupBy(shop).list(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
                                list(orderAmountDeliveryFee.fee.value)))
                );


        System.out.println("=============================before delete=================================");
        infoList.sort(Comparator.comparingLong(info -> shopIds.indexOf(info.getShopId())));
        infoList.forEach(s -> System.out.println(s.getShopName()));
        System.out.println("=============================before delete=================================");


        boolean hasNext = hasNext(size, infoList);

        ShopSimpleInfo lastOne = infoList.get(infoList.size() - 1);

        Integer lastFee = lastOne.getDefaultDeliveryFees()
                .stream()
                .min(Comparator.comparing((Integer fee) -> fee))
                .get();

        long lastShopId = lastOne.getShopId();
        String nextCursor = String.format("%010d", lastFee).concat(String.format("%010d", lastShopId));

        return new ShopListQueryResult(infoList, hasNext, nextCursor);
    }

    private boolean hasNext(int size, List<ShopSimpleInfo> infoList) {
        boolean hasNext = false;
        if (infoList.size() > size) {
            infoList.remove(size);
            hasNext = true;
        }
        return hasNext;
    }

    private BooleanExpression deliveryFeeCursorCondition(String cursor) {
        if(cursor == null || cursor.length() < 20) return null;

        return StringExpressions.lpad(orderAmountDeliveryFee.fee.value.min().stringValue(), 10, '0')
                .concat(StringExpressions.lpad(orderAmountDeliveryFee.shop.id.stringValue(), 10, '0'))
                .gt(cursor);
    }
}
