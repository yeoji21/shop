package delivery.shop.shop.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CategoryShop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    CategoryShop(Shop shop, Long categoryId) {
        this.shop = shop;
        this.categoryId = categoryId;
    }
}
