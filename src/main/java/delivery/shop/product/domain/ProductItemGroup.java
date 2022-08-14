package delivery.shop.product.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductItemGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_group_id")
    private ItemGroup itemGroup;

    public ProductItemGroup(Product product, ItemGroup itemGroup) {
        this.product = product;
        this.itemGroup = itemGroup;
    }

    protected boolean equalItemGroup(ItemGroup itemGroup) {
        return this.itemGroup.equals(itemGroup);
    }

    public Money verify(Set<Item> items) {
        return itemGroup.verify(items);
    }
}
