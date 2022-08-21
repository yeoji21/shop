package delivery.shop.product.domain;

import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OptionGroup{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_select_count", nullable = false)
    private int maxSelectCount;

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionInGroup> options = new ArrayList<>();

    public OptionGroup(String name, int maxSelectCount) {
        Assert.hasText(name, "name");
        Assert.notNull(maxSelectCount, "maxSelectCount");

        this.name = name;
        this.maxSelectCount = maxSelectCount;
    }

    public void validateOptions(List<Option> options) {
        if(options.size() > maxSelectCount)
            throw new IllegalArgumentException();
    }

    public void addOption(Option option) {
        options.add(new OptionInGroup(this, option));
    }
}
