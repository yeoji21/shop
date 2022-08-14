package delivery.shop.product.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

// TODO: 2022/08/11 양방향 연관과 단방향 연관인 경우 비교
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

    public Money place(Map<ItemGroup, List<Item>> items){
        if(items.isEmpty())
            return this.price;
        if(items.size() != itemGroups.size())
            throw new IllegalArgumentException();

        Money totalAmount = Money.ZERO.add(this.price);
        for (ItemGroup key : items.keySet()) {
            ProductItemGroup productItemGroup = findProductItemGroup(key);
            Money itemAmount = productItemGroup.placeWithItems(items.get(key));
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
