package videofutur.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

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
	
	public Product(String name, String description, BigDecimal price, Integer year, Integer stock, Category category,
			Integer length, String directorName, String picture) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.year = year;
		this.stock = stock;
		this.category = category;
		this.length = length;
		this.directorName = directorName;
		this.picture = picture;
	}

	private boolean rented;
	
	private String photo;

	// standard constructors / setters / getters / toString
}
