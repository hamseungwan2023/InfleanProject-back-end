package inflearnproject.anoncom.comment.controller;

import inflearnproject.anoncom.comment.dto.ReqAddCommentDto;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.comment.service.CommentService;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @PostMapping("/api/commentWrite/{postId}")
    public ResponseEntity<?> addComment(@IfLogin LoginUserDto userDto, @PathVariable("postId") Long postId, @RequestBody ReqAddCommentDto reqAddCommentDto){
        Post post = postRepository.findPostById(postId);
        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .userLike(0)
                .content(reqAddCommentDto.getContent())
                .build();
        commentService.saveComment(comment,user,post);

        return ResponseEntity.ok().body("ok");
    }

}
