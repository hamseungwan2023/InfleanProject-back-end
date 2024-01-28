package inflearnproject.anoncom.commentReaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.custom.MockMvcUTF;
import inflearnproject.anoncom.custom.TestControllerUtils;
import inflearnproject.anoncom.domain.Comment;
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
class CommentReactionControllerTest {
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

    private Long commentId;
    @BeforeEach
    void before() throws Exception{
        String signupReQuest = "{\"nickname\":\"nickname\", \"username\":\"username\", \"password\":\"password\", \"email\":\"1@naver.com\"}";
        TestControllerUtils.signUpUser(mockMvc,signupReQuest);

        String loginRequest = "{\"username\":\"username\", \"password\":\"password\"}";
        accessToken = TestControllerUtils.loginUser(mockMvc,objectMapper,loginRequest);

        String postRequest = "{\"title\":\"제목\", \"content\":\"컨텐츠\", \"category\":\"카테고리\"}";
        Long postId = addPostReturnPostId(mockMvc, objectMapper, accessToken, postRequest);

        String commentRequest = "{\"content\":\"댓글\"}";
        commentId = addComment(mockMvc,postId,accessToken,commentRepository,commentRequest);

        String reCommentRequest = "{\"content\":\"대댓글\"}";
        addReComment(mockMvc,commentId,reCommentRequest,accessToken,reCommentRepository);
    }

    @Test
    @DisplayName("좋아요 버튼을 다른 유저가 클릭 시 댓글의 좋아요가 1 상승한다")
    void increase_comment_like() throws Exception {
        loginUser2();
        addIncreaseLikeComment(mockMvc,commentId,accessToken);
        Comment findComment = commentRepository.findCommentById(commentId);
        assertEquals(findComment.getUserLike(),1);
    }

    @Test
    @DisplayName("좋아요 버튼을 댓글 단 본인이 누르면 에러가 발생힌다")
    void increase_comment_like_error() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/increaseCommentLike/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("싫어요 버튼을 다른 유저가 클릭 시 댓글의 싫어요가 1 상승한다")
    void increase_comment_disLike() throws Exception {
        loginUser2();
        addIncreaseDisLikeComment(mockMvc,commentId,accessToken);
        Comment findComment = commentRepository.findCommentById(commentId);
        assertEquals(findComment.getUserDisLike(),1);
    }

    @Test
    @DisplayName("싫어요 버튼을 댓글 단 본인이 누르면 에러가 발생힌다")
    void increase_comment_disLike_error() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/decreaseCommentLike/" + commentId)
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