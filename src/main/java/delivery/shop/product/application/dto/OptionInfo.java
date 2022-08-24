package delivery.shop.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import delivery.shop.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OptionInfo {
    private long optionId;
    private String name;
    private Money money;

    @Builder @QueryProjection
    public OptionInfo(long optionId,
                      String name,
                      Money money) {
        this.optionId = optionId;
        this.name = name;
        this.money = money;
    }
}
