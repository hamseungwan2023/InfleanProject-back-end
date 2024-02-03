package inflearnproject.anoncom.config;


import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.enumType.LocationType;
import inflearnproject.anoncom.role.repository.RoleRepository;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Profile("local")
public class RoleInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
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

            if (!userRepository.existsByUsername("adminUser")) {
                String nickname = "adminNick";
                String password = "adminPass";
                String username = "adminUser";
                String email = "admin@naver.com";
                LocationType location = LocationType.SEOUL;

                UserEntity user = UserEntity.builder()
                        .nickname(nickname)
                        .password(passwordEncoder.encode(password))
                        .username(username)
                        .roles(new HashSet<>())
                        .location(location)
                        .email(email)
                        .isActive(true).build();

                Optional<Role> userRole = roleRepository.findByName("ROLE_ADMIN");
                user.addRole(userRole.get());
                userRepository.save(user);
            }
        };
    }

}
