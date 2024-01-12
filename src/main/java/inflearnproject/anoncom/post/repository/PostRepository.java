package inflearnproject.anoncom.post.repository;

import inflearnproject.anoncom.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findPostById(Long id);

}
