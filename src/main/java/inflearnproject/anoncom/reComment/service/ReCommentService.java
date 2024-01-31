package inflearnproject.anoncom.reComment.service;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.ReComment;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.reComment.dto.ReqAddReCommentDto;
import inflearnproject.anoncom.reComment.exception.NoReCommentException;
import inflearnproject.anoncom.reComment.repository.ReCommentRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static inflearnproject.anoncom.error.ExceptionMessage.NOT_SAME_USER;
import static inflearnproject.anoncom.error.ExceptionMessage.NO_RE_COMMENT_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class ReCommentService {

    private final ReCommentRepository reCommentRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ReComment addReComment(LoginUserDto userDto, Long commentId, String content) {
        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        Comment comment = commentRepository.findCommentById(commentId);
        Post post = comment.getPost();

        ReComment recomment = ReComment.builder().
                post(post)
                .user(user)
                .comment(comment)
                .content(content)
                .deleted(false)
                .build();
        recomment.addComment(comment);
        return reCommentRepository.save(recomment);
    }

    public void patchComment(LoginUserDto userDto, Long reCommentId, ReqAddReCommentDto reqAddReCommentDto) {
        ReComment reComment = validAndGetReComment(userDto, reCommentId);
        reComment.updateContent(reqAddReCommentDto.getContent());
    }

    public void deleteReComment(LoginUserDto userDto, Long reCommentId) {
        ReComment reComment = validAndGetReComment(userDto, reCommentId);
        reComment.delete();
    }

    private ReComment validAndGetReComment(LoginUserDto userDto, Long reCommentId) {
        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        ReComment reComment = reCommentRepository.findById(reCommentId).orElseThrow(() -> new NoReCommentException(NO_RE_COMMENT_ERROR));
        if (!user.equals(reComment.getUser())) {
            throw new NotSameUserException(NOT_SAME_USER);
        }
        return reComment;
    }
}
