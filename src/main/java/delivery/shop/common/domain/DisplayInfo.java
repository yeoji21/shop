package delivery.shop.common.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DisplayInfo {
    @Column(name = "is_display")
    private boolean isDisplay;

    @Column(name ="display_order")
    private int displayOrder;

    public DisplayInfo(int displayOrder) {
        this.displayOrder = displayOrder;
        this.isDisplay = true;
    }
}
