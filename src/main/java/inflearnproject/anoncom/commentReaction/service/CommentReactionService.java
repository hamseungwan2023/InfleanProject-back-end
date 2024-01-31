package inflearnproject.anoncom.commentReaction.service;

import inflearnproject.anoncom.comment.exception.NoCommentException;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.commentReaction.repository.CommentReactionRepository;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.CommentReaction;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.postReaction.exception.AlreadyReactionExistsException;
import inflearnproject.anoncom.postReaction.exception.NotIncreaseLikeSelfException;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static inflearnproject.anoncom.error.ExceptionMessage.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReactionService {

    private final CommentReactionRepository commentReactionRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public void increaseLike(Long memberId, Long commentId) {
        isAlreadyReactionExists(memberId, commentId);
        UserEntity user = userRepository.findById(memberId).orElseThrow(() -> new NoUserEntityException(NO_SAME_INFO_USER_MESSAGE));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoCommentException(NO_COMMENT_MESSAGE));
        validSelfReaction(memberId, comment);
        CommentReaction commentReaction = CommentReaction.builder()
                .user(user)
                .post(comment.getPost())
                .comment(comment)
                .liked(true)
                .build();
        comment.addUserLike();

        commentReactionRepository.save(commentReaction);
    }

    public void increaseDisLike(Long memberId, Long commentId) {
        isAlreadyReactionExists(memberId, commentId);
        UserEntity user = userRepository.findById(memberId).orElseThrow(() -> new NoUserEntityException(NO_SAME_INFO_USER_MESSAGE));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoCommentException(NO_COMMENT_MESSAGE));
        validSelfReaction(memberId, comment);
        CommentReaction commentReaction = CommentReaction.builder()
                .user(user)
                .post(comment.getPost())
                .comment(comment)
                .liked(false)
                .build();
        comment.addUserDisLike();

        commentReactionRepository.save(commentReaction);
    }

    private static void validSelfReaction(Long memberId, Comment comment) {
        if (comment.isOwnedBy(memberId)) {
            //자신이 작성한 게시글에는 스스로 추천할 수 없다.
            throw new NotIncreaseLikeSelfException(CANNOT_SELF_REACTION_COMMENT);
        }
    }

    public void isAlreadyReactionExists(Long memberId, Long commentId) {
        if (commentReactionRepository.existsByUserIdAndCommentId(memberId, commentId)) {
            throw new AlreadyReactionExistsException(CANNOT_REACTION_TWICE_COMMENT);
        }
    }
}
