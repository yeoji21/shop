package delivery.shop.shop.domain;


import delivery.shop.common.domain.Money;
import lombok.*;

import javax.persistence.*;
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

    @Column(name = "shop_file_id")
    private Long shopThumbnailFileId;

    @Embedded @Column(name = "min_order_price")
    private Money minOrderAmount;

    @Column(name = "introduction")
    private String introduction;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private BusinessTimeInfo businessTimeInfo;

    @Embedded
    private ShopLocation location;

    @Embedded
    private DefaultDeliveryFees defaultDeliveryFees;

    @ElementCollection
    @CollectionTable(
            name = "shop_category",
            joinColumns = @JoinColumn(name = "shop_id", referencedColumnName = "id")
    )
    private List<Long> categoryIds = new ArrayList<>();

    @Builder
    public Shop(String shopName,
                Money minOrderAmount,
                PhoneNumber phoneNumber,
                String introduction,
                BusinessTimeInfo businessTimeInfo,
                ShopLocation location,
                Long shopThumbnailFileId) {
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.phoneNumber = phoneNumber;
        this.introduction = introduction;
        this.businessTimeInfo = businessTimeInfo;
        this.location = location;
        this.shopThumbnailFileId = shopThumbnailFileId;
        this.defaultDeliveryFees = new DefaultDeliveryFees();
    }

    public void addDeliveryFee(OrderAmountDeliveryFee defaultDeliveryFee) {
        defaultDeliveryFees.add(defaultDeliveryFee);
        defaultDeliveryFee.setShop(this);
    }

    public Money calculateDefaultDeliveryFee(Money orderAmount) {
        return defaultDeliveryFees.calculateDeliveryFee(orderAmount);
    }

    public void includeCategory(long categoryId) {
        categoryIds.add(categoryId);
    }
}





















