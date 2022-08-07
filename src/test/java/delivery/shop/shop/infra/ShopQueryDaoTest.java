package delivery.shop.shop.infra;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.common.config.JpaQueryFactoryConfig;
import delivery.shop.common.domain.Money;
import delivery.shop.shop.application.dto.response.QShopSimpleInfo;
import delivery.shop.shop.application.dto.response.ShopSimpleInfo;
import delivery.shop.shop.domain.BusinessTimeInfo;
import delivery.shop.shop.domain.OrderAmountDeliveryFee;
import delivery.shop.shop.domain.PhoneNumber;
import delivery.shop.shop.domain.Shop;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.*;
import static delivery.shop.file.domain.QFile.file;
import static delivery.shop.shop.domain.QCategoryShop.categoryShop;
import static delivery.shop.shop.domain.QOrderAmountDeliveryFee.orderAmountDeliveryFee;
import static delivery.shop.shop.domain.QShop.shop;

@Import({JpaQueryFactoryConfig.class, ShopQueryDao.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ShopQueryDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;


    @Test @Rollback(value = false)
    void test() throws Exception{
        for (int i = 0; i < 50; i++) {
            Shop shop = Shop.builder()
                    .name(i + " shop")
                    .minOrderAmount(new Money(10_000))
                    .phoneNumber(new PhoneNumber("010-1234-5678"))
                    .businessTimeInfo(new BusinessTimeInfo("매일 15시 ~ 02시", "연중무휴"))
                    .categoryId((long) i % 2)
                    .build();
            em.persist(shop);

            OrderAmountDeliveryFee first = OrderAmountDeliveryFee.builder()
                    .shop(shop)
                    .orderAmount(new Money(i * 1000))
                    .fee(new Money(i * 100))
                    .build();
            em.persist(first);

            OrderAmountDeliveryFee second = OrderAmountDeliveryFee.builder()
                    .shop(shop)
                    .orderAmount(new Money(i * 1000))
                    .fee(new Money(i * 500))
                    .build();
            em.persist(second);
        }
        em.flush();
        em.clear();
    }

    @Test
    void keep() throws Exception{
        List<Tuple> fetch = queryFactory
                .select(orderAmountDeliveryFee.shop, orderAmountDeliveryFee.fee.value.min())
                .from(orderAmountDeliveryFee)
                .groupBy(orderAmountDeliveryFee.shop.id)
                .orderBy(orderAmountDeliveryFee.fee.value.min().asc())
                .fetch();

        for (int i = 0; i < fetch.size(); i++) {
            System.out.println(fetch.get(i).get(orderAmountDeliveryFee.shop).getName());
        }

        queryFactory
                .select(orderAmountDeliveryFee.shop, orderAmountDeliveryFee.fee.value.min())
                .from(orderAmountDeliveryFee)
                .join(categoryShop).on(categoryShop.shop.eq(orderAmountDeliveryFee.shop))
                .where(categoryShop.categoryId.eq(1L))
                .groupBy(orderAmountDeliveryFee.shop.id)
                .orderBy(orderAmountDeliveryFee.fee.value.min().asc())
                .fetch();

        for (int i = 0; i < fetch.size(); i++) {
            System.out.println(fetch.get(i).get(orderAmountDeliveryFee.shop.id) + " - > " +
                    fetch.get(i).get(orderAmountDeliveryFee.fee.value.min()));
        }
    }

    @Test
    void select() throws Exception{
        List<Long> shopIds = queryFactory
                .select(orderAmountDeliveryFee.shop.id, orderAmountDeliveryFee.fee.value.min())
                .from(orderAmountDeliveryFee)
                .join(categoryShop).on(categoryShop.shop.eq(orderAmountDeliveryFee.shop))
                .groupBy(orderAmountDeliveryFee.shop.id)
                .having(
                        orderAmountDeliveryFee.fee.value.min().gt(2000)
                        .or(
                                orderAmountDeliveryFee.fee.value.min().eq(2000).and(orderAmountDeliveryFee.shop.id.gt(19L))
                        )
                )
                .where(categoryShop.categoryId.eq(1L))
                .orderBy(orderAmountDeliveryFee.fee.value.min().asc())
                .limit(10)
                .fetch()
                .stream().map(t -> t.get(orderAmountDeliveryFee.shop.id))
                .collect(Collectors.toList());

        shopIds.forEach(System.out::println);

        List<ShopSimpleInfo> infoList = queryFactory
                .from(shop)
                .innerJoin(categoryShop).on(categoryShop.shop.eq(shop))
                .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
                .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
                .where(shop.id.in(shopIds))
                .orderBy(shop.id.desc())
                .transform(
                        groupBy(shop).list(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
                                list(orderAmountDeliveryFee.fee.value)))
                );

        infoList.forEach(shop -> {
            System.out.println(shop.getShopName());
            shop.getDefaultDeliveryFees().forEach(m -> System.out.print(m + " "));
            System.out.println();
            System.out.println("========================");
        });

    }

    private BooleanExpression isShopIdLt(Long cursorId) {
        return cursorId != null ? shop.id.lt(cursorId) : null;
    }
}
