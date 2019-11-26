package videofutur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import videofutur.model.Cart;
import videofutur.model.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{
	
      UserAccount findByUserName (String userName);
      
      UserAccount findByCart (Cart cart);


}
