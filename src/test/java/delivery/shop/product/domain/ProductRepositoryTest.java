package delivery.shop.product.domain;

import com.querydsl.core.group.GroupBy;
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
import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static delivery.shop.product.domain.QItem.item;
import static delivery.shop.product.domain.QItemGroup.itemGroup;
import static delivery.shop.product.domain.QItemGroupItem.itemGroupItem;
import static delivery.shop.product.domain.QProductItemGroup.productItemGroup;

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
        Product 반반_치킨 = new Product("반반 치킨", new Money(15_000));
        em.persist(반반_치킨);

        ItemGroup 양념_선택 = new ItemGroup("양념 선택", 2, 2);
        em.persist(양념_선택);

//        ItemGroup 양념_선택2 = new ItemGroup("양념 선택2", 2, 2);
//        em.persist(양념_선택2);
//        em.persist(new ProductItemGroup(반반_치킨, 양념_선택2));

        em.persist(new ProductItemGroup(반반_치킨, 양념_선택));

        Item item1 = new Item("후라이드", new Money(0));
        Item item2 = new Item("양념", new Money(500));
        Item item3 = new Item("간장", new Money(1000));

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);

        em.persist(new ItemGroupItem(양념_선택, item1));
        em.persist(new ItemGroupItem(양념_선택, item2));
        em.persist(new ItemGroupItem(양념_선택, item3));

        // id가 3인 아이템을 선택했다고 가정

        List<ItemGroup> itemGroups = queryFactory.select(itemGroup)
                .from(productItemGroup)
                .where(productItemGroup.product.id.eq(1L))
                .fetch();

        System.out.println("==========================================");
        itemGroups.forEach(ig -> System.out.println(ig.getName()));
        System.out.println("==========================================");


        Map<ItemGroup, List<Item>> map = queryFactory
                .from(itemGroupItem)
                .join(itemGroupItem.item, item)
                .join(itemGroupItem.itemGroup, itemGroup)
                .join(productItemGroup).on(productItemGroup.itemGroup.eq(itemGroup))
                .where(itemGroupItem.item.id.in(3L), productItemGroup.product.id.eq(1L))
                .transform(groupBy(itemGroup).as(GroupBy.list(item)));

        System.out.println("==========================================");
        map.entrySet()
                .forEach(es -> {
                    System.out.println(es.getKey().getName());
                    es.getValue().forEach(i -> System.out.println(i.getName()));
                });

        System.out.println("==========================================");


    }
}