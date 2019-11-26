package videofutur.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import videofutur.model.Category;
import videofutur.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	public List<Product>findByCategory(Category category);
	
	@Query("select p from Product p where p.name like :x and p.category= :category")
	public Page<Product>findProductsByCategory(Category category, @Param("x") String mc, Pageable pageable);

	
	public List<Product>findByYear(Integer year);
	
	@Query("select p from Product p where p.name like :x and p.year= :year")
	public Page<Product>findProductsByYear(Integer year, @Param("x") String mc, Pageable pageable);
	
	@Query("select p from Product p where p.name like :x")
	public Page<Product>findProducts(@Param("x") String mc, Pageable pageable);
	
	


}
