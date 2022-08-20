package delivery.shop.product.domain;

import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OptionInGroup{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    public OptionInGroup(OptionGroup optionGroup, Option option) {
        Assert.notNull(optionGroup, "optionGroup");
        Assert.notNull(option, "option");

        this.optionGroup = optionGroup;
        this.option = option;
    }
}
