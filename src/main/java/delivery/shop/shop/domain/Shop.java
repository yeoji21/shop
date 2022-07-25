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

    @Embedded
    private Money minOrderPrice;

    @Embedded
    private ShopLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_file_id")
    private File shopImage;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DeliveryFee> deliveryFees = new ArrayList<>();

    @Builder
    public Shop(String shopName, Money minOrderPrice, ShopLocation location, File shopImage, List<DeliveryFee> deliveryFees) {
        this.shopName = shopName;
        this.minOrderPrice = minOrderPrice;
        this.location = location;
        this.shopImage = shopImage;
        this.deliveryFees = deliveryFees;
    }

    public void addDeliveryFee(DeliveryFee deliveryFee) {
        deliveryFees.add(deliveryFee);
        deliveryFee.setShop(this);
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}





















