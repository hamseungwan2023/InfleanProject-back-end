package inflearnproject.anoncom.commentReaction.repository;

import inflearnproject.anoncom.domain.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReactionRepository extends JpaRepository<CommentReaction,Long> {

    Boolean existsByUserIdAndCommentId(Long userId, Long commentId);
}
