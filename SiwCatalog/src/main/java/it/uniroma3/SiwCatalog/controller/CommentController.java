package it.uniroma3.SiwCatalog.controller;

import it.uniroma3.SiwCatalog.model.Comment;
import it.uniroma3.SiwCatalog.model.Product;
import it.uniroma3.SiwCatalog.model.User;
import it.uniroma3.SiwCatalog.service.CommentService;
import it.uniroma3.SiwCatalog.service.ProductService;
import it.uniroma3.SiwCatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("comments", commentService.findAll());
        return "comments/list";
    }

    @GetMapping("/new/{productId}")
    public String createForm(@PathVariable Long productId, Model model) {
        Comment comment = new Comment();
        Product product = productService.findById(productId).orElseThrow();
        comment.setProduct(product);
        model.addAttribute("comment", comment);
        return "comments/form";
    }

    @PostMapping
    public String save(@ModelAttribute Comment comment, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        comment.setAuthor(user);
        commentService.save(comment);
        return "redirect:/products/" + comment.getProduct().getId();
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("comment", commentService.findById(id).orElseThrow());
        return "comments/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Comment comment) {
        comment.setId(id);
        commentService.save(comment);
        return "redirect:/products/" + comment.getProduct().getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Comment comment = commentService.findById(id).orElseThrow();
        Long productId = comment.getProduct().getId();
        commentService.delete(id);
        return "redirect:/products/" + productId;
    }
}
