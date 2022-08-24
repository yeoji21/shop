package delivery.shop.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OptionGroupInfo {
    private long optionGroupId;
    private String name;
    private int maxSelectCount;
    private List<OptionInfo> optionInfos;

    @Builder @QueryProjection
    public OptionGroupInfo(long optionGroupId,
                           String name,
                           int maxSelectCount,
                           List<OptionInfo> optionInfos) {
        this.optionGroupId = optionGroupId;
        this.name = name;
        this.maxSelectCount = maxSelectCount;
        this.optionInfos = optionInfos;
    }
}
