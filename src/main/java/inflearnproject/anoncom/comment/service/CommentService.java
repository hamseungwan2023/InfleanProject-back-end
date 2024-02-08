package inflearnproject.anoncom.comment.service;

import inflearnproject.anoncom.alarm.service.AlarmService;
import inflearnproject.anoncom.comment.dto.ResCommentDto;
import inflearnproject.anoncom.comment.exception.NoCommentException;
import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.comment.repository.CommentDSLRepository;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.ReComment;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.reComment.dto.ResReCommentDto;
import inflearnproject.anoncom.reComment.repository.ReCommentDSLRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static inflearnproject.anoncom.alarm.constant.AlarmMessage.POST_ADD_COMMENT;
import static inflearnproject.anoncom.error.ExceptionMessage.NOT_SAME_USER;
import static inflearnproject.anoncom.error.ExceptionMessage.NO_COMMENT_MESSAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentDSLRepository commentDSLRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReCommentDSLRepository reCommentDSLRepository;
    private final AlarmService alarmService;

    public Comment saveComment(LoginUserDto userDto, Long postId, String content) {
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

        alarmService.addAlarm(post.getUser(), post.getTitle() + POST_ADD_COMMENT);

        return commentRepository.save(comment);
    }

    public List<ResCommentDto> findComments(Long postId) {

        List<ResCommentDto> comments = commentDSLRepository.findCommentByPostId(postId);
        List<Long> commentIds = comments.stream().map(ResCommentDto::getId).collect(Collectors.toList());

        // 모든 대댓글을 한 번에 조회
        Map<Long, List<ResReCommentDto>> reCommentsMap = reCommentDSLRepository.findReCommentsByPost(postId, commentIds);

        // 각 댓글에 대댓글 매핑
        comments.forEach(comment -> comment.setReplyCommentList(reCommentsMap.get(comment.getId())));
        return comments;
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
        if (comment == null) {
            throw new NoCommentException(NO_COMMENT_MESSAGE);
        }

        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        if (!comment.getUser().equals(user)) {
            throw new NotSameUserException(NOT_SAME_USER);
        }
        return comment;
    }

    public List<ResCommentDto> findCommentsBatch(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::convertToResCommentDto).collect(Collectors.toList());
    }

    private ResCommentDto convertToResCommentDto(Comment comment) {
        ResCommentDto dto = new ResCommentDto(comment);
        // DTO에 필요한 데이터 설정
        dto.setReplyCommentList(convertToResReCommentDtoList(new ArrayList<>(comment.getReComments())));
        return dto;
    }

    private List<ResReCommentDto> convertToResReCommentDtoList(List<ReComment> reComments) {
        return reComments.stream().map(reComment -> convertToResReCommentDto(reComment)).collect(Collectors.toList());
    }

    private ResReCommentDto convertToResReCommentDto(ReComment reComment) {
        ResReCommentDto dto = new ResReCommentDto(reComment);
        // DTO에 필요한 데이터 설정
        return dto;
    }
}
