package it.uniroma3.SiwCatalog.service;

import it.uniroma3.SiwCatalog.model.Comment;
import it.uniroma3.SiwCatalog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findAll() { return commentRepository.findAll(); }

    public Optional<Comment> findById(Long id) { return commentRepository.findById(id); }

    public List<Comment> findByProduct(Long productId) { return commentRepository.findByProductId(productId); }

    public Comment save(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public void delete(Long id) { commentRepository.deleteById(id); }
}
