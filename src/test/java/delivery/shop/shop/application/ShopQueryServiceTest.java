package delivery.shop.shop.application;

import delivery.shop.shop.domain.ShopRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShopQueryServiceTest {
    @Mock
    private ShopRepository shopRepository;

    @InjectMocks
    private ShopQueryService shopQueryService;

}