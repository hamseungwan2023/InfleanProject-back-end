package inflearnproject.anoncom.custom;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inflearnproject.anoncom.comment.repository.CommentRepository;
import inflearnproject.anoncom.domain.Comment;
import inflearnproject.anoncom.post.dto.ResAddPostDto;
import inflearnproject.anoncom.reComment.repository.ReCommentRepository;
import inflearnproject.anoncom.user.dto.ResUserLoginDto;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestControllerUtils {

    public static void signUpUser(MockMvc mockMvc,String jsonRequest) throws Exception {

        // MockMultipartFile을 사용하여 multipart 요청을 준비
        MockMultipartFile jsonFile = new MockMultipartFile("reqUserJoinFormDto", "", "application/json", jsonRequest.getBytes());

        // MockMvc를 사용하여 multipart/form-data 요청을 보냄
        mockMvc.perform(MockMvcRequestBuilders.multipart("/user/signup")
                        .file(jsonFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    public static String loginUser(MockMvc mockMvc, ObjectMapper objectMapper,String jsonRequest) throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ResUserLoginDto dto = objectMapper.readValue(contentAsString, new TypeReference<ResUserLoginDto>() {});
        return dto.getAccessToken();
    }

    public static Long addPostReturnPostId(MockMvc mockMvc, ObjectMapper objectMapper,String accessToken,String jsonRequest) throws Exception {


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/postWrite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ResAddPostDto dto = objectMapper.readValue(contentAsString, new TypeReference<ResAddPostDto>() {});
        return dto.getId();
    }

    public static Long addComment(MockMvc mockMvc, Long postId, String accessToken, CommentRepository commentRepository,String jsonRequest) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/commentWrite/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
        return commentRepository.findAll().get(0).getId();
    }

    public static Long addReComment(MockMvc mockMvc, Long commentId, String jsonRequest, String accessToken, ReCommentRepository reCommentRepository) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/replyCommentWrite/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
        return reCommentRepository.findAll().get(0).getId();
    }

    public static void increasePostLike(MockMvc mockMvc,Long postId, String accessToken) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/increasePostLike/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    public static void increasePostDisLike(MockMvc mockMvc,Long postId,String accessToken) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/decreasePostLike/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    public static void addIncreaseLikeComment(MockMvc mockMvc, Long commentId, String accessToken) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/increaseCommentLike/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    public static void addIncreaseDisLikeComment(MockMvc mockMvc, Long commentId, String accessToken) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/decreaseCommentLike/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
}
