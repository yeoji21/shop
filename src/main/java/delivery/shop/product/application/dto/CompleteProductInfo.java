package delivery.shop.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import delivery.shop.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CompleteProductInfo {
    private long productId;
    private String productName;
    private Money productPrice;
    private List<IngredientGroupInfo> ingredientGroupInfos;
    private List<OptionGroupInfo> optionGroupInfos;

    @Builder @QueryProjection
    public CompleteProductInfo(long productId,
                               String productName,
                               Money productPrice,
                               List<IngredientGroupInfo> ingredientGroupInfos,
                               List<OptionGroupInfo> optionGroupInfos) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.ingredientGroupInfos = ingredientGroupInfos;
        this.optionGroupInfos = optionGroupInfos;
    }

    @QueryProjection
    public CompleteProductInfo(long productId, String productName, Money productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public void setIngredientGroupInfos(List<IngredientGroupInfo> ingredientGroupInfos) {
        this.ingredientGroupInfos = ingredientGroupInfos;
    }

    public void setOptionGroupInfos(List<OptionGroupInfo> optionGroupInfos) {
        this.optionGroupInfos = optionGroupInfos;
    }
}
