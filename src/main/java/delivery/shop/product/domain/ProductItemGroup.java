package delivery.shop.product.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    protected boolean hasItemGroup(ItemGroup itemGroup) {
        return this.itemGroup.equals(itemGroup);
    }

    public Money placeWithItems(List<Item> items) {
        if(items.isEmpty()) return Money.ZERO;
        return itemGroup.calculateItemAmount(items);
    }
}
