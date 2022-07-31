package delivery.shop.product.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private Money price;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MenuProduct> menuProducts = new ArrayList<>();


    public Product(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
