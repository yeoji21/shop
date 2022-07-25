package delivery.shop.shop.domain;


import delivery.shop.common.domain.Money;
import delivery.shop.file.domain.File;
import lombok.*;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SecondaryTable(
        name = "shop_location",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "shop_id", referencedColumnName = "id")
)
@Table(name = "shop")
@Entity
public class Shop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "shop_name")
    private String shopName;

    @Embedded @Column(name = "min_order_price")
    private Money minOrderPrice;

    @Embedded @Column(name = "phone_number")
    private PhoneNumber phoneNumber;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "business_hour")
    private String businessHour;

    @Column(name = "days_off")
    private String dayOff;

    @Embedded
    private ShopLocation location;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DeliveryFee> deliveryFees = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_file_id")
    private File shopImage;

    @Builder
    public Shop(String shopName,
                Money minOrderPrice,
                PhoneNumber phoneNumber,
                String introduction,
                String businessHour, String dayOff,
                ShopLocation location,
                File shopImage) {
        this.shopName = shopName;
        this.minOrderPrice = minOrderPrice;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.businessHour = businessHour;
        this.dayOff = dayOff;
        this.location = location;
        this.shopImage = shopImage;
    }

    public void addDeliveryFee(DeliveryFee deliveryFee) {
        deliveryFees.add(deliveryFee);
        deliveryFee.setShop(this);
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}





















