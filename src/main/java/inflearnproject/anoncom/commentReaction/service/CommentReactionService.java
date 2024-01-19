package inflearnproject.anoncom.commentReaction.service;

import inflearnproject.anoncom.comment.exception.NoCommentException;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.commentReaction.repository.CommentReactionRepository;
import inflearnproject.anoncom.domain.*;
import inflearnproject.anoncom.post.exception.NoPostException;
import inflearnproject.anoncom.postReaction.exception.AlreadyReactionExistsException;
import inflearnproject.anoncom.postReaction.exception.NotIncreaseLikeSelfException;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static inflearnproject.anoncom.user.exception.ExceptionMessage.NO_COMMENT_MESSAGE;
import static inflearnproject.anoncom.user.exception.ExceptionMessage.NO_POST_MESSAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReactionService {

    private final CommentReactionRepository commentReactionRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    public void increaseLike(Long memberId, Long commentId) {
        isAlreadyReactionExists(memberId, commentId);
        UserEntity user = userRepository.findById(memberId).orElseThrow(() -> new NoUserEntityException("해당 정보와 일치하는 회원이 존재하지 않습니다"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoCommentException(NO_COMMENT_MESSAGE));
        if(comment.isOwnedBy(memberId)){
            //자신이 작성한 게시글에는 스스로 추천할 수 없다.
            throw new NotIncreaseLikeSelfException("자신이 작성한 댓글에는 좋아요를 할 수 없습니다.");
        }
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
        UserEntity user = userRepository.findById(memberId).orElseThrow(() -> new NoUserEntityException("해당 정보와 일치하는 회원이 존재하지 않습니다"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoCommentException(NO_COMMENT_MESSAGE));
        if(comment.isOwnedBy(memberId)){
            //자신이 작성한 게시글에는 스스로 추천할 수 없다.
            throw new NotIncreaseLikeSelfException("자신이 작성한 댓글에는 좋아요를 할 수 없습니다.");
        }
        CommentReaction commentReaction = CommentReaction.builder()
                .user(user)
                .post(comment.getPost())
                .comment(comment)
                .liked(false)
                .build();
        comment.addUserDisLike();

        commentReactionRepository.save(commentReaction);
    }

    public void isAlreadyReactionExists(Long memberId, Long commentId){
        if(commentReactionRepository.existsByUserIdAndCommentId(memberId, commentId)){
            throw new AlreadyReactionExistsException("이미 좋아요/싫어요한 게시글에 좋아요/싫어요를 할 수 없습니다.");
        }
    }
}
