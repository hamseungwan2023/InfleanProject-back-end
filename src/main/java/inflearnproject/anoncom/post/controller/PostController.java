package inflearnproject.anoncom.post.controller;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.ReqAddPostDto;
import inflearnproject.anoncom.post.dto.ResPostDto;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static inflearnproject.anoncom.post.dto.ReqAddPostDto.*;
import static inflearnproject.anoncom.post.dto.ResPostDto.buildResPostDto;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    @PostMapping("/api/postWrite")
    public ResponseEntity<String> addPost(@IfLogin LoginUserDto userDto, @RequestBody ReqAddPostDto postDto){
        Post post = buildPost(postDto);
        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        System.out.println(userDto);
        System.out.println(user);
        postService.savePost(user,post);
        return ResponseEntity.ok().body("ok");
    }
}
