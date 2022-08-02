package delivery.shop.shop.infra;

import delivery.shop.shop.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

interface ShopDao extends JpaRepository<Shop, Long>{

}
