package it.uniroma3.SiwCatalog.controller;

import it.uniroma3.SiwCatalog.model.User;
import it.uniroma3.SiwCatalog.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    private final UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/account")
    public String accountPage(Model model) {
        // Ottieni l'utente loggato
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Carica dal database il tuo utente
        User user = (User) userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));

        model.addAttribute("user", user);
        return "account"; // account.html
    }
}
