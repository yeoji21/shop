package delivery.shop.shop.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.common.config.JpaQueryFactoryConfig;
import delivery.shop.common.domain.Money;
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
        for (int i = 0; i < 10; i++) {
            Shop shop = Shop.builder()
                    .name(i + " shop")
                    .minOrderAmount(new Money(10_000))
                    .phoneNumber(new PhoneNumber("010-1234-5678"))
                    .businessTimeInfo(new BusinessTimeInfo("매일 15시 ~ 02시", "연중무휴"))
                    .categoryId((long) i % 2)
                    .build();
            em.persist(shop);

            OrderAmountDeliveryFee deliveryFee = OrderAmountDeliveryFee.builder()
                    .shop(shop)
                    .orderAmount(new Money(i * 1000))
                    .fee(new Money(1000))
                    .build();
            em.persist(deliveryFee);
        }
        em.flush();
        em.clear();
    }



    @Test
    void select() throws Exception{
//        List<ShopSimpleInfo> result = queryFactory
//                .from(shop)
//                .where(shop.categoryIds.contains(1L))
//                .leftJoin(file).on(file.id.eq(shop.shopThumbnailFileId))
//                .leftJoin(orderAmountDeliveryFee).on(orderAmountDeliveryFee.shop.eq(shop))
//                .transform(
//                        groupBy(shop).list(new QShopSimpleInfo(shop.id, shop.name, shop.minOrderAmount.value, file.filePath,
//                                list(orderAmountDeliveryFee.fee.value)))
//                );
//
//        result.forEach(shop -> System.out.println(shop.getShopName()));
    }
}
