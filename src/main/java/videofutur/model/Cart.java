package videofutur.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
//@SequenceGenerator(name = "CART_SEQ_GENERATOR", sequenceName = "CART_SEQ", allocationSize = 1)
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cart  {


	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CART_SEQ_GENERATOR")
	@GeneratedValue(strategy = GenerationType.AUTO)


	private Long id;

	@OneToOne(mappedBy = "cart")
	private UserAccount user;

	@OneToMany(targetEntity = CartProduct.class)
	private List <CartProduct> cartProducts = new ArrayList<>();

	private Integer quantity;

	// standard constructors / setters / getters / toString
}