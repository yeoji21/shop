package delivery.shop.product.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private Money price;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductItemGroup> itemGroups = new ArrayList<>();

    public Product(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public void addItemGroup(ItemGroup itemGroup) {
        if(itemGroup.getId() == null) throw new IllegalArgumentException();
        itemGroups.add(new ProductItemGroup(this, itemGroup));
    }

    public Money place(Map<ItemGroup, List<Item>> map){
        if(map.isEmpty())
            return this.price;
        if(map.size() != itemGroups.size())
            throw new IllegalArgumentException();

        Money totalAmount = Money.ZERO.add(this.price);
        for (ItemGroup key : map.keySet()) {
            ProductItemGroup productItemGroup = findProductItemGroup(key);
            Money itemAmount = productItemGroup.placeWithItems(map.get(key));
            totalAmount = totalAmount.add(itemAmount);
        }

        return totalAmount;
    }

    private ProductItemGroup findProductItemGroup(ItemGroup key) {
        return itemGroups
                .stream()
                .filter(productItemGroup -> productItemGroup.hasItemGroup(key))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
