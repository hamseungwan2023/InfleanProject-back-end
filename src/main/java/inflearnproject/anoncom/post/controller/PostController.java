package inflearnproject.anoncom.post.controller;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.*;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.post.service.PostService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Value("${spring.data.web.pageable.default-page-size}")
    private int defaultPageSize;
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


    @DeleteMapping("/api/postDelete/{postId}")
    public ResponseEntity<?> deletePost(@IfLogin LoginUserDto userDto, @PathVariable("postId") Long postId){
        postService.delete(userDto.getMemberId() ,postId);
        return ResponseEntity.ok().body("ok");
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


    /**
     * 지역에 해당되는 모든 게시글들 보여주기
     */
    @GetMapping("/api/postList")
    public ResponseEntity<PagingPost> getPostsByLocation(
                                                         @ModelAttribute(value = "findPostContent") PostSearchCondition cond,
                                                         @RequestParam(value = "page", defaultValue = "0") int page){

        Pageable pageable = PageRequest.of(page, defaultPageSize);
        return getPagingPostResponseEntity(cond, pageable);
    }

    private ResponseEntity<PagingPost> getPagingPostResponseEntity(PostSearchCondition cond, Pageable pageable) {
        Page<Post> postsByCategory = postService.findPostsByCondByDSL(cond, pageable);
        int currentPage = postsByCategory.getNumber();
        int totalPage = postsByCategory.getTotalPages();
        List<ResPostDto> dtos = postsByCategory.stream().map(ResPostDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(new PagingPost(dtos, currentPage, totalPage));
    }


}
