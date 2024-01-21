package inflearnproject.anoncom.post.repository;

import inflearnproject.anoncom.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findPostById(Long id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.category = :category")
    Page<Post> findPostByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments")
    Page<Post> findPosts(Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.location = :location")
    Page<Post> findPostsByLocation(@Param("location") String location, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.location = :location and p.category = :category")
    Page<Post> findPostsByLocationAndCategory(@Param("location")String location,@Param("category") String category, Pageable pageable);
}
