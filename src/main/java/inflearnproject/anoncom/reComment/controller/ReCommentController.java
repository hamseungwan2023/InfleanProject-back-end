package inflearnproject.anoncom.reComment.controller;

import inflearnproject.anoncom.reComment.dto.ReqAddReCommentDto;
import inflearnproject.anoncom.reComment.service.ReCommentService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ReCommentController {

    private final ReCommentService reCommentService;

    @PostMapping("/api/replyCommentWrite/{commentId}")
    public ResponseEntity<?> addReplyComment(@IfLogin LoginUserDto userDto, @PathVariable("commentId") Long commentId, @Valid @RequestBody ReqAddReCommentDto reqAddReCommentDto) {
        reCommentService.addReComment(userDto, commentId, reqAddReCommentDto.getContent());
        return ResponseEntity.ok().body("ok");
    }

    @PatchMapping("/api/replyCommentCorrect/{replyCommentId}")
    public ResponseEntity<?> patchReplayComment(@IfLogin LoginUserDto userDto, @PathVariable("replyCommentId") Long reCommentId, @Valid @RequestBody ReqAddReCommentDto reqAddReCommentDto) {
        reCommentService.patchComment(userDto, reCommentId, reqAddReCommentDto);
        return ResponseEntity.ok().body("ok");
    }

    @DeleteMapping("/api/replyCommentDelete/{commentReplyId}")
    public ResponseEntity<?> deleteReplyComment(@IfLogin LoginUserDto userDto, @PathVariable("commentReplyId") Long reCommentId) {
        reCommentService.deleteReComment(userDto, reCommentId);
        return ResponseEntity.ok().body("ok");
    }
}
