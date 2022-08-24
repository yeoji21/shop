package delivery.shop.product.domain;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IngredientInGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_group_id")
    private IngredientGroup ingredientGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Ingredient ingredient;

    public IngredientInGroup(IngredientGroup ingredientGroup, Ingredient ingredient) {
        this.ingredientGroup = ingredientGroup;
        this.ingredient = ingredient;
    }

    public boolean hasItem(Ingredient ingredient) {
        return this.ingredient.equals(ingredient);
    }
}
