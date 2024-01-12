package inflearnproject.anoncom.post.repository;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.role.repository.RoleRepository;
import inflearnproject.anoncom.user.dto.ReqUserJoinFormDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import inflearnproject.anoncom.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired UserRepository userRepository;

    @Autowired PostRepository postRepository;
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
    @DisplayName("post가 잘 저장되나 확인하기")
    void post_add_success(){
        UserEntity user = userRepository.findByUsername("username");
        Post post = Post.builder().content("컨텐트")
                .title("타이틀")
                .userLike(0)
                .userDisLike(0)
                .views(0)
                .category("카테고리").build();
        post.putUser(user);

        postRepository.save(post);
        Post post1 = postRepository.findById(1L).get();
        assertThat(post1.getId()).isEqualTo(1L);
        assertThat(post1.getContent()).isEqualTo("컨텐트");
        assertThat(post1.getTitle()).isEqualTo("타이틀");
        assertThat(post1.getUserLike()).isEqualTo(0);
        assertThat(post1.getUser()).isEqualTo(user);

    }

    @Test
    @DisplayName("post가 잘 삭제되나 확인하기")
    void post_delete_success(){
        UserEntity user = userRepository.findByUsername("username");
        Post post = Post.builder().content("컨텐트")
                .title("타이틀")
                .userLike(0)
                .userDisLike(0)
                .views(0)
                .category("카테고리").build();
        post.putUser(user);

        postRepository.save(post);

        postRepository.delete(post);
        assertFalse(postRepository.findById(1L).isPresent());
    }
}