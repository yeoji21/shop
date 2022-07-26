package delivery.shop.shop;

import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.common.domain.Money;
import delivery.shop.file.domain.File;
import delivery.shop.file.domain.FileName;
import delivery.shop.shop.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static delivery.shop.shop.domain.QShop.shop;

//@ExtendWith(SpringExtension.class)
//@Import(JpaQueryFactoryConfig.class)
//@DataJpaTest
@SpringBootTest
class ShopServiceTest {
    @Autowired private ShopRepository shopRepository;
    @Autowired
    private JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager em;

    @Test @Transactional
    void Shop_이름_변경_update_쿼리_나감() throws Exception{

        Shop newShop = Shop.builder()
                .shopName("shop")
                .minOrderPrice(new Money(15_000))
                .location(new ShopLocation("xxxx", 1.0, 2.0))
                .build();

        newShop.addDeliveryFee(new DeliveryFee(new Money(15_000), new Money(3000)));
        newShop.addDeliveryFee(new DeliveryFee(new Money(20_000), new Money(1000)));
        newShop.addDeliveryFee(new DeliveryFee(new Money(25_000), new Money(0)));

        Shop savedShop = shopRepository.save(newShop);

        Shop findShop = queryFactory.selectFrom(shop)
                .where(shop.id.eq(savedShop.getId()))
                .fetchOne();

        findShop.setShopName("test");

        clear();

        Shop updatedShop = queryFactory.selectFrom(shop)
                .where(shop.id.eq(savedShop.getId()))
                .fetchOne();

        System.out.println(updatedShop.getShopName());
    }

    @Test @Transactional
    void DeliveryFee_변경_update_쿼리_나감() throws Exception{
        Shop newShop = Shop.builder()
                .shopName("shop")
                .minOrderPrice(new Money(15_000))
                .location(new ShopLocation("xxxx", 1.0, 2.0))
                .build();

        newShop.addDeliveryFee(new DeliveryFee(new Money(15_000), new Money(3000)));
        newShop.addDeliveryFee(new DeliveryFee(new Money(20_000), new Money(1000)));
        newShop.addDeliveryFee(new DeliveryFee(new Money(25_000), new Money(0)));

        Shop savedShop = shopRepository.save(newShop);

        savedShop.getDeliveryFees().forEach(d -> System.out.println(d.getFee()));

        savedShop.getDeliveryFees().get(0).setFee(new Money(1));
        clear();

        savedShop.getDeliveryFees().forEach(d -> System.out.println(d.getFee()));
    }

    @Test @Transactional
    void DeliveryFee_변경_delete_쿼리_나감() throws Exception{
        Shop newShop = Shop.builder()
                .shopName("shop")
                .minOrderPrice(new Money(15_000))
                .location(new ShopLocation("xxxx", 1.0, 2.0))
                .build();

        newShop.addDeliveryFee(new DeliveryFee(new Money(15_000), new Money(3000)));
        newShop.addDeliveryFee(new DeliveryFee(new Money(20_000), new Money(1000)));
        newShop.addDeliveryFee(new DeliveryFee(new Money(25_000), new Money(0)));

        Shop savedShop = shopRepository.save(newShop);

        savedShop.getDeliveryFees().remove(0);

        clear();
    }

    @Test @Transactional
    void 가게_사진과_함께_저장() throws Exception{
        File image = new File(new FileName("original", "stored"), "xxx");
        Shop newShop = Shop.builder()
                .shopName("shop")
                .minOrderPrice(new Money(15_000))
                .location(new ShopLocation("xxxx", 1.0, 2.0))
                .shopThumbnail(image)
                .build();
        em.persist(image);
        shopRepository.save(newShop);
    }


    private void clear() {
        em.flush();
        em.clear();
    }
}