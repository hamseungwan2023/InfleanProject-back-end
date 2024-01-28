package inflearnproject.anoncom.user.service;

import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.user.dto.ReqUserUpdateDto;
import inflearnproject.anoncom.user.dto.UserDeleteFormDto;
import inflearnproject.anoncom.user.exception.BlockedUserException;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import inflearnproject.anoncom.user.exception.NotActiveUser;
import inflearnproject.anoncom.user.exception.WrongPasswordException;
import inflearnproject.anoncom.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static inflearnproject.anoncom.custom.TestServiceUtils.addUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;
    private UserEntity user;
    @BeforeEach
    void before(){
        user = addUser(userService);
    }

    @Test
    @DisplayName("회원가입이 잘 되서 email, username, nickname으로 조회가 되나 확인")
    void joinUser_success(){

        assertNotNull(userRepository.findByEmail(user.getEmail()));
        assertNotNull(userRepository.findByNickname(user.getNickname()));
        assertNotNull(userRepository.findByUsername(user.getUsername()));
    }

    @Test
    @DisplayName("회원가입이 잘 되서 전체 리스트를 조회하면 1, 또 회원가입을 하면 총 2가 잘 나오나 확인")
    void allUsers(){
        List<UserEntity> users = userService.allUsers();
        assertEquals(users.size(),1);

        UserEntity user2 = UserEntity.builder()
                .email("2@naver.com")
                .username("username2")
                .nickname("nickname2")
                .password("password2")
                .roles(new HashSet<>())
                .location("seoul")
                .isActive(true)
                .build();
        userService.joinUser(user2);

        List<UserEntity> users2 = userService.allUsers();
        assertEquals(users2.size(),2);
    }

    @Test
    @DisplayName("username, password 입력해서 회원이 잘 찾아지나 확인 성공")
    void findUser_success(){
        UserEntity findUser = userService.findUser("username", "password");

        assertNotNull(findUser);
    }

    @Test
    @DisplayName("username, password 잘못 입력해서 에러 발생")
    void findUser_fail1(){
        assertThrows(NoUserEntityException.class, () ->  userService.findUser("username1", "password"));
        assertThrows(NoUserEntityException.class, () ->  userService.findUser("username", "password1"));
    }

    @Test
    @DisplayName("username, password 잘 입력했지만 isActive가 false여서 에러 발생")
    void findUser_fail2(){
        user.setActiveFalse();
        assertThrows(NotActiveUser.class, () ->  userService.findUser("username", "password"));
    }

    @Test
    @DisplayName("username, password 잘 입력했지만 관리자가 차단한 계정이여서 에러 발생")
    void findUser_fail3(){
        user.blockUser();
        assertThrows(BlockedUserException.class, () ->  userService.findUser("username", "password"));
    }

    @Test
    @DisplayName("username, password 잘 입력해서 계정 삭제 완료 -> 실제 삭제가 아닌 active만 false로 변경")
    void deleteUser_success1(){
        UserDeleteFormDto userDeleteFormDto = new UserDeleteFormDto("username","password");
        userService.deleteUser(userDeleteFormDto);
        assertFalse(user.isActive());
    }

    @Test
    @DisplayName("username, password 잘못 입력해서 에러 발생")
    void deleteUser_success2(){
        UserDeleteFormDto userDeleteFormDto1 = new UserDeleteFormDto("username1","password");
        UserDeleteFormDto userDeleteFormDto2 = new UserDeleteFormDto("username1","password2");

        assertThrows(NoUserEntityException.class, () -> userService.deleteUser(userDeleteFormDto1));
        assertThrows(NoUserEntityException.class, () -> userService.deleteUser(userDeleteFormDto2));
    }

    @Test
    @DisplayName("닉네임만 입력할 시 닉네임만 변경 완료 - 성공")
    void update_success1(){
        ReqUserUpdateDto dto = new ReqUserUpdateDto();
        dto.setNickname("nickname2");
        userService.updateUser("1@naver.com",dto);
        assertNotNull(userRepository.findByNickname("nickname2"));
    }

    @Test
    @DisplayName("새 비밀번호 입력시 에러 발생, 기존 비밀번호까지 맞춰야지 입력해야 새 비밀번호로 교체 - 성공")
    void update_success_and_fail(){
        ReqUserUpdateDto dto = new ReqUserUpdateDto();
        dto.setNickname("nickname2");
        dto.setNewPassword("password1");

        assertThrows(WrongPasswordException.class,() ->userService.updateUser("1@naver.com",dto));

        dto.setPassword("passwor");
        assertThrows(WrongPasswordException.class,() ->userService.updateUser("1@naver.com",dto));

        dto.setPassword("password");
        userService.updateUser("1@naver.com",dto);
        assertNotNull(userRepository.findByNickname("nickname2"));
        assertTrue(passwordEncoder.matches("password1",userRepository.findByNickname("nickname2").get().getPassword()));
    }

    @Test
    @DisplayName("닉네임 일부를 입력할 시 그 일부를 포함한 닉네임들이 찾아진다")
    void searchUser(){
        String nick = "nick";
        List<String> nicknames = userService.searchUser(nick);
        assertEquals(nicknames.size(),1);

        UserEntity user2 = UserEntity.builder()
                .email("2@naver.com")
                .username("username2")
                .nickname("nickname2")
                .password("password2")
                .roles(new HashSet<>())
                .location("seoul")
                .isActive(true)
                .build();
        userService.joinUser(user2);

        List<String> nicknames2 = userService.searchUser(nick);
        assertEquals(nicknames2.size(),2);
    }
}