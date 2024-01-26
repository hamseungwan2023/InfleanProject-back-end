package inflearnproject.anoncom.config;


import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.role.repository.RoleRepository;
import inflearnproject.anoncom.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

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

            if(!userRepository.existsByUsername("adminUser")){
                UserEntity user = new UserEntity();
                user.setAdminInfo();
                user.addRole(roleRepository.findById(2L).get());

                userRepository.save(user);
            }
        };
    }

}
