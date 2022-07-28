package delivery.shop.common.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class Money {
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

    public Money times(double times) {






        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int toInt() {
        return value;
    }
}
