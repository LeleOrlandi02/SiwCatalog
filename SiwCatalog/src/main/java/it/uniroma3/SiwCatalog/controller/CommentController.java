package it.uniroma3.SiwCatalog.controller;

import it.uniroma3.SiwCatalog.model.Comment;
import it.uniroma3.SiwCatalog.model.Product;
import it.uniroma3.SiwCatalog.model.User;
import it.uniroma3.SiwCatalog.service.CommentService;
import it.uniroma3.SiwCatalog.service.ProductService;
import it.uniroma3.SiwCatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 🔹 NEW
import org.springframework.security.core.userdetails.UserDetails; // 🔹 NEW
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    // 🚨 This list page is optional (probably not needed in your flow)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("comments", commentService.findAll());
        return "comments/list";
    }

    // 🔹 CREATE COMMENT FORM
    @GetMapping("/new/{productId}")
    public String createForm(@PathVariable Long productId,
                             @AuthenticationPrincipal UserDetails userDetails, // 🔹 CHANGED
                             Model model) {
        Product product = productService.findById(productId).orElseThrow();
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        // 🔹 Check if user already commented on this product
        Optional<Comment> existing = commentService.getCommentByAuthorAndProduct(user, product);
        if (existing.isPresent()) {
            return "redirect:/products/" + productId; // already commented
        }

        Comment comment = new Comment();
        comment.setProduct(product);
        model.addAttribute("comment", comment);
        return "productDetails";
    }

    // 🔹 SAVE NEW COMMENT
    @PostMapping
    public String save(@ModelAttribute Comment comment,
                       @AuthenticationPrincipal UserDetails userDetails) { // 🔹 CHANGED
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Product product = productService.findById(comment.getProduct().getId()).orElseThrow();

        // ensure no duplicate
        if (commentService.getCommentByAuthorAndProduct(user, product).isEmpty()) {
            comment.setAuthor(user);
            comment.setProduct(product); // 🔹 prevent tampering
            commentService.save(comment);
        }
        return "redirect:/products/" + product.getId();
    }

    // 🔹 EDIT FORM
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails, // 🔹 NEW
                           Model model) {
        Comment comment = commentService.findById(id).orElseThrow();
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        // 🔹 Only author can edit their own comment
        if (!comment.getAuthor().equals(user)) {
            return "redirect:/products/" + comment.getProduct().getId();
        }

        model.addAttribute("comment", comment);
        return "productDetails";
    }

    // 🔹 UPDATE COMMENT
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Comment formComment,
                         @AuthenticationPrincipal UserDetails userDetails) { // 🔹 CHANGED
        Comment comment = commentService.findById(id).orElseThrow();
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        if (comment.getAuthor().equals(user)) { // 🔹 Only author can update
            comment.setContent(formComment.getContent()); // only update content
            commentService.save(comment);
        }
        return "redirect:/products/" + comment.getProduct().getId();
    }

    // elimina commento (autore o admin)
    @PostMapping("/{productId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long productId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        Product product = productService.findById(productId).orElseThrow();
        User user = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));;
        Comment comment = commentService.getCommentsByProduct(product).stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst().orElseThrow();

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (comment.getAuthor().equals(user) || isAdmin) {
            commentService.deleteComment(comment);
        }
        return "redirect:/products/" + productId;
    }
}
