package inflearnproject.anoncom.postReaction.repository;

import inflearnproject.anoncom.domain.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {

    Boolean existsByUserIdAndPostId(Long userId, Long postId);
}
