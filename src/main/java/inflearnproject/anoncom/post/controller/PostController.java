package inflearnproject.anoncom.post.controller;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.ReqAddPostDto;
import inflearnproject.anoncom.post.dto.ResAddPostDto;
import inflearnproject.anoncom.post.dto.ResPostDetailDto;
import inflearnproject.anoncom.post.dto.ResPostDto;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static inflearnproject.anoncom.post.dto.ReqAddPostDto.*;
import static inflearnproject.anoncom.post.dto.ResAddPostDto.buildResPostDto;
import static inflearnproject.anoncom.post.dto.ResPostDetailDto.buildResPostDetailDto;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @PostMapping("/api/postWrite")
    public ResponseEntity<ResAddPostDto> addPost(@IfLogin LoginUserDto userDto, @RequestBody ReqAddPostDto postDto){

        Post post = buildPost(postDto);
        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        postService.savePost(user,post);
        ResAddPostDto resAddPostDto = buildResPostDto(user.getId(), post);

        return ResponseEntity.ok().body(resAddPostDto);
    }

    //TODO API URL 규칙에 맞게 나중에 수정하기

    @PatchMapping("/api/postCorrect/{postId}")
    public ResponseEntity<?> updatePost(@IfLogin LoginUserDto userDto,@PathVariable("postId") Long postId,
                                                    @RequestBody ReqAddPostDto postDto){
        postService.update(postId,postDto);
        return ResponseEntity.ok().body("ok");
    }


    /**
     카테고리에 따라 게시글들을 보여주는 메서드
     */
    @GetMapping("/api/postList/{category}")
    public ResponseEntity<List<ResPostDto>> getPostsByCategory(@PathVariable("category") String category){
        List<Post> postsByCategory = postService.findPostsByCategory(category);
        List<ResPostDto> dtos = postsByCategory.stream().map(ResPostDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }
    /**
     * postId에 해당되는 게시글 하나의 상세 정보를 보여주는 메서드
     */
    @GetMapping("/api/postDetail/{postId}")
    public ResponseEntity<ResPostDetailDto> getPost(@PathVariable Long postId){
        Post post = postService.findPostById(postId);
        ResPostDetailDto resPostDetailDto = buildResPostDetailDto(post);
        return ResponseEntity.ok().body(resPostDetailDto);
    }
}
