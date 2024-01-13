package inflearnproject.anoncom.comment.service;

import inflearnproject.anoncom.comment.dto.ReqCommentDto;
import inflearnproject.anoncom.comment.exception.NoCommentException;
import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.comment.repository.CommentDSLRepository;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
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

    public void saveComment(Comment comment,UserEntity user, Post post){
        comment.putUserPost(user,post);
        commentRepository.save(comment);
    }

    public List<ReqCommentDto> findComments(Long postId){
        return commentDSLRepository.findCommentByPostId(postId);
    }

    public void updateComment(LoginUserDto userDto, Long commentId, String content){
        Comment comment = commentRepository.findCommentById(commentId);
        UserEntity user = userRepository.findByEmail(userDto.getEmail());

        if(comment == null){
            throw new NoCommentException("해당 댓글이 존재하지 않습니다.");
        }
        if(!comment.getUser().equals(user)){
            throw new NotSameUserException("동일한 유저가 아니라서 삭제가 불가능합니다.");
        }
        comment.updateContent(content);
    }
}
