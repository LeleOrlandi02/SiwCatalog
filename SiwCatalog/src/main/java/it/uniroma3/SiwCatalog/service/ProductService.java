package it.uniroma3.SiwCatalog.service;

import it.uniroma3.SiwCatalog.model.Product;
import it.uniroma3.SiwCatalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() { return productRepository.findAll(); }

    public Optional<Product> findById(Long id) { return productRepository.findById(id); }

    public List<Product> findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(String name, String type) { return productRepository.findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(name, type); }

     public List<Product> search(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(keyword, keyword);
        } else {
            return productRepository.findAll();
        }
    }
    
    public Product save(Product product) { return productRepository.save(product); }

    public void delete(Long id) { productRepository.deleteById(id); }
}
