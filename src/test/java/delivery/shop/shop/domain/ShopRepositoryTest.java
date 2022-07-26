package delivery.shop.shop.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.shop.common.config.JpaQueryFactoryConfig;
import delivery.shop.common.domain.Money;
import delivery.shop.file.domain.File;
import delivery.shop.file.domain.FileName;
import delivery.shop.shop.application.dto.response.ShopSimpleInfo;
import delivery.shop.shop.infra.JpaShopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@Import({JpaQueryFactoryConfig.class, JpaShopRepository.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ShopRepositoryTest {
    @PersistenceContext EntityManager em;
    @Autowired protected ShopRepository shopRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 단건_가게_간략정보_조회() throws Exception{
        //given
        File thumbnail = File.builder()
                .fileName(FileName
                        .builder()
                        .originalFileName("original")
                        .storedFileName("stored")
                        .build())
                .filePath("file path")
                .build();
        em.persist(thumbnail);

        Shop shop = Shop.builder()
                .shopName("A shop")
                .minOrderPrice(new Money(10_000))
                .shopThumbnail(thumbnail)
                .build();

        Shop savedShop = shopRepository.save(shop);

        //when
        ShopSimpleInfo info = shopRepository.findSimpleInfo(savedShop.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(info.getShopName()).isEqualTo(shop.getShopName());
        assertThat(info.getMinOrderPrice()).isEqualTo(shop.getMinOrderPrice().toInt());
        assertThat(info.getThumbnail()).isEqualTo(shop.getShopThumbnail().getFilePath());

        System.out.println(objectMapper.writeValueAsString(info));
    }
}