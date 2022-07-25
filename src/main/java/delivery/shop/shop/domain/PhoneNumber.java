package delivery.shop.shop.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PhoneNumber {
    private String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        if(!phoneNumber.matches("\\d{2,3}-\\d{3,4}-\\d{4}"))
            throw new IllegalArgumentException("invalid phone number format");
        this.phoneNumber = phoneNumber;
    }
}
