package inflearnproject.anoncom.comment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inflearnproject.anoncom.custom.MockMvcUTF;
import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.ResAddPostDto;
import inflearnproject.anoncom.user.dto.ResUserLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@MockMvcUTF
class CommentServiceTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    CommentService commentService;
    @Autowired
    ObjectMapper objectMapper;

    private String accessToken;
    private Long postId;



    private void signupAndLoginUser() throws Exception {
        String jsonRequest1 = "{\"nickname\":\"nickname\", \"username\":\"username\", \"password\":\"password\", \"email\":\"1@naver.com\"}";

        MockMultipartFile jsonFile = new MockMultipartFile("reqUserJoinFormDto", "", "application/json", jsonRequest1.getBytes());


        mockMvc.perform(MockMvcRequestBuilders.multipart("/user/signup")
                        .file(jsonFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        String jsonRequest = "{\"username\":\"username\", \"password\":\"password\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ResUserLoginDto dto = objectMapper.readValue(contentAsString, new TypeReference<ResUserLoginDto>() {});
        accessToken = dto.getAccessToken();
    }

    private void addPost() throws Exception {
        String jsonRequest = "{\"title\":\"제목\", \"content\":\"컨텐츠\", \"category\":\"카테고리\"}";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/postWrite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ResAddPostDto dto = objectMapper.readValue(contentAsString, new TypeReference<ResAddPostDto>() {});
        postId = dto.getId();

    }
}