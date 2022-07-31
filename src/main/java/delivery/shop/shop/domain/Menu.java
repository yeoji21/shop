package delivery.shop.shop.domain;

import delivery.shop.common.domain.DisplayInfo;
import delivery.shop.product.domain.Product;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "introduction")
    private String introduction;

    @Embedded
    private DisplayInfo displayInfo;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu(String menuName, String introduction) {
        this.menuName = menuName;
        this.introduction = introduction;
    }

    void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setDisplayInfo(DisplayInfo displayInfo) {
        this.displayInfo = displayInfo;
    }

    public void addProduct(Product product) {
        MenuProduct menuProduct = new MenuProduct(product.getId(), this, new DisplayInfo(menuProducts.size()));
        menuProducts.add(menuProduct);
    }
}
