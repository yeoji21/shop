package delivery.shop.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import delivery.shop.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IngredientInfo {
    private long ingredientId;
    private String name;
    private Money price;

    @Builder @QueryProjection
    public IngredientInfo(long ingredientId,
                          String name,
                          Money price) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.price = price;
    }
}
