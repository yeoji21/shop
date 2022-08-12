package delivery.shop.product.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.common.config.JpaQueryFactoryConfig;
import delivery.shop.common.domain.Money;
import delivery.shop.shop.infra.JpaShopRepository;
import delivery.shop.shop.infra.ShopQueryDao;
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

@Import({JpaQueryFactoryConfig.class, JpaShopRepository.class, ShopQueryDao.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ProductRepositoryTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;

    @Test @Rollback(value = false)
    void 상품_저장_테스트() throws Exception{
        Product product = new Product("반반 치킨", new Money(15_000));
        em.persist(product);

        ItemGroup itemGroup = new ItemGroup("양념 선택", 2, 2);
        em.persist(itemGroup);

        em.persist(new ProductItemGroup(product, itemGroup));

        Item item1 = new Item("후라이드", new Money(0));
        Item item2 = new Item("양념", new Money(500));
        Item item3 = new Item("간장", new Money(1000));

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);

        em.persist(new ItemGroupItem(itemGroup, item1));
        em.persist(new ItemGroupItem(itemGroup, item2));
        em.persist(new ItemGroupItem(itemGroup, item3));
    }
}