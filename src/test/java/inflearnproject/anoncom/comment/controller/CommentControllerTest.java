package inflearnproject.anoncom.comment.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.custom.MockMvcUTF;
import inflearnproject.anoncom.custom.TestControllerUtils;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.post.dto.ResAddPostDto;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.refreshToken.repository.RefreshTokenRepository;
import inflearnproject.anoncom.user.repository.UserRepository;
import jdk.swing.interop.SwingInterOpUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static inflearnproject.anoncom.custom.TestControllerUtils.addComment;
import static inflearnproject.anoncom.custom.TestControllerUtils.addPostReturnPostId;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@MockMvcUTF
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentControllerTest {
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
    @BeforeEach
    void before() throws Exception{
        String signupReQuest = "{\"nickname\":\"nickname\", \"username\":\"username\", \"password\":\"password\", \"email\":\"1@naver.com\"}";
        TestControllerUtils.signUpUser(mockMvc,signupReQuest);

        String loginRequest = "{\"username\":\"username\", \"password\":\"password\"}";
        accessToken = TestControllerUtils.loginUser(mockMvc,objectMapper,loginRequest);

        String postRequest = "{\"title\":\"제목\", \"content\":\"컨텐츠\", \"category\":\"카테고리\"}";
        Long postId = addPostReturnPostId(mockMvc, objectMapper, accessToken, postRequest);

        String jsonRequest = "{\"content\":\"댓글\"}";

        Long commentId = addComment(mockMvc,postId,accessToken,commentRepository);
    }

    @Test
    @DisplayName("댓글 추가가 잘 됐나 확인")
    void add_comment_success() throws Exception {

        Post post = postRepository.findAll().get(0);

        Post findPost = postRepository.findById(post.getId()).get();
        assertEquals(findPost.getComments().get(0).getContent(),"댓글");
        assertEquals(findPost.getComments().size(),1);
    }

    @Test
    @DisplayName("댓글 수정이 잘 됐나 확인")
    void update_comment_success() throws Exception {
        String jsonRequest = "{\"content\":\"댓글2\"}";

        Post post = postRepository.findAll().get(0);
        Long commentId = post.getComments().get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/commentCorrect/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        Post findPost = postRepository.findById(post.getId()).get();
        assertEquals(findPost.getComments().get(0).getContent(),"댓글2");
        assertEquals(findPost.getComments().size(),1);
    }

    @Test
    @DisplayName("댓글 삭제가 잘 됐나 확인 -> 실제 삭제가 아닌 deleted 필드가 true로 바뀌어야함")
    void delete_comment_success() throws Exception {

        Post post = postRepository.findAll().get(0);
        Long commentId = post.getComments().get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/commentDelete/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertTrue(post.getComments().get(0).isDeleted());
    }
}