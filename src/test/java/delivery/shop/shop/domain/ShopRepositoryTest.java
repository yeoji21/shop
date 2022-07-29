package delivery.shop.shop.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.shop.common.config.JpaQueryFactoryConfig;
import delivery.shop.common.domain.Money;
import delivery.shop.file.domain.File;
import delivery.shop.file.domain.FileName;
import delivery.shop.shop.application.dto.response.ShopSimpleInfo;
import delivery.shop.shop.infra.JpaShopRepository;
import delivery.shop.shop.infra.ShopQueryDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({JpaQueryFactoryConfig.class, JpaShopRepository.class, ShopQueryDao.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ShopRepositoryTest {
    @PersistenceContext EntityManager em;
    @Autowired protected ShopRepository shopRepository;
    @Autowired protected ShopQueryDao shopQueryDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void test() throws Exception{
        for (int i = 0; i < 100; i++) {
            File thumbnail = File.builder()
                    .fileName(FileName
                            .builder()
                            .originalFileName("original")
                            .storedFileName("stored")
                            .build())
                    .filePath("file path " + i)
                    .build();
            em.persist(thumbnail);

            Shop shop = Shop.builder()
                    .shopName(i + " shop")
                    .minOrderAmount(new Money(10_000))
                    .shopThumbnailFileId(thumbnail.getId())
                    .build();

            shop.addDeliveryFee(
                    OrderAmountDeliveryFee.builder()
                            .orderAmount(new Money(20_000))
                            .fee(new Money(2000))
                            .build());

            shop.addDeliveryFee(
                    OrderAmountDeliveryFee.builder()
                            .orderAmount(new Money(15_000))
                            .fee(new Money(3000))
                            .build());

            shopRepository.save(shop);
        }
    }

    @Test
    void 가게_간략정보_리스트로_조회() throws Exception{
        List<ShopSimpleInfo> infoList = shopQueryDao.findAllSimpleInfo();
        assertThat(infoList.size()).isEqualTo(100);
    }

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
                .minOrderAmount(new Money(10_000))
                .shopThumbnailFileId(thumbnail.getId())
                .build();

        shop.addDeliveryFee(
                OrderAmountDeliveryFee.builder()
                        .orderAmount(new Money(20_000))
                        .fee(new Money(2000))
                        .build());

        shop.addDeliveryFee(
                OrderAmountDeliveryFee.builder()
                        .orderAmount(new Money(15_000))
                        .fee(new Money(3000))
                        .build());

        Shop savedShop = shopRepository.save(shop);

        em.flush();
        em.clear();

        //when
        ShopSimpleInfo info = shopQueryDao.findSimpleInfo(savedShop.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(info.getShopName()).isEqualTo(shop.getShopName());
        assertThat(info.getMinOrderAmount()).isEqualTo(shop.getMinOrderAmount().toInt());
        assertThat(info.getThumbnail()).isEqualTo(thumbnail.getFilePath());

        System.out.println(objectMapper.writeValueAsString(info));
    }
}