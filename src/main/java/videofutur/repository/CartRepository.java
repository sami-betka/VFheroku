package videofutur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import videofutur.model.Cart;
import videofutur.model.UserAccount;

public interface CartRepository extends JpaRepository<Cart, Long>{
	
	 Cart findByUser(UserAccount user);
	 
	 void deleteByUser(UserAccount user);

}
