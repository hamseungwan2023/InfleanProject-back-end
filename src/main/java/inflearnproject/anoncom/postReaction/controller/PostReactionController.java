package inflearnproject.anoncom.postReaction.controller;

import inflearnproject.anoncom.postReaction.service.PostReactionService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostReactionController {

    private final PostReactionService postReactionService;

    @PostMapping("/api/increasePostLike/{postId}")
    public ResponseEntity<?> increaseLike(@IfLogin LoginUserDto userDto, @PathVariable("postId") Long postId){
        postReactionService.increaseLike(userDto.getMemberId(),postId);
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/api/decreasePostLike/{postId}")
    public ResponseEntity<?> increaseDisLike(@IfLogin LoginUserDto userDto, @PathVariable("postId") Long postId){
        postReactionService.increaseDisLike(userDto.getMemberId(),postId);
        return ResponseEntity.ok().body("ok");
    }
}
