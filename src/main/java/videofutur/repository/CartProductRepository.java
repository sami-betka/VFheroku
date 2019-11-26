package videofutur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import videofutur.model.CartProduct;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long>{

}
