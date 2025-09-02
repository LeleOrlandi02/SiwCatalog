package it.uniroma3.SiwCatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Only admins can manage products
                .requestMatchers("/products/new", "/products/edit/**", "/products/delete/**").hasRole("ADMIN")
                // Authenticated users can comment
                .requestMatchers("/comments/**", "home/**", "products/**").authenticated()
                // Public pages
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.loginPage("/login")
            .defaultSuccessUrl("/home", true))  // redirect to authenticated home after login.permitAll())    // default login form (the one you see)
            .logout(Customizer.withDefaults())   // default logout page
            .userDetailsService(userDetailsService); 
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
