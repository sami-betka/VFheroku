package videofutur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import videofutur.model.Order;
import videofutur.model.UserAccount;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
	public List <Order> findAllByUser(UserAccount user);
	
}
