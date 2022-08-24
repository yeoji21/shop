package delivery.shop.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class IngredientGroupInfo {
    private long ingredientGroupId;
    private String name;
    private int minSelectCount;
    private int maxSelectCount;
    private List<IngredientInfo> ingredientInfos;

    @Builder @QueryProjection
    public IngredientGroupInfo(long ingredientGroupId,
                               String name,
                               int minSelectCount,
                               int maxSelectCount,
                               List<IngredientInfo> ingredientInfos) {
        this.ingredientGroupId = ingredientGroupId;
        this.name = name;
        this.minSelectCount = minSelectCount;
        this.maxSelectCount = maxSelectCount;
        this.ingredientInfos = ingredientInfos;
    }
}
