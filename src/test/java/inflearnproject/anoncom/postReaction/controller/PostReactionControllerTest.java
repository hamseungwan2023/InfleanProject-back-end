package inflearnproject.anoncom.postReaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.custom.MockMvcUTF;
import inflearnproject.anoncom.custom.TestControllerUtils;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.ReComment;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.reComment.repository.ReCommentRepository;
import inflearnproject.anoncom.refreshToken.repository.RefreshTokenRepository;
import inflearnproject.anoncom.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static inflearnproject.anoncom.custom.TestControllerUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@MockMvcUTF
class PostReactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    private String accessToken;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ReCommentRepository reCommentRepository;

    private Long postId;
    @BeforeEach
    void before() throws Exception{
        String signupReQuest = "{\"nickname\":\"nickname\", \"username\":\"username\", \"password\":\"password\", \"email\":\"1@naver.com\"}";
        TestControllerUtils.signUpUser(mockMvc,signupReQuest);

        String loginRequest = "{\"username\":\"username\", \"password\":\"password\"}";
        accessToken = TestControllerUtils.loginUser(mockMvc,objectMapper,loginRequest);

        String postRequest = "{\"title\":\"제목\", \"content\":\"컨텐츠\", \"category\":\"카테고리\"}";
        postId = addPostReturnPostId(mockMvc, objectMapper, accessToken, postRequest);

        String commentRequest = "{\"content\":\"댓글\"}";
        Long commentId = addComment(mockMvc,postId,accessToken,commentRepository,commentRequest);

        String reCommentRequest = "{\"content\":\"대댓글\"}";
        addReComment(mockMvc,commentId,reCommentRequest,accessToken,reCommentRepository);
    }

    @Test
    @DisplayName("게시글 작성한 본인이 아닌 다른 사용자가 게시글을 좋아요 할 시 게시글의 userLike 필드가 1 증가한다")
    void add_increase_like_post_success() throws Exception {
        loginUser2();

        increasePostLike(mockMvc,postId,accessToken);
        Post post = postRepository.findById(postId).get();
        assertEquals(post.getUserLike(),1);
    }

    @Test
    @DisplayName("게시글 작성한 본인이 좋아요를 누를 시 에러가 발생")
    void add_increase_like_post_error() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/increasePostLike/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("게시글 작성한 본인이 아닌 다른 사용자가 게시글을 싫어요 할 시 게시글의 userLike 필드가 1 증가한다")
    void add_increase_disLike_post_success() throws Exception {
        loginUser2();

        increasePostDisLike(mockMvc,postId,accessToken);
        Post post = postRepository.findById(postId).get();
        assertEquals(post.getUserDisLike(),1);
    }

    @Test
    @DisplayName("게시글 작성한 본인이 싫어요를 누를 시 에러가 발생")
    void add_increase_disLike_post_error() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/decreasePostLike/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }


    private void loginUser2() throws Exception {
        String signupReQuest = "{\"nickname\":\"nickname2\", \"username\":\"username2\", \"password\":\"password2\", \"email\":\"2@naver.com\"}";
        TestControllerUtils.signUpUser(mockMvc,signupReQuest);

        String loginRequest = "{\"username\":\"username2\", \"password\":\"password2\"}";
        accessToken = TestControllerUtils.loginUser(mockMvc,objectMapper,loginRequest);
    }
}