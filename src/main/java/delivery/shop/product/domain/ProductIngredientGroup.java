package delivery.shop.product.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductIngredientGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_group_id")
    private IngredientGroup ingredientGroup;

    public ProductIngredientGroup(Product product, IngredientGroup ingredientGroup) {
        this.product = product;
        this.ingredientGroup = ingredientGroup;
    }

    protected boolean hasItemGroup(IngredientGroup ingredientGroup) {
        return this.ingredientGroup.equals(ingredientGroup);
    }

    public Money placeWithItems(List<Ingredient> ingredients) {
        if(ingredients.isEmpty()) return Money.ZERO;
        return ingredientGroup.calculateItemAmount(ingredients);
    }
}
