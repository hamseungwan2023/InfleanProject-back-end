package inflearnproject.anoncom.user.Controller;

import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    @DisplayName("회원가입이 잘 되었나")
    void signup_success() throws Exception {
        // JSON 데이터를 문자열로 준비
        String jsonRequest = "{\"nickname\":\"nickname\", \"username\":\"username\", \"password\":\"password\", \"email\":\"1@naver.com\"}";

        // MockMultipartFile을 사용하여 multipart 요청을 준비
        MockMultipartFile jsonFile = new MockMultipartFile("reqUserJoinFormDto", "", "application/json", jsonRequest.getBytes());

        // MockMvc를 사용하여 multipart/form-data 요청을 보냄
        mockMvc.perform(MockMvcRequestBuilders.multipart("/user/signup")
                        .file(jsonFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
        UserEntity user = userRepository.findByUsername("username");
        assertEquals(user.getNickname(),"nickname");
        assertEquals(user.getUsername(),"username");
        assertTrue(passwordEncoder.matches("password",user.getPassword()));
        assertEquals(user.getEmail(),"1@naver.com");

    }
}