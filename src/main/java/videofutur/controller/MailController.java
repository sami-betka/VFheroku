package videofutur.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import videofutur.model.Cart;
import videofutur.model.CartProduct;
import videofutur.model.Category;
import videofutur.model.Mail;
import videofutur.model.Product;
import videofutur.model.UserAccount;
import videofutur.repository.CartRepository;
import videofutur.repository.ProductRepository;
import videofutur.repository.UserAccountRepository;
import videofutur.service.MailService;

@RequestMapping("/mail")
@Controller
public class MailController {

	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MailService mailService;

//	@PostMapping("/send")
	@GetMapping("/send")

	public String sendMail(
//			@Valid Mail m, BindingResult result, 
			Model model, Principal principal) {

//		if (result.hasErrors()) {
//			return "form";
//		}

		UserAccount user = userAccountRepository.findByUserName(principal.getName());

		Mail mail = new Mail();
      mail.setMailFrom(user.getEmail());
//      mail.setMailTo("vfutur@gmail.com");
//      mail.setMailSubject(m.getMailSubject());
//      mail.setMailContent(m.getMailContent());

//		mail.setMailFrom("samtetedestup@gmail.com");
		mail.setMailTo("samtetedestup@gmail.com");
		mail.setMailSubject("Spring Boot - Email Example");
		mail.setMailContent("Hello");

		mailService.sendEmail(mail, principal);

		navbarAttributes(model, principal);

		return "mailSuccess";

	}

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

			if (cart.getCartProducts() != null) {	int sumQuantity = 0;
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
