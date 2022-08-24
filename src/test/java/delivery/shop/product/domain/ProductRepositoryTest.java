package delivery.shop.product.domain;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.common.config.JpaQueryFactoryConfig;
import delivery.shop.common.domain.Money;
import delivery.shop.product.application.dto.*;
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
import static com.querydsl.core.group.GroupBy.list;
import static delivery.shop.product.domain.QIngredient.ingredient;
import static delivery.shop.product.domain.QIngredientGroup.ingredientGroup;
import static delivery.shop.product.domain.QIngredientInGroup.ingredientInGroup;
import static delivery.shop.product.domain.QOption.option;
import static delivery.shop.product.domain.QOptionGroup.optionGroup;
import static delivery.shop.product.domain.QOptionInGroup.optionInGroup;
import static delivery.shop.product.domain.QProduct.product;
import static delivery.shop.product.domain.QProductIngredientGroup.productIngredientGroup;
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
        List<IngredientGroup> ingredientGroups = queryFactory.select(ingredientGroup)
                .from(productIngredientGroup)
                .where(productIngredientGroup.product.id.eq(1L))
                .fetch();

        System.out.println("==========================================");
        ingredientGroups.forEach(ig -> System.out.println(ig.getName()));
        System.out.println("==========================================");

        // 해당 상품의 1번 아이템과 그 아이템 그룹을 map으로 조회
        Map<IngredientGroup, List<Ingredient>> map = queryFactory
                .from(ingredientInGroup)
                .join(ingredientInGroup.ingredient, ingredient)
                .join(ingredientInGroup.ingredientGroup, ingredientGroup)
                .join(productIngredientGroup).on(productIngredientGroup.ingredientGroup.eq(ingredientGroup))
                .where(ingredientInGroup.ingredient.id.in(3L), productIngredientGroup.product.id.eq(1L))
                .transform(groupBy(ingredientGroup).as(GroupBy.list(ingredient)));

        // 엔티티간 양방향 매핑을 하지 않으면 각각 따로 조회해서 맞춰봐야 함
        System.out.println("==========================================");
        map.entrySet()
                .forEach(es -> {
                    System.out.println(es.getKey().getName());
                    es.getValue().forEach(i -> System.out.println(i.getName()));
                });

        // 개수로만 체크하면 안됨
        assertThat(ingredientGroups.size()).isEqualTo(map.size());

        for (Map.Entry<IngredientGroup, List<Ingredient>> es : map.entrySet()) {
            es.getKey().calculateItemAmount(map.get(es.getKey()));
        }

        System.out.println("==========================================");
    }

    @Test
    void 상품_조회_테스트() throws Exception{
        setDataBidirectional();

        CompleteProductInfo completeProductInfo = queryFactory
                .select(new QCompleteProductInfo(product.id, product.name, product.price))
                .from(product)
                .where(product.id.eq(1L))
                .fetchOne();

        List<IngredientGroupInfo> ingredientGroupInfos = queryFactory
                .from(productIngredientGroup)
                .leftJoin(productIngredientGroup.ingredientGroup, ingredientGroup)
                .leftJoin(ingredientInGroup).on(ingredientInGroup.ingredientGroup.eq(ingredientGroup))
                .leftJoin(ingredientInGroup.ingredient, ingredient)
                .where(productIngredientGroup.product.id.eq(1L))
                .transform(groupBy(ingredientGroup)
                        .list(new QIngredientGroupInfo(ingredientGroup.id, ingredientGroup.name, ingredientGroup.minCount, ingredientGroup.maxCount,
                                list(new QIngredientInfo(ingredient.id, ingredient.name, ingredient.price)))));

        ingredientGroupInfos.forEach(
                info -> {
                    System.out.println(info.getIngredientGroupId() + " : " + info.getName() + " : " + info.getMinSelectCount() + " : " + info.getMaxSelectCount());
                    info.getIngredientInfos().forEach(i -> System.out.println(" -------> " + i.getIngredientId() + " : " + i.getName() + " : " + i.getPrice().toInt()));
                }
        );

        List<OptionGroupInfo> optionGroupInfos = queryFactory
                .from(productOptionGroup)
                .leftJoin(productOptionGroup.optionGroup, optionGroup)
                .leftJoin(optionInGroup).on(optionInGroup.optionGroup.eq(optionGroup))
                .leftJoin(optionInGroup.option, option)
                .where(productOptionGroup.product.id.eq(1L))
                .transform(groupBy(optionGroup)
                        .list(new QOptionGroupInfo(optionGroup.id, optionGroup.name, optionGroup.maxSelectCount,
                                list(new QOptionInfo(option.id, option.name, option.price)))));

        optionGroupInfos.forEach(
                info -> {
                    System.out.println(info.getOptionGroupId() + " : " + info.getName() + " : " + info.getMaxSelectCount());
                    info.getOptionInfos().forEach(i -> System.out.println(" -------> " + i.getOptionId() + " : " + i.getName() + " : " + i.getMoney().toInt()));
                }
        );

        completeProductInfo.setIngredientGroupInfos(ingredientGroupInfos);
        completeProductInfo.setOptionGroupInfos(optionGroupInfos);
        System.out.println("=========================================");
        System.out.println(completeProductInfo.getProductId() + " : " + completeProductInfo.getProductName() + " : " + completeProductInfo.getProductPrice().toInt());
        System.out.println("=========================================");
    }

    @Test @Rollback(value = false)
    void place_v1() throws Exception{
        setDataBidirectional();
        System.out.println("=========================================================================");

        Product findProduct = queryFactory.selectFrom(product)
                .leftJoin(product.ingredientGroups, productIngredientGroup).fetchJoin()
                .join(productIngredientGroup.ingredientGroup, ingredientGroup).fetchJoin()
                .where(product.id.eq(1L))
                .fetchOne();

        System.out.println("**********************************************************************************");
        Map<Long, List<Ingredient>> map = queryFactory
                .from(ingredientInGroup)
                .join(ingredientInGroup.ingredient, ingredient)
                .join(ingredientInGroup.ingredientGroup, ingredientGroup)
                .join(productIngredientGroup).on(productIngredientGroup.ingredientGroup.eq(ingredientGroup))
                .where(ingredientInGroup.ingredient.id.in(2L, 3L, 5L), productIngredientGroup.product.id.eq(1L))
                .transform(groupBy(ingredientGroup.id).as(GroupBy.list(ingredient)));
        System.out.println("**********************************************************************************");

        Money totalAmount = findProduct.place(map);
        System.out.println(totalAmount.toInt());
    }

    @Test @Rollback(value = false)
    void place_v2() throws Exception{
        Product 반반_치킨 = new Product("반반 치킨", new Money(15_000));
        em.persist(반반_치킨);

        IngredientGroup 뼈_선택 = new IngredientGroup("뼈_선택", 1, 1);
        em.persist(뼈_선택);

        IngredientGroup 양념_선택 = new IngredientGroup("양념 선택", 1, 2);
        em.persist(양념_선택);

        IngredientGroup emptyGroup = new IngredientGroup("emptyGroup", 1, 1);
        em.persist(emptyGroup);

        반반_치킨.addItemGroup(양념_선택);
        반반_치킨.addItemGroup(뼈_선택);
//        반반_치킨.addItemGroup(emptyGroup);

        Ingredient ingredient1 = new Ingredient("후라이드", new Money(0));
        Ingredient ingredient2 = new Ingredient("양념", new Money(500));
        Ingredient ingredient3 = new Ingredient("간장", new Money(1000));

        em.persist(ingredient1);
        em.persist(ingredient2);
        em.persist(ingredient3);

        양념_선택.addItem(ingredient1);
        양념_선택.addItem(ingredient2);
        양념_선택.addItem(ingredient3);

        Ingredient ingredient4 = new Ingredient("뼈", new Money(0));
        Ingredient ingredient5 = new Ingredient("순살", new Money(2_000));

        em.persist(ingredient4);
        em.persist(ingredient5);

        뼈_선택.addItem(ingredient4);
        뼈_선택.addItem(ingredient5);


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
        // 조회 때 item의 판매여부 체크 추가해야 함
        List<Long> ingredientIds = List.of(ingredient2.getId(), ingredient3.getId(), ingredient5.getId());
        Map<IngredientGroup, List<Ingredient>> ingredientMap = queryFactory
                .from(productIngredientGroup)
                .innerJoin(productIngredientGroup.product, product)
                .innerJoin(productIngredientGroup.ingredientGroup, ingredientGroup)
                .leftJoin(ingredientInGroup).on(ingredientInGroup.ingredientGroup.eq(ingredientGroup))
                .leftJoin(ingredientInGroup.ingredient, ingredient).on(ingredient.id.in(ingredientIds))
                .where(product.id.eq(1L))
                .transform(groupBy(ingredientGroup).as(GroupBy.list(ingredient)));

        boolean[] checkIds = new boolean[ingredientIds.size()];
        int ingredientSize = 0;
        for (Map.Entry<IngredientGroup, List<Ingredient>> es : ingredientMap.entrySet()) {
            ingredientSize += es.getValue().size();
            System.out.println(es.getKey().getName());
            System.out.print(" -> ");
            es.getValue().forEach(i -> System.out.print(i.getName() + " "));
            Money money = es.getKey().calculateItemAmount(es.getValue());
            System.out.println("totalPrice = " + money.toInt());
            System.out.println();
            es.getValue().forEach(ingredient -> {
                int index = ingredientIds.indexOf(ingredient.getId());
                checkIds[index] = true;
            });
        }

        System.out.println("ingredientSize = " + ingredientSize);
        for (int i = 0; i < checkIds.length; i++) {
            if(!checkIds[i])
                throw new IllegalArgumentException("ingredient id " + ingredientIds.get(i) + " is not in product");
        }

        Map<OptionGroup, List<Option>> optionMap = queryFactory
                .from(productOptionGroup)
                .join(productOptionGroup.product, product)
                .join(productOptionGroup.optionGroup, optionGroup)
                .leftJoin(optionInGroup).on(optionInGroup.optionGroup.eq(optionGroup))
                .leftJoin(optionInGroup.option, option)
                .where(product.id.eq(1L), option.id.in(optionA.getId(), optionB.getId(), optionC.getId()))
//                .where(product.id.eq(1L), option.id.in(1231, 4124, 1412))
                .transform(groupBy(optionGroup).as(GroupBy.list(option)));

        // 근데 이러면 만약 product랑 option이랑 매핑이 안돼있어서 아무것도 리턴 못받아도 예외 안터질텐데?
        // 문제 생길 여지 많음
        // 조회된 옵션 개수가 파라미터로 넘어온 개수랑 같은지도 체크해야겠음 -> ingredient도 마찬가지
        // 개수 비교 말고 equals 비교?

        int optionSize = 0;
        for (Map.Entry<OptionGroup, List<Option>> es : optionMap.entrySet()) {
            optionSize += es.getValue().size();
            System.out.println(es.getKey().getName());
            System.out.print(" -> ");
            es.getValue().forEach(o -> System.out.print(o.getName() + " "));
            es.getKey().validateOptions(es.getValue());
            System.out.println();
        }

        System.out.println("optionSize = " + optionSize);

    }

    private void setDataBidirectional() {
        Product 반반_치킨 = new Product("반반 치킨", new Money(15_000));
        em.persist(반반_치킨);

        IngredientGroup 뼈_선택 = new IngredientGroup("뼈_선택", 1, 1);
        em.persist(뼈_선택);

        IngredientGroup 양념_선택 = new IngredientGroup("양념 선택", 1, 2);
        em.persist(양념_선택);

        반반_치킨.addItemGroup(양념_선택);
        반반_치킨.addItemGroup(뼈_선택);

//        ItemGroup emptyGroup = new ItemGroup("emptyGroup", 1, 1);
//        em.persist(emptyGroup);
//        반반_치킨.addItemGroup(emptyGroup);

        Ingredient ingredient1 = new Ingredient("후라이드", new Money(0));
        Ingredient ingredient2 = new Ingredient("양념", new Money(500));
        Ingredient ingredient3 = new Ingredient("간장", new Money(1000));

        em.persist(ingredient1);
        em.persist(ingredient2);
        em.persist(ingredient3);

        양념_선택.addItem(ingredient1);
        양념_선택.addItem(ingredient2);
        양념_선택.addItem(ingredient3);

        Ingredient ingredient4 = new Ingredient("뼈", new Money(0));
        Ingredient ingredient5 = new Ingredient("순살", new Money(2_000));

        em.persist(ingredient4);
        em.persist(ingredient5);

        뼈_선택.addItem(ingredient4);
        뼈_선택.addItem(ingredient5);


        OptionGroup sideMenu = new OptionGroup("사이드 메뉴", 3);
        em.persist(sideMenu);
        em.persist(new ProductOptionGroup(반반_치킨, sideMenu));

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
    }

    private void setDataUnidirectional() {
        Product 반반_치킨 = new Product("반반 치킨", new Money(15_000));
        em.persist(반반_치킨);

        IngredientGroup 양념_선택 = new IngredientGroup("양념 선택", 1, 1);
        em.persist(양념_선택);

//        ItemGroup 양념_선택2 = new ItemGroup("양념 선택2", 2, 2);
//        em.persist(양념_선택2);
//        em.persist(new ProductItemGroup(반반_치킨, 양념_선택2));

        em.persist(new ProductIngredientGroup(반반_치킨, 양념_선택));

        Ingredient ingredient1 = new Ingredient("후라이드", new Money(0));
        Ingredient ingredient2 = new Ingredient("양념", new Money(500));
        Ingredient ingredient3 = new Ingredient("간장", new Money(1000));

        em.persist(ingredient1);
        em.persist(ingredient2);
        em.persist(ingredient3);

        em.persist(new IngredientInGroup(양념_선택, ingredient1));
        em.persist(new IngredientInGroup(양념_선택, ingredient2));
        em.persist(new IngredientInGroup(양념_선택, ingredient3));

        em.flush();
        em.clear();
    }
}