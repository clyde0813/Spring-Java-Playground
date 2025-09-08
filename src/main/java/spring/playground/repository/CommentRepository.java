package spring.playground.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.playground.domain.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
