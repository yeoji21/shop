package delivery.shop.common.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class Money {
    public static Money ZERO = new Money(0);
    private int value;

    public Money(int value) {
        this.value = value;
    }

    public Money add(Money money) {
        return new Money(this.value + money.value);
    }

    public Money minus(Money money) {
        return new Money(this.value - money.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int toInt() {
        return value;
    }
}
