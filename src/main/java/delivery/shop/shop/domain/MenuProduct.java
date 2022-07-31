package delivery.shop.shop.domain;

import delivery.shop.common.domain.DisplayInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MenuProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private DisplayInfo displayInfo;

    public MenuProduct(Long productId, Menu menu, DisplayInfo displayInfo) {
        this.productId = productId;
        this.menu = menu;
        this.displayInfo = displayInfo;
    }
}
