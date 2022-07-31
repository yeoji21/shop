package delivery.shop.shop.domain;

import delivery.shop.common.domain.DisplayInfo;

import javax.persistence.*;

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
}
