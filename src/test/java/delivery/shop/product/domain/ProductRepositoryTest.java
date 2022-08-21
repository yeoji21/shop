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
import static delivery.shop.product.domain.QItemInGroup.itemInGroup;
import static delivery.shop.product.domain.QOption.option;
import static delivery.shop.product.domain.QOptionGroup.optionGroup;
import static delivery.shop.product.domain.QOptionInGroup.optionInGroup;
import static delivery.shop.product.domain.QProduct.product;
import static delivery.shop.product.domain.QProductItemGroup.productItemGroup;
import static delivery.shop.product.domain.QProductOptionGroup.productOptionGroup;
import static org.assertj.core.api.Assertions.assertThat;

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
    void 단방향_상품_저장_테스트() throws Exception{
        setDataUnidirectional();

        // id가 3인 아이템을 선택했다고 가정
        // 해당 상품의 모든 itemGroup 조회
        List<ItemGroup> itemGroups = queryFactory.select(itemGroup)
                .from(productItemGroup)
                .where(productItemGroup.product.id.eq(1L))
                .fetch();

        System.out.println("==========================================");
        itemGroups.forEach(ig -> System.out.println(ig.getName()));
        System.out.println("==========================================");

        // 해당 상품의 1번 아이템과 그 아이템 그룹을 map으로 조회
        Map<ItemGroup, List<Item>> map = queryFactory
                .from(itemInGroup)
                .join(itemInGroup.item, item)
                .join(itemInGroup.itemGroup, itemGroup)
                .join(productItemGroup).on(productItemGroup.itemGroup.eq(itemGroup))
                .where(itemInGroup.item.id.in(3L), productItemGroup.product.id.eq(1L))
                .transform(groupBy(itemGroup).as(GroupBy.list(item)));

        // 엔티티간 양방향 매핑을 하지 않으면 각각 따로 조회해서 맞춰봐야 함
        System.out.println("==========================================");
        map.entrySet()
                .forEach(es -> {
                    System.out.println(es.getKey().getName());
                    es.getValue().forEach(i -> System.out.println(i.getName()));
                });

        // 개수로만 체크하면 안됨
        assertThat(itemGroups.size()).isEqualTo(map.size());

        for (Map.Entry<ItemGroup, List<Item>> es : map.entrySet()) {
            es.getKey().calculateItemAmount(map.get(es.getKey()));
        }

        System.out.println("==========================================");
    }

    @Test @Rollback(value = false)
    void place_v1() throws Exception{
        setDataBidirectional();
        System.out.println("=========================================================================");

        Product findProduct = queryFactory.selectFrom(product)
                .leftJoin(product.itemGroups, productItemGroup).fetchJoin()
                .join(productItemGroup.itemGroup, itemGroup).fetchJoin()
                .where(product.id.eq(1L))
                .fetchOne();

        System.out.println("**********************************************************************************");
        Map<Long, List<Item>> map = queryFactory
                .from(itemInGroup)
                .join(itemInGroup.item, item)
                .join(itemInGroup.itemGroup, itemGroup)
                .join(productItemGroup).on(productItemGroup.itemGroup.eq(itemGroup))
                .where(itemInGroup.item.id.in(2L, 3L, 5L), productItemGroup.product.id.eq(1L))
                .transform(groupBy(itemGroup.id).as(GroupBy.list(item)));
        System.out.println("**********************************************************************************");

        Money totalAmount = findProduct.place(map);
        System.out.println(totalAmount.toInt());
    }

    @Test @Rollback(value = false)
    void place_v2() throws Exception{
        Product 반반_치킨 = new Product("반반 치킨", new Money(15_000));
        em.persist(반반_치킨);

        ItemGroup 뼈_선택 = new ItemGroup("뼈_선택", 1, 1);
        em.persist(뼈_선택);

        ItemGroup 양념_선택 = new ItemGroup("양념 선택", 1, 2);
        em.persist(양념_선택);

        ItemGroup emptyGroup = new ItemGroup("emptyGroup", 1, 1);
        em.persist(emptyGroup);

        반반_치킨.addItemGroup(양념_선택);
        반반_치킨.addItemGroup(뼈_선택);
//        반반_치킨.addItemGroup(emptyGroup);

        Item item1 = new Item("후라이드", new Money(0));
        Item item2 = new Item("양념", new Money(500));
        Item item3 = new Item("간장", new Money(1000));

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);

        양념_선택.addItem(item1);
        양념_선택.addItem(item2);
        양념_선택.addItem(item3);

        Item item4 = new Item("뼈", new Money(0));
        Item item5 = new Item("순살", new Money(2_000));

        em.persist(item4);
        em.persist(item5);

        뼈_선택.addItem(item4);
        뼈_선택.addItem(item5);


        OptionGroup sideMenu = new OptionGroup("사이드 메뉴", 3);
        em.persist(sideMenu);
        em.persist(new ProductOptionGroup(반반_치킨, sideMenu));

        OptionGroup emptyOptionGroup = new OptionGroup("emptyOptionGroup", 3);
        em.persist(emptyOptionGroup);
        em.persist(new ProductOptionGroup(반반_치킨, emptyOptionGroup));

        Option optionA = new Option("optionA", new Money(1000));
        Option optionB = new Option("optionB", new Money(2000));
        Option optionC = new Option("optionC", new Money(3000));
        Option optionD = new Option("optionD", new Money(4000));

        em.persist(optionA);
        em.persist(optionB);
        em.persist(optionC);
        em.persist(optionD);

        sideMenu.addOption(optionA);
        sideMenu.addOption(optionB);
        sideMenu.addOption(optionC);
        sideMenu.addOption(optionD);


        em.flush();
        em.clear();

        System.out.println("=========================================================================");




        // 가격 계산 없이 필수 옵션 다 들어왔는지만 체크하려면 이게 베스트
        Map<ItemGroup, List<Item>> ingredientMap = queryFactory
                .from(productItemGroup)
                .join(productItemGroup.product, product)
                .join(productItemGroup.itemGroup, itemGroup)
                .leftJoin(itemInGroup).on(itemInGroup.itemGroup.eq(itemGroup))
                .leftJoin(itemInGroup.item, item).on(item.id.in(2L, 3L, 5L))
                .where(product.id.eq(1L))
                .transform(groupBy(itemGroup).as(GroupBy.list(item)));

        for (Map.Entry<ItemGroup, List<Item>> es : ingredientMap.entrySet()) {
            System.out.println(es.getKey().getName());
            System.out.print(" -> ");
            es.getValue().forEach(i -> System.out.print(i.getName() + " "));
            Money money = es.getKey().calculateItemAmount(es.getValue());
            System.out.println("totalPrice = " + money.toInt());
            System.out.println();
        }

        // 옵션은 이렇게 조회하면 안되지! 안들어와도 되니까
        Map<OptionGroup, List<Option>> optionMap = queryFactory
                .from(productOptionGroup)
                .join(productOptionGroup.product, product)
                .join(productOptionGroup.optionGroup, optionGroup)
                .leftJoin(optionInGroup).on(optionInGroup.optionGroup.eq(optionGroup))
                .leftJoin(optionInGroup.option, option).on(option.id.in(optionA.getId(), optionB.getId(), optionC.getId()))
                .where(product.id.eq(1L))
                .transform(groupBy(optionGroup).as(GroupBy.list(option)));

        for (Map.Entry<OptionGroup, List<Option>> es : optionMap.entrySet()) {
            System.out.println(es.getKey().getName());
            System.out.print(" -> ");
            es.getValue().forEach(o -> System.out.print(o.getName() + " "));
            es.getKey().validateOptions(es.getValue());
            System.out.println();
        }

    }

    private void setDataBidirectional() {
        Product 반반_치킨 = new Product("반반 치킨", new Money(15_000));
        em.persist(반반_치킨);

        ItemGroup 뼈_선택 = new ItemGroup("뼈_선택", 1, 1);
        em.persist(뼈_선택);

        ItemGroup 양념_선택 = new ItemGroup("양념 선택", 1, 2);
        em.persist(양념_선택);

        반반_치킨.addItemGroup(양념_선택);
        반반_치킨.addItemGroup(뼈_선택);

//        ItemGroup emptyGroup = new ItemGroup("emptyGroup", 1, 1);
//        em.persist(emptyGroup);
//        반반_치킨.addItemGroup(emptyGroup);

        Item item1 = new Item("후라이드", new Money(0));
        Item item2 = new Item("양념", new Money(500));
        Item item3 = new Item("간장", new Money(1000));

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);

        양념_선택.addItem(item1);
        양념_선택.addItem(item2);
        양념_선택.addItem(item3);

        Item item4 = new Item("뼈", new Money(0));
        Item item5 = new Item("순살", new Money(2_000));

        em.persist(item4);
        em.persist(item5);

        뼈_선택.addItem(item4);
        뼈_선택.addItem(item5);

        em.flush();
        em.clear();
    }

    private void setDataUnidirectional() {
        Product 반반_치킨 = new Product("반반 치킨", new Money(15_000));
        em.persist(반반_치킨);

        ItemGroup 양념_선택 = new ItemGroup("양념 선택", 1, 1);
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

        em.persist(new ItemInGroup(양념_선택, item1));
        em.persist(new ItemInGroup(양념_선택, item2));
        em.persist(new ItemInGroup(양념_선택, item3));

        em.flush();
        em.clear();
    }
}