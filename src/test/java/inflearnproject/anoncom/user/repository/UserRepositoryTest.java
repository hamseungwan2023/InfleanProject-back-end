package inflearnproject.anoncom.user.repository;

import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.role.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Test
    @DisplayName("adminUser있나 -> profile 테스트")
    void findByUsername(){
        List<UserEntity> all = userRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("rolerepository는 1과 2가 들어가 있어야 한다 -> profile 테스트")
    void findAllRoleRepository(){
        List<Role> all = roleRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }
}