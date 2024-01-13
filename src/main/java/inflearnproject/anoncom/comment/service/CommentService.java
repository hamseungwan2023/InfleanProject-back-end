package inflearnproject.anoncom.comment.service;

import inflearnproject.anoncom.comment.dto.ReqCommentDto;
import inflearnproject.anoncom.comment.repository.CommentDSLRepository;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
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
    public void saveComment(Comment comment,UserEntity user, Post post){
        comment.putUserPost(user,post);
        commentRepository.save(comment);
    }

    public List<ReqCommentDto> findComments(Long postId){
        return commentDSLRepository.findCommentByPostId(postId);
    }
}
