package inflearnproject.anoncom.user.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import inflearnproject.anoncom.custom.TestUtils;
import inflearnproject.anoncom.domain.RefreshToken;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.refreshToken.dto.RefreshTokenDto;
import inflearnproject.anoncom.refreshToken.repository.RefreshTokenRepository;
import inflearnproject.anoncom.security.jwt.util.JwtTokenizer;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.dto.ResUserLoginDto;
import inflearnproject.anoncom.user.dto.UserFormDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import inflearnproject.anoncom.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    JwtTokenizer jwtTokenizer;
    @BeforeEach
    void before() throws Exception{
        // JSON 데이터를 문자열로 준비
        String jsonRequest = "{\"nickname\":\"nickname\", \"username\":\"username\", \"password\":\"password\", \"email\":\"1@naver.com\"}";
        TestUtils.signUpUser(mockMvc,jsonRequest);
    }

    @AfterEach
    void after(){
        userRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }
    @Test
    @WithMockUser
    @DisplayName("회원가입이 잘 되어서 username, password로 회원이 잘 찾아지고, 닉네임까지 일치하는지 확인")
    void signup_success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // 응답 내용을 원하는 DTO 리스트 타입으로 변환
        List<UserFormDto> userFormDtos = objectMapper.readValue(contentAsString,new TypeReference<>() {});
        assertEquals(userFormDtos.size(),1);
        assertThat(userFormDtos).extracting("nickname").containsExactly("nickname");
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("회원 삭제가 잘 되었나 확인 - 실제 삭제는 아니고 user의 isActive만 false로 변경")
    void user_delete() throws Exception {
        String jsonRequest = "{\"username\":\"username\", \"password\":\"password\"}";
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        UserEntity user = userRepository.findByEmail("1@naver.com");
        assertFalse(user.isActive());
    }

    @Test
    @DisplayName("회원 로그인 잘 되었나 확인 - rank, accessToken refreshToken memberId값 가져와야함")
    void user_login() throws Exception {
        String jsonRequest = "{\"username\":\"username\", \"password\":\"password\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ResUserLoginDto dto = objectMapper.readValue(contentAsString, new TypeReference<ResUserLoginDto>() {});
        assertNotNull(dto.getAccessToken());
        assertNotNull(dto.getRefreshToken());
        assertNotNull(dto.getMemberId());
        assertNotNull(dto.getMemberId());
        assertEquals(dto.getRank(),0);

        RefreshToken refreshTokenByUserEntityId = refreshTokenRepository.findRefreshTokenByUserEntityId(1L);
        System.out.println(refreshTokenByUserEntityId);
    }

    @Test
    @WithMockUser(username = "username")
    @DisplayName("회원 로그아웃 잘 되었나 확인")
    void user_logout() throws Exception {
        String jsonRequests = "{\"username\":\"username\", \"password\":\"password\"}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequests))
                .andExpect(status().isOk())
                .andReturn();
        //로그인 완료

        UserEntity user = userRepository.findByUsername("username");
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUserEntityId(user.getId());
        System.out.println("refreshToken.getTokenValue() = " + refreshToken.getTokenValue());
        String jsonRequest = "{\"refreshToken\":\"" + refreshToken.getTokenValue() + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
        assertNull(refreshTokenRepository.findRefreshTokenByUserEntityId(user.getId()));
    }

    @Test
    @DisplayName("유저의 닉네임이 잘 변경되나 확인")
    void updateUserTest() throws Exception {
        List<String> roles = new ArrayList<>(List.of("ROLE_USER"));
        String accessToken = jwtTokenizer.createAccessToken(1L,"1@naver.com","nickname",roles);
        System.out.println("accessToken = " + accessToken);

        String jsonRequests = "{\"nickname\":\"nickname2\"}";
        mockMvc.perform(MockMvcRequestBuilders.patch("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonRequests))
                .andExpect(status().isOk());

        assertNotNull(userRepository.findByNickname("nickname2"));
    }

    @Test
    @DisplayName("유저 검색이 잘 되나 확인")
    @WithMockUser(username = "username")
    void searchUser() throws Exception {
        List<String> roles = new ArrayList<>(List.of("ROLE_USER"));
        String accessToken = jwtTokenizer.createAccessToken(1L,"1@naver.com","nickname",roles);
        System.out.println("accessToken = " + accessToken);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/api/search")
                .header("Authorization", "Bearer " + accessToken)
                        .param("keyword","nick"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<String> list = objectMapper.readValue(contentAsString,new TypeReference<>() {});
        assertEquals(list.size(),1);
    }

}