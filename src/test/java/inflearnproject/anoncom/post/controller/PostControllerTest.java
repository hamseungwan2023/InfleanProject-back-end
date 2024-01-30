package inflearnproject.anoncom.post.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inflearnproject.anoncom.custom.MockMvcUTF;
import inflearnproject.anoncom.custom.TestControllerUtils;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.PagingPost;
import inflearnproject.anoncom.post.dto.ResAddPostDto;
import inflearnproject.anoncom.post.dto.ResPostDetailDto;
import inflearnproject.anoncom.post.repository.PostRepository;
import inflearnproject.anoncom.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@MockMvcUTF
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    private String accessToken;
    private Long postId;

    @BeforeEach
    void before() throws Exception {
        List<UserEntity> all = userRepository.findAll();
        System.out.println("zz");
        for (UserEntity userEntity : all) {
            System.out.println(userEntity);
        }
        signupAndLoginUser();
        addPost();
    }

    @Test
    @DisplayName("게시글 추가가 잘 됐나 확인")
    @WithMockUser(username = "username1")
    void addPost_success() throws Exception {
        String jsonRequest = "{\"title\":\"제목\", \"content\":\"컨텐츠\", \"category\":\"카테고리\",\"location\":\"seoul\"}";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/postWrite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ResAddPostDto dto = objectMapper.readValue(contentAsString, new TypeReference<ResAddPostDto>() {
        });
        assertEquals(dto.getCategory(), "카테고리");
        assertEquals(dto.getContent(), "컨텐츠");
        assertEquals(dto.getTitle(), "제목");
        assertNotNull(postRepository.findPostById(dto.getId()));
    }

    @Test
    @DisplayName("게시글 작성한 사람이 게시글 수정을 요청하면 처리가 잘 됐나")
    void post_update() throws Exception {
        String jsonRequest = "{\"title\":\"제목2\", \"content\":\"컨텐츠2\", \"category\":\"카테고리2\",\"location\":\"seoul\"}";
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/postCorrect/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        Post post = postRepository.findById(postId).get();
        assertEquals(post.getTitle(), "제목2");
        assertEquals(post.getContent(), "컨텐츠2");
        assertEquals(post.getCategory(), "카테고리2");
    }


    @Test
    @DisplayName("게시글 작성한 사람이 게시글 삭제를 요청하면 처리가 잘 됐나")
    void post_delete() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/postDelete/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        assertThat(postRepository.findById(postId)).isEmpty();
    }

    @Test
    @DisplayName("게시글 상세한 것들을 잘 보여주나")
    void post_detail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/postDetail/" + postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Post post = postRepository.findById(postId).get();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ResPostDetailDto dto = objectMapper.readValue(contentAsString, new TypeReference<ResPostDetailDto>() {
        });
        UserEntity user = userRepository.findByNickname("nickname").get();
        assertEquals(dto.getId(), postId);
        assertEquals(dto.getTitle(), post.getTitle());
        assertEquals(dto.getCategory(), post.getCategory());
        assertEquals(dto.getWriteId(), user.getId());
        assertEquals(dto.getWriteNickname(), user.getNickname());
        assertEquals(dto.getViews(), 1);
        assertEquals(dto.getContent(), "컨텐츠");
    }

    @Test
    @DisplayName("동일한 카테고리에 해당되는 게시글들을 잘 가져오나")
    void getPosts_Cond() throws Exception {

        for (int i = 0; i < 100; i++) {
            if (i < 4) addPost();
            else addPostDifferentCategory();
        }

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/postList")
                        .param("category", "카테고리")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        PagingPost dto = objectMapper.readValue(contentAsString, new TypeReference<PagingPost>() {
        });
        assertEquals(dto.getDtos().size(), 5);
    }


    private void signupAndLoginUser() throws Exception {
        String signupReQuest = "{\"nickname\":\"nickname\", \"username\":\"username\", \"password\":\"password\", \"email\":\"1@naver.com\",\"location\":\"seoul\"}";
        TestControllerUtils.signUpUser(mockMvc, signupReQuest);
        String loginRequest = "{\"username\":\"username\", \"password\":\"password\"}";
        accessToken = TestControllerUtils.loginUser(mockMvc, objectMapper, loginRequest);
    }

    private void addPost() throws Exception {
        String jsonRequest = "{\"title\":\"제목\", \"content\":\"컨텐츠\", \"category\":\"카테고리\"}";
        postId = TestControllerUtils.addPostReturnPostId(mockMvc, objectMapper, accessToken, jsonRequest);
    }

    private void addPostDifferentCategory() throws Exception {
        String jsonRequest = "{\"title\":\"제목\", \"content\":\"컨텐츠\", \"category\":\"카테고리2\"}";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/postWrite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ResAddPostDto dto = objectMapper.readValue(contentAsString, new TypeReference<ResAddPostDto>() {
        });
        postId = dto.getId();

    }
}