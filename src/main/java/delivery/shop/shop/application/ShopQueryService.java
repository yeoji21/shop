package delivery.shop.shop.application;

import delivery.shop.shop.application.dto.response.ShopSimpleInfo;
import delivery.shop.shop.domain.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShopQueryService {
    private final ShopRepository shopRepository;

    @Transactional(readOnly = true)
    public ShopSimpleInfo findSimpleInfo(long shopId) {
        return shopRepository.findSimpleInfo(shopId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
