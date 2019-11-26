package videofutur.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {
	
	@Id
	private Long id;
	
	private Long cartId;
	
	private String name;

	private String description;

	private BigDecimal price;

	private Integer year;

	private Integer stock;

	private Category category;

	private Integer length;
	
	private String picture;

	@ManyToMany
	private List<Artist> actors = new ArrayList<>();

//	@ManyToOne
//	private Artist director;
	
	private String directorName;	
	
	int quantity;
	

}
