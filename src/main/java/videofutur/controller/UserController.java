package videofutur.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import videofutur.model.Cart;
import videofutur.model.CartProduct;
import videofutur.model.Category;
import videofutur.model.Mail;
import videofutur.model.Order;
import videofutur.model.Product;
import videofutur.model.UserAccount;
import videofutur.model.UserRole;
import videofutur.repository.AppRoleRepository;
import videofutur.repository.CartProductRepository;
import videofutur.repository.CartRepository;
import videofutur.repository.OrderRepository;
import videofutur.repository.ProductRepository;
import videofutur.repository.UserAccountRepository;
import videofutur.repository.UserRoleRepository;
import videofutur.service.MailService;
import videofutur.utils.EncrytedPasswordUtils;

@RequestMapping("/users")
@Controller
public class UserController {

	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	AppRoleRepository appRoleRepository;

	@Autowired
	CartProductRepository cartProductRepository;

	@Autowired
	MailService mailService;

	@Autowired
	static EncrytedPasswordUtils encrytedPasswordUtils;

	// Obtenir les détails d'un client

	@GetMapping("/me/details")
	public String getUser(Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		// After user login successfully.
		UserAccount user = userAccountRepository.findByUserName(principal.getName());
		user.setFullName(user.getLastName().toUpperCase() + " " + user.getFirstName().toUpperCase());
		model.addAttribute("user", user);

		navbarAttributes(model, principal);

		return "userDetails";
	}

	// Ajouter un utilisateur

	@GetMapping(value = "/create-account")
	public String addUser(Model model, Principal principal) {

		model.addAttribute("user", new UserAccount());

		navbarAttributes(model, principal);

		return "createAccount";
	}

	@PostMapping("/save-user")
	public String saveUser(@Valid UserAccount user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "addUser";
		}

//		user.setEnabled(true);
		user.setEncrytedPassword(encrytedPasswordUtils.encrytePassword(user.getEncrytedPassword()));

		Cart cart = new Cart();
		user.setCart(cart);

		UserRole userRole = new UserRole(user, appRoleRepository.findById(2L).get());

		userAccountRepository.save(user);
		userRoleRepository.save(userRole);

		return "redirect:/products/list";

	}

	// Obtenir les détails de mon panier

	@GetMapping("/me/cart")
	public String getCart(Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		Cart cart = cartRepository.findById(userAccountRepository.findByUserName(principal.getName()).getCart().getId())
				.get();

		model.addAttribute("cart", cart);
		model.addAttribute("prods", cart.getCartProducts());
		model.addAttribute("prodsNumber", cart.getCartProducts().size());
		navbarAttributes(model, principal);

		return "myCart";
	}
	// Ajouter un produit à mon panier

	@GetMapping("/add-to-cart/{productId}/{view}")
	public String addProductToMyCart(@PathVariable("productId") Long productId, @PathVariable("view") String view,
			Principal principal, Model model) {

		if (principal == null) {
			return "redirect:/login";
		}

		UserAccount user = userAccountRepository.findByUserName(principal.getName());
		Cart cart = cartRepository.findById(user.getCart().getId()).get();

		Product product = productRepository.findById(productId).get();
		CartProduct cartProduct = null;
		if (!cartProductRepository.findById(productId).isPresent()) {
			cartProduct = convertToCartProducts(product, cart);
		} else {
			cartProduct = cartProductRepository.findById(productId).get();
		}

		if (!cart.getCartProducts().contains(cartProduct)) {
			cart.getCartProducts().add(cartProduct);
			cartProduct.setCartId(cart.getId());

		} else {

			for (int i = 0; i < cart.getCartProducts().size(); i++) {
				if (cart.getCartProducts().get(i).getId() == productId) {
					cart.getCartProducts().get(i).setQuantity(cart.getCartProducts().get(i).getQuantity() + 1);
				}
			}
		}

		int sumQuantity = 0;
		for (CartProduct cp : cart.getCartProducts()) {
			sumQuantity = sumQuantity + cp.getQuantity();
		}
		cart.setQuantity(sumQuantity);
		cartProductRepository.save(cartProduct);
		cartRepository.save(cart);

		navbarAttributes(model, principal);

		return "redirect:/products/list";

	}

	@GetMapping("/add-to-cart2/{productId}/{view}")
	public String addProductToMyCart2(@PathVariable("productId") Long productId, @PathVariable("view") String view,
			Principal principal, Model model) {

		if (principal == null) {
			return "redirect:/login";
		}

		UserAccount user = userAccountRepository.findByUserName(principal.getName());
		Cart cart = cartRepository.findById(user.getCart().getId()).get();

		Product product = productRepository.findById(productId).get();
		CartProduct cartProduct = null;
		if (!cartProductRepository.findById(productId).isPresent()) {
			cartProduct = convertToCartProducts(product, cart);
		} else {
			cartProduct = cartProductRepository.findById(productId).get();
		}

		if (!cart.getCartProducts().contains(cartProduct)) {
			cart.getCartProducts().add(cartProduct);
			cartProduct.setCartId(cart.getId());

		} else {

			for (int i = 0; i < cart.getCartProducts().size(); i++) {
				if (cart.getCartProducts().get(i).getId() == productId) {
					cart.getCartProducts().get(i).setQuantity(cart.getCartProducts().get(i).getQuantity() + 1);
				}
			}
		}

		int sumQuantity = 0;
		for (CartProduct cp : cart.getCartProducts()) {
			sumQuantity = sumQuantity + cp.getQuantity();
		}
		cart.setQuantity(sumQuantity);
		cartProductRepository.save(cartProduct);
		cartRepository.save(cart);

		navbarAttributes(model, principal);

		return "redirect:/users/me/cart";

	}

	// Retirer un article du panier

	@GetMapping("/me/cart/remove/{cartProductId}")
	public String removeFromCart(@PathVariable("cartProductId") Long cartProductId, Model model, Principal principal) {

		Cart cart = cartRepository.findById(userAccountRepository.findByUserName(principal.getName()).getCart().getId())
				.get();
		for (CartProduct cartProduct : cart.getCartProducts()) {
			if (cartProduct.getId() == cartProductId) {
				cartProduct.setQuantity(cartProduct.getQuantity() - 1);
			}
		}
		cartRepository.save(cart);
		navbarAttributes(model, principal);

		return "redirect:/users/me/cart";
	}

	// Valider le paiement

	@RequestMapping(value = "/to-payment")
	public String toPayment(Principal principal, Model model) {

		navbarAttributes(model, principal);

		return "payment";
	}

	@RequestMapping(value = "/validate-payment")
	public String validatePayment(Principal principal, Model model) {

		if (principal == null) {
			return "redirect:/login";
		}

		UserAccount user = userAccountRepository.findByUserName(principal.getName());
		Cart existingCart = user.getCart();

		Order order = new Order();
		order.setUser(user);
		BigDecimal sum = BigDecimal.ZERO;
		for (CartProduct cartProduct : existingCart.getCartProducts()) {

			sum = sum.add(cartProduct.getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity())));

		}
		order.setAmount(sum);
		order.setDate(LocalDate.now());
