package it.uniroma3.SiwCatalog.service;

import it.uniroma3.SiwCatalog.model.User;
import it.uniroma3.SiwCatalog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() { return userRepository.findAll(); }

    public Optional<User> findById(Long id) { return userRepository.findById(id); }

    public Optional<User> findByUsername(String username) { return userRepository.findByUsername(username); }

    public User save(User user) { return userRepository.save(user); }

    public void delete(Long id) { userRepository.deleteById(id); }
}

