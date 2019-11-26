package videofutur.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import videofutur.model.Cart;
import videofutur.model.CartProduct;
import videofutur.model.Category;
import videofutur.model.Product;
import videofutur.model.UserAccount;
import videofutur.repository.CartRepository;
import videofutur.repository.ProductRepository;
import videofutur.repository.UserAccountRepository;

@RequestMapping("/admin")
@Controller
public class AdminController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	CartRepository cartRepository;

//	private LocalDate year;
	
	@GetMapping(value = "/administrate")
	public String GetAdminPage(Model model, Principal principal) {
		
		if (principal == null) {
			return "redirect:/login";
		}

		navbarAttributes(model, principal);
		return "adminMenu";
	}
	
	@GetMapping(value = "/user-list")
	public String GetUserList(Model model, Principal principal) {
		
		if (principal == null) {
			return "redirect:/login";
		}
		
		model.addAttribute("userlist", userAccountRepository.findAll());

		navbarAttributes(model, principal);
		return "userList";
	}


	// Admin
	// Ajouter un produit
	@GetMapping(value = "/add-product")
	public String addProduct(Model model, Principal principal) {
	
//		if (principal == null) {
//			return "redirect:/login";
//		}

		List<Integer> allYears = new ArrayList<>();

		for (int i = 1900; i < 2020; i++) {
			allYears.add(i);
		}

		model.addAttribute("product", new Product());
		model.addAttribute("allYears", allYears);

		navbarAttributes(model, principal);
		return "addProduct";

	}

	@PostMapping(value = "/save-product")
	public String saveProduct(@Valid Product product, BindingResult bindingresult)
			throws IllegalStateException, IOException {

		if (bindingresult.hasErrors()) {
			return "redirect:/admin/add-product";
		}

//		if(!(file.isEmpty())) {
//			product.setPhoto(file.getOriginalFilename());
//			file.transferTo(new File("C:\\Users\\s.betka\\Desktop\\sco\\"+file.getOriginalFilename()));
//		}
		productRepository.save(product);
		return "redirect:/products/list";

	}
	// Admin
	// Modifier un produit

	@RequestMapping(value = "/edit-product/{id}")
	public String editProduct(@PathVariable("id") Long id, Model model, Principal principal) {
		
		if (principal == null) {
			return "redirect:/login";
		}

		Product product = productRepository.findById(id).get();

		model.addAttribute("product", product);

		navbarAttributes(model, principal);
		return "updateProduct";

	}

	@PostMapping(value = "/update-product")
	public String updateProduct(@Valid Product product, BindingResult bindingresult) {
		if (bindingresult.hasErrors()) {
			return "updateProduct";
		}

		productRepository.save(product);
		return "redirect:/products/list";

	}
	// Admin
	// Supprimer un produit

	@RequestMapping(value = "/delete-product/{id}")
	public String deleteProduct(@PathVariable("id") Long id, Principal principal) {
		
		if (principal == null) {
			return "redirect:/login";
		}

		productRepository.deleteById(id);
		return "redirect:/products/list";

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

			if (cart.getCartProducts() != null) {
				int sumQuantity = 0;
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
