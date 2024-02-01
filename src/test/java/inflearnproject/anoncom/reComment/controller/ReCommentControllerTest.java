package inflearnproject.anoncom.reComment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.custom.MockMvcUTF;
import inflearnproject.anoncom.custom.TestControllerUtils;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@MockMvcUTF
class ReCommentControllerTest {

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

    private Long reCommentId;

    @BeforeEach
    void before() throws Exception {
        String signupReQuest = "{\"nickname\":\"nickname\", \"username\":\"username\", \"password\":\"password\", \"email\":\"1@naver.com\",\"location\":\"SEOUL\"}";
        TestControllerUtils.signUpUser(mockMvc, signupReQuest);

        String loginRequest = "{\"username\":\"username\", \"password\":\"password\"}";
        accessToken = TestControllerUtils.loginUser(mockMvc, objectMapper, loginRequest);

        String postRequest = "{\"title\":\"제목\", \"content\":\"컨텐츠123123123\", \"category\":\"카테고리\"}";
        Long postId = addPostReturnPostId(mockMvc, objectMapper, accessToken, postRequest);

        String commentRequest = "{\"content\":\"댓글\"}";
        Long commentId = addComment(mockMvc, postId, accessToken, commentRepository, commentRequest);

        String reCommentRequest = "{\"content\":\"대댓글\"}";
        reCommentId = addReComment(mockMvc, commentId, reCommentRequest, accessToken, reCommentRepository);
    }

    @Test
    @DisplayName("대댓글이 잘 추가되었나 확인하기")
    void addRecomment_success() {
        assertEquals(reCommentRepository.findAll().get(0).getContent(), "대댓글");
        assertEquals(reCommentRepository.count(), 1);
    }

    @Test
    @DisplayName("대댓글의 수정이 잘 이루어졌나 확인")
    void update_reComment_success() throws Exception {
        String jsonRequest = "{\"content\":\"대댓글2\"}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/replyCommentCorrect/" + reCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
        ReComment reComment = reCommentRepository.findById(reCommentId).get();
        assertEquals(reComment.getContent(), "대댓글2");
    }

    @Test
    @DisplayName("대댓글을 단 본인이 아닌 다른 사용자가 대댓글의 수정을 요청할 경우 에러가 나는지 확인")
    void update_reComment_fail() throws Exception {
        String signupReQuest = "{\"nickname\":\"nickname2\", \"username\":\"username2\", \"password\":\"password2\", \"email\":\"2@naver.com\",\"location\":\"SEOUL\"}";
        TestControllerUtils.signUpUser(mockMvc, signupReQuest);

        String loginRequest = "{\"username\":\"username2\", \"password\":\"password2\"}";
        accessToken = TestControllerUtils.loginUser(mockMvc, objectMapper, loginRequest);

        String jsonRequest = "{\"content\":\"대댓글2\"}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/replyCommentCorrect/" + reCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("대댓글 삭제할 시 실제로 삭제되지는 않지만 deleted필드가 true가 되어야 한다")
    void delete_reComment_success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/replyCommentDelete/" + reCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertTrue(reCommentRepository.findById(reCommentId).get().isDeleted());
    }
}