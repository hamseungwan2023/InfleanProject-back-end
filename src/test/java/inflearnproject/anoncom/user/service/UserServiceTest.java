package inflearnproject.anoncom.user.service;

import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.role.repository.RoleRepository;
import inflearnproject.anoncom.user.dto.ReqUserJoinFormDto;
import inflearnproject.anoncom.user.dto.UserDeleteFormDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void before(){
        if (roleRepository.count() == 0) { // role 테이블에 데이터가 없을 경우
            Role userRole = new Role();
            userRole.setRoleId(1L);
            userRole.setName("ROLE_USER");

            Role adminRole = new Role();
            adminRole.setRoleId(2L);
            adminRole.setName("ROLE_ADMIN");

            roleRepository.save(userRole);
            roleRepository.save(adminRole);
        }

        ReqUserJoinFormDto req = ReqUserJoinFormDto.builder().email("1@naver.com")
                .username("username")
                .password("password")
                .nickname("nickname").build();
        UserEntity user = new UserEntity(req);
        userService.joinUser(user);
    }

    @Test
    @DisplayName("저장이 잘 됐나 확인")
    void signup_success(){
        UserEntity user = userService.findUser("username", "password");
        assertNotNull(user);
    }

    @Test
    @DisplayName("삭제가 잘 됐나 확인")
    void delete_success(){
        UserDeleteFormDto req = UserDeleteFormDto.builder().password("password")
                .username("username").build();
        userService.deleteUser(req);
        assertFalse(userRepository.existsByUsername("username"));
    }
}