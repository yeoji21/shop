package delivery.shop.shop.domain;


import delivery.shop.common.domain.Money;
import delivery.shop.file.domain.File;
import lombok.*;

import javax.persistence.*;

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
    private Money minOrderAmount;

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

    @Embedded
    private DefaultDeliveryFees defaultDeliveryFees;

    // TODO: 2022/07/28 객체 참조 제거하고 ID 가지도록?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_file_id")
    private File shopThumbnail;

    @Builder
    public Shop(String shopName,
                Money minOrderAmount,
                PhoneNumber phoneNumber,
                String introduction,
                String businessHour, String dayOff,
                ShopLocation location,
                File shopThumbnail) {
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.businessHour = businessHour;
        this.dayOff = dayOff;
        this.location = location;
        this.shopThumbnail = shopThumbnail;
        this.defaultDeliveryFees = new DefaultDeliveryFees();
    }

    public void addDeliveryFee(OrderAmountDeliveryFee defaultDeliveryFee) {
        defaultDeliveryFees.add(defaultDeliveryFee);
        defaultDeliveryFee.setShop(this);
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}





















