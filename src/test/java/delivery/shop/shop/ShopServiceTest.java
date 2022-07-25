package delivery.shop.shop;

import delivery.shop.common.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShopServiceTest {
    @Autowired private ShopRepository shopRepository;

    @Test
    void withDeliveryFee() throws Exception{

        Shop newShop = Shop.builder()
                .shopName("shop")
                .minOrderPrice(new Money(15_000))
                .location(new ShopLocation("xxxx", 1.0, 2.0))
                .build();

        newShop.addDeliveryFee(new DeliveryFee(new Money(15_000), new Money(3000)));
        newShop.addDeliveryFee(new DeliveryFee(new Money(20_000), new Money(1000)));
        newShop.addDeliveryFee(new DeliveryFee(new Money(25_000), new Money(0)));

        shopRepository.save(newShop);
    }
}