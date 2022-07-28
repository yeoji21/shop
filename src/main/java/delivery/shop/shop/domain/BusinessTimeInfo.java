package delivery.shop.shop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class BusinessTimeInfo {
    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "days_off")
    private String dayOff;
}
