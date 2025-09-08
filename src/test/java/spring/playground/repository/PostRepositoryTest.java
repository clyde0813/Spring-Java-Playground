package spring.playground.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import spring.playground.domain.entity.Comment;
import spring.playground.domain.entity.Post;

@SpringBootTest
@ActiveProfiles("dev")
public class PostRepositoryTest {

    @Autowired EntityManagerFactory emf;

    @Autowired PostRepository postRepository;
    @Autowired CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 2; i++) {
            Post post = Post.builder()
            .content("Post " + i)
            .build();
    
            for (int j = 1; j <= 100; j++) {
                Comment comment = Comment.builder()
                        .content("Comment " + j + " of Post " + i)
                        .post(post)
                        .build();
                post.getComments().add(comment); // or post.addComment(comment);
            }
            postRepository.save(post); // cascade 로 comment도 저장됨
        }
    }

    @Test
    @Transactional
    void nPlusOneTest() {
        int pageSize = 3;

        Statistics stats = emf.unwrap(SessionFactory.class).getStatistics();
        stats.clear(); // 초기화

        Page<Post> posts = postRepository.findAll(PageRequest.of(0, pageSize));

        for (Post post : posts) {
            System.out.println("comment = " + post.getComments());
        }

        long queryCount = stats.getPrepareStatementCount();
        System.out.println("QueryCount = " + queryCount);

        assertThat(queryCount).isEqualTo(pageSize * 2);
    }
}
