package delivery.shop.shop.application;

import delivery.shop.shop.application.dto.response.ShopSimpleInfo;
import delivery.shop.shop.infra.ShopQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShopQueryService {
    private final ShopQueryDao queryRepository;

    @Transactional(readOnly = true)
    public ShopSimpleInfo findSimpleInfo(long shopId) {
        return queryRepository.findSimpleInfo(shopId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
