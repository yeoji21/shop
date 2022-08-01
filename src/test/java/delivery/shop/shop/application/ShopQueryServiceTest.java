package delivery.shop.shop.application;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import delivery.shop.shop.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static delivery.shop.product.domain.QProduct.product;
import static delivery.shop.shop.domain.QMenu.menu;
import static delivery.shop.shop.domain.QMenuProduct.menuProduct;

@SpringBootTest
@Transactional
class ShopQueryServiceTest {
    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    void basic_query() throws Exception{
        List<Long> menuIDs = queryFactory.select(menu.id)
                .from(menu)
                .where(menu.shop.id.eq(1L))
                .fetch();

        Map<Menu, List<Tuple>> map = queryFactory
                .select(menu, product)
                .from(menuProduct)
                .join(menuProduct.menu, menu)
                .join(product).on(menuProduct.productId.eq(product.id))
                .where(menuProduct.menu.id.in(menuIDs),
                        menuProduct.displayInfo.isDisplay.isTrue())
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(t -> t.get(menu)));

        for (Map.Entry<Menu, List<Tuple>> es : map.entrySet()) {
            System.out.println(es.getKey().getMenuName());
            es.getValue().forEach(t -> System.out.print(t.get(product).getName() + " "));
            System.out.println();
        }

    }
}