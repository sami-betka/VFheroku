package videofutur.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import videofutur.model.Cart;
import videofutur.model.CartProduct;
import videofutur.model.Category;
import videofutur.model.Product;
import videofutur.model.UserAccount;
import videofutur.repository.CartRepository;
import videofutur.repository.ProductRepository;
import videofutur.repository.UserAccountRepository;

@RequestMapping("/products")
@Controller
public class ProductController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	CartRepository cartRepository;

	// Customer
	// Obtenir les détails d'un produit
	@GetMapping("/details/{id}")
	public String getProduct(@PathVariable("id") Long id, Model model, Principal principal) {

		model.addAttribute("product", productRepository.findById(id).get());

		navbarAttributes(model, principal);

		return "productDetails";
	}

	// Customer
	// Obtenir la liste des produits
	@GetMapping("/list")
	public String getProductList(Model model, Principal principal,
			@RequestParam(name="page", defaultValue = "0")int p,
			@RequestParam(name="motCle", defaultValue = "")String mc
			,@RequestParam(name="category", defaultValue = "")Category category
			) {
		
//		Page<Product>pageProducts=productRepository.findAll(PageRequest.of(p, 5));
		Page<Product>pageProducts=productRepository.findProducts("%"+mc+"%",PageRequest.of(p, 12));
//		Page<Product>pageProducts=productRepository.findProductsByCategory(category ,"%"+mc+"%",PageRequest.of(p, 12));

		
		int pagesCount = pageProducts.getTotalPages();
		int[]pages = new int[pagesCount];	
		for(int i=0; i<pagesCount; i++) pages[i]=i;
		model.addAttribute("pages", pages);
		model.addAttribute("pageProducts", pageProducts);
		model.addAttribute("currentpage", p);
		model.addAttribute("motCle", mc);

        navbarAttributes(model, principal);
	
		return "home";

	}

	// Customer
	// Obtenir la liste des produits par catégorie
	@GetMapping("/list/category/{category}")
	public String getProductListByCategory(
			@PathVariable("category") Category category,
			Model model, Principal principal,
			@RequestParam(name="page", defaultValue = "0")int p,
			@RequestParam(name="motCle", defaultValue = "")String mc
//			@RequestParam(name="category", defaultValue = "")Category category
			) {

//		model.addAttribute("productsby", productRepository.findByCategory(Category.valueOf(categoryName)));

//		Page<Product>pageProducts=productRepository.findAll(PageRequest.of(p, 5));
//		Page<Product>pageProducts=productRepository.findProducts("%"+mc+"%",PageRequest.of(p, 12));
//		Page<Product>pageProducts=productRepository.findProductsByCategory(Category.valueOf(category) ,"%"+mc+"%",PageRequest.of(p, 12));
		Page<Product>pageProducts=productRepository.findProductsByCategory(category ,"%"+mc+"%",PageRequest.of(p, 12));

		
		int pagesCount = pageProducts.getTotalPages();
		int[]pages = new int[pagesCount];	
		for(int i=0; i<pagesCount; i++) pages[i]=i;
		model.addAttribute("pages", pages);
		model.addAttribute("pageProducts", pageProducts);
		model.addAttribute("currentpage", p);
		model.addAttribute("motCle", mc);
		
		navbarAttributes(model, principal);

		return "byCategory";
	}

	// Customer
	// Obtenir la liste des produits par année
	@GetMapping("/list/year/{year}")
	public String getProductListByYear(@PathVariable("year") Integer year, Model model, Principal principal,
			@RequestParam(name="page", defaultValue = "0")int p,
			@RequestParam(name="motCle", defaultValue = "")String mc) {

//		model.addAttribute("productsby", productRepository.findByYear(year));
		
//		Page<Product>pageProducts=productRepository.findAll(PageRequest.of(p, 5));
//		Page<Product>pageProducts=productRepository.findProducts("%"+mc+"%",PageRequest.of(p, 12));
//		Page<Product>pageProducts=productRepository.findProductsByCategory(Category.valueOf(category) ,"%"+mc+"%",PageRequest.of(p, 12));
		Page<Product>pageProducts=productRepository.findProductsByYear(year ,"%"+mc+"%",PageRequest.of(p, 12));

		
		int pagesCount = pageProducts.getTotalPages();
		int[]pages = new int[pagesCount];	
		for(int i=0; i<pagesCount; i++) pages[i]=i;
		model.addAttribute("pages", pages);
		model.addAttribute("pageProducts", pageProducts);
		model.addAttribute("currentpage", p);
		model.addAttribute("motCle", mc);

		navbarAttributes(model, principal);

		return "byYear";
	}

	// Other methods...

	private List<Integer> getYears() {

		List<Integer> years = new ArrayList<Integer>();
		for (Product p : productRepository.findAll()) {
			if (!years.contains(p.getYear())) {
				years.add(p.getYear());
			}
		}

		return years;
	}

		private void navbarAttributes(Model model, Principal principal) {

			if (principal == null) {
				Cart cart = new Cart();
				cart.setQuantity(0);
				model.addAttribute("cart", cart);
				model.addAttribute("products", productRepository.findAll());
				model.addAttribute("categories", Category.values());
				model.addAttribute("years", getYears());
			} else {
				
				UserAccount user = userAccountRepository.findByUserName(principal.getName());
				Cart cart = cartRepository.findById(user.getCart().getId()).get();
				System.out.println(cart.getId());
				
				if(cart.getCartProducts() != null) {	int sumQuantity = 0;
				for (CartProduct cp : cart.getCartProducts()) {
					sumQuantity = sumQuantity + cp.getQuantity();
				}
				cart.setQuantity(sumQuantity);
				cartRepository.save(cart);

				model.addAttribute("cart", cart);
				}
				model.addAttribute("products", productRepository.findAll());
				model.addAttribute("categories", Category.values());
				model.addAttribute("years", getYears());
			}
		}
	}
