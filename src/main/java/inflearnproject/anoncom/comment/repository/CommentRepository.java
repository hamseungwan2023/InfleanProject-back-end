package inflearnproject.anoncom.comment.repository;

import inflearnproject.anoncom.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentById(Long id);

    List<Comment> findByPostId(Long postId);
}
