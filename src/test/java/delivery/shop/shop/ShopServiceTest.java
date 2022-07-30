package delivery.shop.shop;

import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.common.domain.Money;
import delivery.shop.file.domain.File;
import delivery.shop.file.domain.FileName;
import delivery.shop.shop.domain.OrderAmountDeliveryFee;
import delivery.shop.shop.domain.Shop;
import delivery.shop.shop.domain.ShopLocation;
import delivery.shop.shop.domain.ShopRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    void DeliveryFee_변경_update_쿼리_나감() throws Exception{
        Shop newShop = Shop.builder()
                .shopName("shop")
                .minOrderAmount(new Money(15_000))
                .location(new ShopLocation("xxxx", 1.0, 2.0))
                .build();

        newShop.addDeliveryFee(new OrderAmountDeliveryFee(new Money(15_000), new Money(3000)));
        newShop.addDeliveryFee(new OrderAmountDeliveryFee(new Money(20_000), new Money(1000)));
        newShop.addDeliveryFee(new OrderAmountDeliveryFee(new Money(25_000), new Money(0)));

        Shop savedShop = shopRepository.save(newShop);

//        savedShop.getDefaultDeliveryFees().forEach(d -> System.out.println(d.getFee()));
//
//        savedShop.getDefaultDeliveryFees().get(0).setFee(new Money(1));
//        clear();
//
//        savedShop.getDefaultDeliveryFees().forEach(d -> System.out.println(d.getFee()));
    }

    @Test @Transactional
    void DeliveryFee_변경_delete_쿼리_나감() throws Exception{
        Shop newShop = Shop.builder()
                .shopName("shop")
                .minOrderAmount(new Money(15_000))
                .location(new ShopLocation("xxxx", 1.0, 2.0))
                .build();

        newShop.addDeliveryFee(new OrderAmountDeliveryFee(new Money(15_000), new Money(3000)));
        newShop.addDeliveryFee(new OrderAmountDeliveryFee(new Money(20_000), new Money(1000)));
        newShop.addDeliveryFee(new OrderAmountDeliveryFee(new Money(25_000), new Money(0)));

        Shop savedShop = shopRepository.save(newShop);

//        savedShop.getDefaultDeliveryFees().remove(0);

        clear();
    }

    @Test @Transactional
    void 가게_사진과_함께_저장() throws Exception{
        File image = new File(new FileName("original", "stored"), "xxx");
        Shop newShop = Shop.builder()
                .shopName("shop")
                .minOrderAmount(new Money(15_000))
                .location(new ShopLocation("xxxx", 1.0, 2.0))
                .shopThumbnailFileId(image.getId())
                .build();
        em.persist(image);
        shopRepository.save(newShop);
    }


    private void clear() {
        em.flush();
        em.clear();
    }
}