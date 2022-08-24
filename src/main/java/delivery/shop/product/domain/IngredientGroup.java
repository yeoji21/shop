package delivery.shop.product.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IngredientGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private int minCount;
    private int maxCount;
    @OneToMany(mappedBy = "itemGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientInGroup> ingredients = new ArrayList<>();

    public IngredientGroup(String name, int minCount, int maxCount) {
        this.name = name;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    public void addItem(Ingredient ingredient) {
        if(ingredient.getId() == null) throw new IllegalArgumentException();
        ingredients.add(new IngredientInGroup(this, ingredient));
    }

    public Money calculateItemAmount(List<Ingredient> ingredients) {
        selectionCountCheck(ingredients);

        return ingredients.stream()
                .map(Ingredient::getPrice)
                .reduce(Money.ZERO, Money::add);
    }

    private void selectionCountCheck(List<Ingredient> ingredients) {
        int size = ingredients.size();
        if(minCount > size || size > maxCount)
            throw new IllegalArgumentException();
    }

}