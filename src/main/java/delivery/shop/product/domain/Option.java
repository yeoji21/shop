package delivery.shop.product.domain;


import com.mysema.commons.lang.Assert;
import delivery.shop.common.domain.Money;
import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Option{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @Column(name = "price", nullable = false)
    private Money price;

    @Builder
    public Option(String name,
                  Money price) {
        Assert.hasText(name, "name");
        Assert.notNull(price, "price");

        this.name = name;
        this.price = price;
    }
}
