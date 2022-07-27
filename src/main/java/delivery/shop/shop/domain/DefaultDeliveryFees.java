package delivery.shop.shop.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DefaultDeliveryFees {
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderAmountDeliveryFee> deliveryFees = new ArrayList<>();

    public void add(OrderAmountDeliveryFee deliveryFee) {
        deliveryFees.add(deliveryFee);
    }

    public Money calculateDeliveryFee(Money orderAmount) {
        return null;
    }
}
