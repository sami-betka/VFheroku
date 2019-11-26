package videofutur.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import videofutur.model.Cart;
import videofutur.model.CartProduct;
import videofutur.model.Category;
import videofutur.model.Product;
import videofutur.model.UserAccount;
import videofutur.model.UserRole;
import videofutur.repository.CartRepository;
import videofutur.repository.ProductRepository;
import videofutur.repository.UserAccountRepository;
import videofutur.repository.UserRoleRepository;
import videofutur.utils.WebUtils;
 
@Controller
public class MainController {
	
	@Autowired
	UserAccountRepository userAccountRepository;
	
	@Autowired
	UserRoleRepository userRoleRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ProductRepository productRepository;
 
    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model, Principal principal) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        navbarAttributes(model, principal);
//        return "welcomePage";
		return "redirect:/products/list";

    }
 
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        
        navbarAttributes(model, principal);
         
        return "home";
    }
 
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model, Principal principal) {
 
    	navbarAttributes(model, principal);
//         return "loginPage";
        return "loginForm";

    }
    
    @RequestMapping(value = "/userAccountInfo", method = RequestMethod.GET)
    public String loginSuccess(Model model, Principal principal) {
    	
    	UserAccount user = userAccountRepository.findByUserName(principal.getName());
    	for(UserRole userRole: userRoleRepository.findAll()) {
    		if(userRole.getAppRole().getRoleId() ==1 && userRole.getUser().getUserId()==user.getUserId()) {
    			return "redirect:/administrate";
    		}
    	}
		return "redirect:/products/list";
    }
    
    @RequestMapping(value = "/administrate", method = RequestMethod.GET)
    public String adminGate(Model model, Principal principal) {
    	
    	navbarAttributes(model, principal);
 
        return "adminGate";
    }
 
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model, Principal principal) {
        model.addAttribute("title", "Logout");
        navbarAttributes(model, principal);
		return "redirect:/products/list";

//        return "logoutSuccessfulPage";
    }
 
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
 
        // After user login successfully.

		model.addAttribute("products", productRepository.findAll());

              navbarAttributes(model, principal);
 
          return "home";
    }
 
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {
 
        if (principal != null) {

 
            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
 
        }
 
        return "403Page";
    }
    
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
