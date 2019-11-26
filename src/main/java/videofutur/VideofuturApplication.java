package videofutur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import videofutur.repository.ProductRepository;

@SpringBootApplication
public class VideofuturApplication {
	

	
	public static void main(String[] args) {
		ApplicationContext ctx=SpringApplication.run(VideofuturApplication.class, args);
		
		ProductRepository productRepository=ctx.getBean(ProductRepository.class);
		
//		productRepository.save(new Product(
//				"hulk",
//				"La bete est de retour bla bla bla bla ",
//				BigDecimal.valueOf(19.99), 
//				1999, 
//				12, 
//				Category.ACTION,
//				123, 
//				"Lee",
//				"https://media2.woopic.com/api/v1/images/174%2Fcine"
//				+ "movies%2Fff4%2F311%2F49f5f4beb783fda6b0817d1f83%2Fmovies-114820-3.jpg?format=822x700&quality=85"));
//		
//		productRepository.save(new Product(
//				"Chucky",
//				"hhhhhhhhhhhhhhhhhhhh ",
//				BigDecimal.valueOf(19.99), 
//				1999, 
//				12, 
//				Category.HORREUR,
//				123, 
//				"cvb",
//				"https://freakingeek.com/wp-content/uploads/2017/10/LeRetourDeChucky-Banniere02-800x445.jpg"));
//		
//		productRepository.save(new Product(
//				"Les visiteurs",
//				"Ok, Dingue Godeffroy le hardi",
//				BigDecimal.valueOf(19.99), 
//				1515, 
//				178, 
//				Category.COMEDIE,
//				123, 
//				"ftgr",
//				"https://media.senscritique.com/media/000011284534/source_big/Les_Visiteurs.jpg"));

		
	}

}
