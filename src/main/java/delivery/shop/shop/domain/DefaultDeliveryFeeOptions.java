package delivery.shop.shop.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DefaultDeliveryFeeOptions {
    @OrderBy("orderAmount asc")
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderAmountDeliveryFee> deliveryFeeOptions = new ArrayList<>();

    void add(OrderAmountDeliveryFee deliveryFee) {
        deliveryFeeOptions.add(deliveryFee);
    }

    Money calculateDeliveryFee(Money orderAmount) {
        // 대충 이런 식.. 수정필요
        return deliveryFeeOptions.stream()
                .filter(deliveryFee -> deliveryFee.isSatisfiedOrderAmount(orderAmount))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getFee();
    }

    // TODO: 2022/07/29 테스트해보고 제거
    public List<OrderAmountDeliveryFee> getDeliveryFeeOptions() {
        return deliveryFeeOptions;
    }

}
