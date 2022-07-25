package delivery.shop.shop;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ShopLocation {
    @Column(table = "shop_location",
            name = "street_address")
    private String streetAddress;

    @Column(table = "shop_location",
            name = "latitude")
    private Double latitude;

    @Column(table = "shop_location",
            name = "longitude")
    private Double longitude;

    public ShopLocation(String streetAddress, Double latitude, Double longitude) {
        this.streetAddress = streetAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
