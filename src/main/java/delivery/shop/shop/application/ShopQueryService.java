package delivery.shop.shop.application;

import delivery.shop.shop.application.dto.response.ShopDetailInfo;
import delivery.shop.shop.application.dto.response.ShopSimpleInfo;
import delivery.shop.shop.infra.ShopQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShopQueryService {
    private final ShopQueryDao queryDao;

    @Transactional(readOnly = true)
    public ShopSimpleInfo findSimpleInfo(long shopId) {
        return queryDao.findSimpleInfo(shopId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public ShopDetailInfo findDetailInfo(long shopId) {
        return queryDao.findDetailInfo(shopId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
