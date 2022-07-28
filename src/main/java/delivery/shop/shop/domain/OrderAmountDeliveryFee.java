package delivery.shop.shop.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(exclude = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "delivery_fee")
@Entity
public class OrderAmountDeliveryFee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter(AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "order_price"))
    private Money orderAmount;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "fee"))
    private Money fee;

    public OrderAmountDeliveryFee(Money orderAmount, Money fee) {
        this.orderAmount = orderAmount;
        this.fee = fee;
    }

    void setShop(Shop shop) {
        this.shop = shop;
    }

    boolean isSatisfiedOrderAmount(Money orderAmount) {
        return false;
    }
}
