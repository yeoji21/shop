package delivery.shop.product.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ItemGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private int minCount;
    private int maxCount;

    public ItemGroup(String name, int minCount, int maxCount) {
        this.name = name;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }
}
