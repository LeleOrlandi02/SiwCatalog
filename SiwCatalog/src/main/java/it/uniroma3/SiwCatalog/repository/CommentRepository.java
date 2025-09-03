package it.uniroma3.SiwCatalog.repository;

import it.uniroma3.SiwCatalog.model.Comment;
import it.uniroma3.SiwCatalog.model.Product;
import it.uniroma3.SiwCatalog.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProduct(Product product);
    List<Comment> findByAuthorUsername(String username);
    Optional<Comment> findByAuthorAndProduct(User author, Product product);
}

