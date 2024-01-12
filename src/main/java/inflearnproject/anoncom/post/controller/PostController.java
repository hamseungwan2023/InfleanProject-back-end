package inflearnproject.anoncom.post.controller;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.ReqAddPostDto;
import inflearnproject.anoncom.post.dto.ResAddPostDto;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static inflearnproject.anoncom.post.dto.ReqAddPostDto.*;
import static inflearnproject.anoncom.post.dto.ResAddPostDto.buildResPostDto;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @PostMapping("/api/postWrite")
    public ResponseEntity<ResAddPostDto> addPost(@IfLogin LoginUserDto userDto, @RequestBody ReqAddPostDto postDto){
        //TODO 로그인 안 되어있으면 에러 발생
        Post post = buildPost(postDto);
        UserEntity user = userRepository.findByEmail(userDto.getEmail());

        postService.savePost(user,post);
        ResAddPostDto resAddPostDto = buildResPostDto(user, post);
        return ResponseEntity.ok().body(resAddPostDto);
    }

    //TODO API URL 규칙에 맞게 나중에 수정하기
    //TODO 에러 핸들링
    @PatchMapping("/api/postCorrect/{postId}")
    public ResponseEntity<?> updatePost(@IfLogin LoginUserDto userDto,@PathVariable("postId") Long postId,
                                                    @RequestBody ReqAddPostDto postDto){
        postService.update(postId,postDto);
        return ResponseEntity.ok().body("ok");
    }
}
