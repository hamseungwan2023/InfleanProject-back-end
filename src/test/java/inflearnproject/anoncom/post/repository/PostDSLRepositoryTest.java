package inflearnproject.anoncom.post.repository;

import inflearnproject.anoncom.domain.Post;
import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.post.dto.PostSearchCondition;
import inflearnproject.anoncom.post.dto.ResAddPostDto;
import inflearnproject.anoncom.role.repository.RoleRepository;
import inflearnproject.anoncom.user.dto.ReqUserJoinFormDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import inflearnproject.anoncom.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostDSLRepositoryTest {

    @Autowired
    private PostDSLRepository postDSLRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
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
        UserEntity users = new UserEntity(req);
        userService.joinUser(users);

        UserEntity user = userRepository.findByUsername("username");
        Post post1 = Post.builder().content("컨텐트1")
                .title("타이틀1")
                .userLike(0)
                .userDisLike(0)
                .views(0)
                .category("카테고리1").build();
        post1.putUser(user);

        Post post2 = Post.builder().content("컨텐트2")
                .title("타이틀2")
                .userLike(0)
                .userDisLike(0)
                .views(0)
                .category("카테고리2").build();
        post2.putUser(user);

        Post post3 = Post.builder().content("컨텐트3")
                .title("타이틀3")
                .userLike(0)
                .userDisLike(0)
                .views(0)
                .category("카테고리3").build();
        post3.putUser(user);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

}