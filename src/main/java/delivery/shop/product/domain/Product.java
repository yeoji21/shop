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
    private List<ProductIngredientGroup> ingredientGroups = new ArrayList<>();

    public Product(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public void addItemGroup(IngredientGroup ingredientGroup) {
        if(ingredientGroup.getId() == null) throw new IllegalArgumentException();
        ingredientGroups.add(new ProductIngredientGroup(this, ingredientGroup));
    }

    public Money place(Map<Long, List<Ingredient>> map){
        if(map.isEmpty())
            return this.price;
        if(map.size() != ingredientGroups.size())
            throw new IllegalArgumentException();

        Money totalAmount = Money.ZERO.add(this.price);
        for (Long key : map.keySet()) {
            ProductIngredientGroup productIngredientGroup = findProductItemGroup(key);
            Money itemAmount = productIngredientGroup.placeWithItems(map.get(key));
            totalAmount = totalAmount.add(itemAmount);
        }

        return totalAmount;
    }

    private ProductIngredientGroup findProductItemGroup(Long key) {
        return ingredientGroups
                .stream()
                .filter(productIngredientGroup -> productIngredientGroup.getIngredientGroup().getId().equals(key))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
