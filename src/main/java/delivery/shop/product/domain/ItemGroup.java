package delivery.shop.product.domain;

import delivery.shop.common.domain.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @OneToMany(mappedBy = "itemGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemInGroup> items = new ArrayList<>();

    public ItemGroup(String name, int minCount, int maxCount) {
        this.name = name;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    public void addItem(Item item) {
        if(item.getId() == null) throw new IllegalArgumentException();
        items.add(new ItemInGroup(this, item));
    }

    public Money verify(Set<Item> items) {
        int size = items.size();
        if(minCount > size || size > maxCount)
            throw new IllegalArgumentException();

        return items.stream()
                .filter(i ->
                        this.items.stream().anyMatch(ig -> ig.getItem().equals(i))
                )
                .map(Item::getPrice)
                .reduce(Money.ZERO, Money::add);
    }

}
