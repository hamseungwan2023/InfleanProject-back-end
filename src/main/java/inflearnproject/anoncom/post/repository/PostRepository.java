package inflearnproject.anoncom.post.repository;

import inflearnproject.anoncom.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findPostById(Long id);

    Page<Post> findPostByCategory(String category, Pageable pageable);
}
