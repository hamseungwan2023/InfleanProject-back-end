package inflearnproject.anoncom.comment.service;

import inflearnproject.anoncom.comment.dto.ResCommentDto;
import inflearnproject.anoncom.comment.exception.NoCommentException;
import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.comment.repository.CommentDSLRepository;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentDSLRepository commentDSLRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void saveComment(LoginUserDto userDto, Long postId, String content) {
        Post post = postRepository.findPostById(postId);
        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .userLike(0)
                .userDisLike(0)
                .content(content)
                .deleted(false)
                .build();
        comment.putUserPost(user, post);
        commentRepository.save(comment);
    }

    public List<ResCommentDto> findComments(Long postId) {
        return commentDSLRepository.findCommentByPostId(postId);
    }

    public void updateComment(LoginUserDto userDto, Long commentId, String content) {
        Comment comment = validAndGetComment(userDto, commentId);
        comment.updateContent(content);
    }

    public void deleteComment(LoginUserDto userDto, Long commentId) {
        Comment comment = validAndGetComment(userDto, commentId);
        comment.delete();
    }

    private Comment validAndGetComment(LoginUserDto userDto, Long commentId) {
        Comment comment = commentRepository.findCommentById(commentId);
        UserEntity user = userRepository.findByEmail(userDto.getEmail());

        if (comment == null) {
            throw new NoCommentException("해당 댓글이 존재하지 않습니다.");
        }
        if (!comment.getUser().equals(user)) {
            throw new NotSameUserException("동일한 유저가 아니라서 삭제가 불가능합니다.");
        }
        return comment;
    }
}
