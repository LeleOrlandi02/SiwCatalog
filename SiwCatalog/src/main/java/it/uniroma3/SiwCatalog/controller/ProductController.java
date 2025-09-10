package it.uniroma3.SiwCatalog.controller;

import it.uniroma3.SiwCatalog.model.Product;
import it.uniroma3.SiwCatalog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/home";
    }
    
    @GetMapping("/{id}")
    public String view(Model model,
                        @RequestParam(required = false) Long editId,
                        @AuthenticationPrincipal UserDetails userDetails,
                        @PathVariable Long id) {
        model.addAttribute("product", productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found")));
        model.addAttribute("editId", editId);

        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("isAdmin", false);
            model.addAttribute("username", "Guest");
        }

        return "productDetails";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id).orElseThrow());
        return "products/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Product product) {
        product.setId(id);
        productService.save(product);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
    productService.delete(id);
    return "redirect:/home";
}
}
