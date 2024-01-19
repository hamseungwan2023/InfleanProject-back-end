package inflearnproject.anoncom.commentReaction.controller;

import inflearnproject.anoncom.commentReaction.service.CommentReactionService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentReactionController {

    private final CommentReactionService commentReactionService;

    @PostMapping("/api/increaseCommentLike/{commentId}")
    public ResponseEntity<?> increaseLike(@IfLogin LoginUserDto userDto, @PathVariable("commentId") Long commentId){
        commentReactionService.increaseLike(userDto.getMemberId(),commentId);
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/api/decreaseCommentLike/{commentId}")
    public ResponseEntity<?> increaseDisLike(@IfLogin LoginUserDto userDto, @PathVariable("commentId") Long commentId){
        commentReactionService.increaseDisLike(userDto.getMemberId(),commentId);
        return ResponseEntity.ok().body("ok");
    }
}
