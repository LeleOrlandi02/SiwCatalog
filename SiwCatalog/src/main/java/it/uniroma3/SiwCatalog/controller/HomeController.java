package it.uniroma3.SiwCatalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.SiwCatalog.model.Product;
import it.uniroma3.SiwCatalog.service.ProductService;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    // Public landing page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, 
                        @AuthenticationPrincipal UserDetails userDetails,
                        @RequestParam(required = false) String keyword) {
    
    List<Product> products = productService.findAll();

    if (keyword != null && !keyword.isEmpty()) {
        products = productService.search(keyword);
    } else {
        products = productService.findAll();                //sezione relativa al search dei prodotti
    }

    model.addAttribute("products", products);
    model.addAttribute("username", userDetails.getUsername());

    // Check if the user has ROLE_ADMIN
    boolean isAdmin = userDetails.getAuthorities().stream()
                         .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    model.addAttribute("isAdmin", isAdmin);

    return "home";  // home.html → shows product list
}

}