package delivery.shop.shop.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.shop.category.domain.Category;
import delivery.shop.common.config.JpaQueryFactoryConfig;
import delivery.shop.common.domain.Money;
import delivery.shop.file.domain.File;
import delivery.shop.file.domain.FileName;
import delivery.shop.shop.application.dto.response.ShopDetailInfo;
import delivery.shop.shop.application.dto.response.ShopSimpleInfo;
import delivery.shop.shop.infra.JpaShopRepository;
import delivery.shop.shop.infra.ShopQueryDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
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

    @Test @Rollback(value = false)
    void 가게_카테고리_함께_저장() throws Exception{
        Category chicken = new Category("치킨");
        Category pizza = new Category("피자");
        Category coffee = new Category("커피");
        em.persist(chicken);
        em.persist(pizza);
        em.persist(coffee);

        Shop shop = getShopWithoutCategory();

        shop.includeCategory(chicken.getId());
        shop.includeCategory(pizza.getId());
        shop.includeCategory(coffee.getId());

        shop.includeCategory(pizza.getId());

        em.persist(shop);
    }

    @Test @Rollback(value = false)
    void 가게에_카테고리_저장_후_삭제() throws Exception{

        Shop shop = em.find(Shop.class, 1L);

        // shopId에 해당하는 데이터 모두 제거 후 다시 insert 하는 것 확인
        shop.getCategoryIds().remove(0);

        em.flush();
        em.clear();
    }

    @Test @Rollback(value = false)
    void 가게_메뉴_저장() throws Exception{
        Shop shop = getShopWithoutCategory();
        shop.addMenu(new Menu("메뉴 1", "메뉴 1입니다~!"));
        shop.addMenu(new Menu("메뉴 2", "메뉴 2입니다~!"));
        shop.addMenu(new Menu("메뉴 3", "메뉴 3입니다~!"));
        em.persist(shop);
    }


    private Shop getShopWithoutCategory() {
        File thumbnail = getThumbnail();
        Shop shop = Shop.builder()
                .shopName("A shop")
                .minOrderAmount(new Money(10_000))
                .shopThumbnailFileId(thumbnail.getId())
                .introduction("안녕하세용")
                .phoneNumber(new PhoneNumber("010-1234-1245"))
                .location(new ShopLocation("xxxx-xxx", 1L))
                .businessTimeInfo(new BusinessTimeInfo("매일 10시~21시", "매주 첫째주 일요일"))
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
        return shop;
    }

    @Test
    void 가게_간략정보_리스트로_조회() throws Exception{
        List<ShopSimpleInfo> infoList = shopQueryDao.findAllSimpleInfo();
        assertThat(infoList.size()).isEqualTo(100);
    }

    @Test
    void 단건_가게_간략정보_조회() throws Exception{
        //given
        File thumbnail = getThumbnail();
        Shop savedShop = getShop(thumbnail);

        //when
        ShopSimpleInfo info = shopQueryDao.findSimpleInfo(savedShop.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(info.getShopId()).isEqualTo(savedShop.getId());
        assertThat(info.getShopName()).isEqualTo(savedShop.getShopName());
        assertThat(info.getMinOrderAmount()).isEqualTo(savedShop.getMinOrderAmount().toInt());
        assertThat(info.getThumbnail()).isEqualTo(thumbnail.getFilePath());

        System.out.println(objectMapper.writeValueAsString(info));
    }

    @Test
    void 단건_가게_상세정보_조회() throws Exception{
        //given
        File thumbnail = getThumbnail();
        Shop savedShop = getShop(thumbnail);

        //when
        ShopDetailInfo info = shopQueryDao.findDetailInfo(savedShop.getId())
                .orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(info.getShopId()).isEqualTo(savedShop.getId());
        assertThat(info.getShopName()).isEqualTo(savedShop.getShopName());
        assertThat(info.getMinOrderAmount()).isEqualTo(savedShop.getMinOrderAmount().toInt());
        assertThat(info.getDayOff()).isEqualTo(savedShop.getBusinessTimeInfo().getDayOff());
        assertThat(info.getStreetAddress()).isEqualTo(savedShop.getLocation().getStreetAddress());

        System.out.println(objectMapper.writeValueAsString(info));
    }


    private Shop getShop(File thumbnail) {
        Shop shop = Shop.builder()
                .shopName("A shop")
                .minOrderAmount(new Money(10_000))
                .shopThumbnailFileId(thumbnail.getId())
                .introduction("안녕하세용")
                .phoneNumber(new PhoneNumber("010-1234-1245"))
                .location(new ShopLocation("xxxx-xxx", 1L))
                .businessTimeInfo(new BusinessTimeInfo("매일 10시~21시", "매주 첫째주 일요일"))
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
        return savedShop;
    }

    private File getThumbnail() {
        File thumbnail = File.builder()
                .fileName(FileName
                        .builder()
                        .originalFileName("original")
                        .storedFileName("stored")
                        .build())
                .filePath("file path")
                .build();
        em.persist(thumbnail);
        return thumbnail;
    }
}