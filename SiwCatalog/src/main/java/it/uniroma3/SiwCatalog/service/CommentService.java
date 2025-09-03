package it.uniroma3.SiwCatalog.service;

import it.uniroma3.SiwCatalog.model.Comment;
import it.uniroma3.SiwCatalog.model.Product;
import it.uniroma3.SiwCatalog.model.User;
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

    public List<Comment> getCommentsByProduct(Product product) {
        return commentRepository.findByProduct(product);
    }
    
    public Optional<Comment> getCommentByAuthorAndProduct(User author, Product product) {
        return commentRepository.findByAuthorAndProduct(author, product);
    }
    
    public Comment addComment(User author, Product product, String content) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setProduct(product);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
    
    public Comment save(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Comment updateComment(Comment comment, String newContent) {
        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }   
}
