package spring.playground.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import spring.playground.domain.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    Page<Post> findAll(Pageable pageable);
}
