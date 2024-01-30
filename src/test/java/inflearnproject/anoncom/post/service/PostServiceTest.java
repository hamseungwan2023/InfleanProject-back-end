package inflearnproject.anoncom.post.service;

import inflearnproject.anoncom.comment.exception.NotSameUserException;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.PostSearchCondition;
import inflearnproject.anoncom.post.dto.ReqAddPostDto;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

import static inflearnproject.anoncom.custom.TestServiceUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    PostRepository postRepository;

    private UserEntity user;
    private Post post;

    @BeforeEach
    void addUserPost() {
        user = addUser(userService);
        post = addPost(user, postService);
        System.out.println(post);
        System.out.println(post.getId());
        System.out.println(user);
    }

    @Test
    @DisplayName("게시글을 하나 작성하면 db에 잘 저장되고, 작성한 user의 rank가 하나 오른다")
    void add_post() {

        Post findPost = postRepository.findById(post.getId()).get();
        assertEquals(post.getLocation(), user.getLocation());
        assertEquals(post.getCategory(), "카테고리");
        assertEquals(post.getContent(), "컨텐츠123123123");
        assertEquals(post.getTitle(), "제목");
        assertNotNull(postRepository.findById(findPost.getId()));
        assertEquals(user.getRank(), 1);
    }


    @Test
    @DisplayName("게시글 수정이 잘 되나 -> 제목 카테고리 내용 세 가지 다")
    void post_update() {
        Long postId = post.getId();
        ReqAddPostDto reqAddPostDto = new ReqAddPostDto("제목2", "카테고리2", "컨텐츠123123123");
        LoginUserDto loginUserDto = buildUserDto(user);
        postService.update(loginUserDto, postId, reqAddPostDto);

        assertEquals(post.getContent(), "컨텐츠123123123");
        assertEquals(post.getCategory(), "카테고리2");
        assertEquals(post.getTitle(), "제목2");
    }

    @Test
    @DisplayName("동일한 userId를 가지고있을 시 삭제가 잘 되나")
    void post_delete_success() {
        postService.delete(user.getId(), post.getId());
        boolean flag = postRepository.existsById(post.getId());
        assertFalse(flag);
    }

    @Test
    @DisplayName("동일한 userId를 가지고 있지 않을 시 에러 발생")
    void post_delete_fail() {

        UserEntity user2 = UserEntity.builder()
                .email("2@naver.com")
                .username("username2")
                .nickname("nickname2")
                .password("password2")
                .roles(new HashSet<>())
                .location("seoul")
                .isActive(true)
                .posts(new ArrayList<>())
                .build();
        userService.joinUser(user2);

        assertThrows(NotSameUserException.class, () -> postService.delete(user2.getId(), post.getId()));
    }


    @Test
    @DisplayName("post가 매우 많을 시 자료들을 조건에 따라 잘 가져오나")
    void get_posts_cond() {
        Pageable pageable = PageRequest.of(0, 10);
        LoginUserDto userDto = buildUserDto(user);
        for (int i = 1; i <= 100; i++) {
            String title = i % 5 == 0 ? "제목1" : "제목0";
            Post post2 = Post.builder()
                    .title(title)
                    .category("카테고리")
                    .content("컨텐츠123123123")
                    .userLike(0)
                    .userDisLike(0)
                    .views(0)
                    .build();
            ReqAddPostDto postDto = buildPostDto(post2);

            postService.savePost(userDto, postDto);
        }


        PostSearchCondition postCond = new PostSearchCondition();
        postCond.setTitle("제목1");

        Page<Post> postsByCondByDSL = postService.findPostsByCondByDSL(postCond, pageable);
        assertEquals(postsByCondByDSL.getContent().size(), 10);
    }
}