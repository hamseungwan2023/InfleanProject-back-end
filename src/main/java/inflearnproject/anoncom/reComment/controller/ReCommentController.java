package inflearnproject.anoncom.reComment.controller;

import inflearnproject.anoncom.reComment.dto.ReqAddReCommentDto;
import inflearnproject.anoncom.reComment.service.ReCommentService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ReCommentController {

    private final ReCommentService reCommentService;

    @PostMapping("/api/replyCommentWrite/{commentId}")
    public void addReplyComment(@IfLogin LoginUserDto userDto, @PathVariable("commentId") Long commentId, @RequestBody ReqAddReCommentDto reqAddReCommentDto) {
        reCommentService.addReComment(userDto, commentId, reqAddReCommentDto.getContent());
    }

    @PatchMapping("/api/replyCommentCorrect/{replyCommentId}")
    public void patchReplayComment(@IfLogin LoginUserDto userDto, @PathVariable("replyCommentId") Long reCommentId, @RequestBody ReqAddReCommentDto reqAddReCommentDto) {
        reCommentService.patchComment(userDto, reCommentId, reqAddReCommentDto);
    }

    @DeleteMapping("/api/replyCommentDelete/{commentReplyId}")
    public void deleteReplyComment(@IfLogin LoginUserDto userDto, @PathVariable("commentReplyId") Long reCommentId) {
        reCommentService.deleteReComment(userDto, reCommentId);
    }
}
