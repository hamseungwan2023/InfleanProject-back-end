package inflearnproject.anoncom.comment.controller;

import inflearnproject.anoncom.comment.dto.ReqAddPatchCommentDto;
import inflearnproject.anoncom.comment.dto.ResCommentDto;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.comment.service.CommentService;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.reComment.repository.ReCommentDSLRepository;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final ReCommentDSLRepository reCommentDSLRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @PostMapping("/api/commentWrite/{postId}")
    public ResponseEntity<?> addComment(@IfLogin LoginUserDto userDto, @PathVariable("postId") Long postId, @RequestBody ReqAddPatchCommentDto reqAddPatchCommentDto) {
        commentService.saveComment(userDto, postId, reqAddPatchCommentDto.getContent());
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/api/commentList/{postId}")
    public ResponseEntity<List<ResCommentDto>> showComments(@PathVariable("postId") Long postId) {
        List<ResCommentDto> comments = commentService.findComments(postId);
        return ResponseEntity.ok().body(comments);
    }

    @PatchMapping("/api/commentCorrect/{commentId}")
    public ResponseEntity<?> patchComment(@IfLogin LoginUserDto userDto, @PathVariable("commentId") Long commentId, @Valid @RequestBody ReqAddPatchCommentDto reqAddPatchCommentDto) {
        commentService.updateComment(userDto, commentId, reqAddPatchCommentDto.getContent());
        return ResponseEntity.ok().body("ok");
    }

    @DeleteMapping("/api/commentDelete/{commentId}")
    public ResponseEntity<?> deleteComment(@IfLogin LoginUserDto userDto, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(userDto, commentId);
        return ResponseEntity.ok().body("ok");
    }
}