//		order.setProducts(existingCart.getCartProducts());
        user.getOrders().add(order);
		orderRepository.save(order);
		
		Cart cart = new Cart();
		cart.setUser(user);
		user.setCart(cart);

		userAccountRepository.save(user);
		for (CartProduct cp : cartProductRepository.findAll()) {
			if (cp.getCartId() == existingCart.getId()) {
				cartProductRepository.delete(cp);
			}
		}
		cartRepository.deleteById(existingCart.getId());

		Mail mail = new Mail();


		mail.setMailFrom("samtetedestup@gmail.com");
		mail.setMailTo("samtetedestup@gmail.com");
		mail.setMailSubject("Récapitulatif de commande");
		StringBuilder sb = new StringBuilder();
		mail.setMailContent(sb.toString());
		mailService.sendEmail(mail, principal);

		navbarAttributes(model, principal);

		return "paymentSuccess";
	}

	// Obtenir la liste de mes commandes

	@GetMapping("/me/orders")
	public String getMyOrders(Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("orders",
				orderRepository.findAllByUser(userAccountRepository.findByUserName(principal.getName())));
		navbarAttributes(model, principal);

		return "myOrders";
	}

	// Détails d'une commande
	@GetMapping("/me/order/details/{id}")
	public String getOrderDetails(@PathVariable("id") Long id, Model model, Principal principal) {

		if (principal == null) {
			return "redirect:/login";
		}

		model.addAttribute("order", orderRepository.findById(id).get());
		navbarAttributes(model, principal);

		return "orderDetails";
	}

	// Modifier les infos d'un client

	@RequestMapping(value = "/edit-user/{id}")
	public String editUser(@PathVariable("id") Long id, Model model, Principal principal) {

		UserAccount user = userAccountRepository.findById(id).get();

		model.addAttribute("user", user);

		navbarAttributes(model, principal);

		return "updateUser";

	}

	@PostMapping(value = "/update-user")
	public String updateUser(@Valid UserAccount user, BindingResult bindingresult) {
		if (bindingresult.hasErrors()) {
			return "updateUser";
		}
		userAccountRepository.save(user);
		return "redirect:/products/list";

	}

	// Admin
	// Supprimer un client

	@RequestMapping(value = "/delete-user/{id}")
	public String deleteUser(@PathVariable("id") Long id) {

		userAccountRepository.deleteById(id);
		return "redirect:/products/list";

	}
////////////////////////////////////////////Méthodes supplémentaires///////////////////////////////////////////////////////////

//	@RequestMapping(value = "/get-photo", produces=MediaType.IMAGE_JPEG_VALUE)
//	@ResponseBody
//	public byte[] getPhoto(Long id) {
//		
//		File f = new File(pathname)
//		
//		
//		return null;
//	}

	// Retourne l'ensemble des années disponibles
	private List<Integer> getYears() {

		List<Integer> years = new ArrayList<Integer>();
		for (Product p : productRepository.findAll()) {
			if (!years.contains(p.getYear())) {
				years.add(p.getYear());
			}
		}
		return years;
	}

	private CartProduct convertToCartProducts(Product product, Cart cart) {

		CartProduct cartProduct = new CartProduct();
		cartProduct.setId(product.getId());
		cartProduct.setCartId(cart.getId());
		cartProduct.setActors(product.getActors());
		cartProduct.setCategory(product.getCategory());
		cartProduct.setDescription(product.getDescription());
		cartProduct.setDirectorName(product.getDirectorName());
		cartProduct.setLength(product.getLength());
		cartProduct.setName(product.getName());
		cartProduct.setPicture(product.getPicture());
		cartProduct.setPrice(product.getPrice());
		cartProduct.setStock(product.getStock());
		cartProduct.setYear(product.getYear());
// 		cartProduct.setQuantity(1);
//		cartProductRepository.save(cartProduct);

		return cartProduct;
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