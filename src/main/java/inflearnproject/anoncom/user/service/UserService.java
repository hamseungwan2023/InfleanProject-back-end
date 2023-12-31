package inflearnproject.anoncom.user.service;

import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.role.repository.RoleRepository;
import inflearnproject.anoncom.user.dto.UserDeleteFormDto;
import inflearnproject.anoncom.user.exception.NoUserEntityException;
import inflearnproject.anoncom.user.exception.SameInfoUserEntityException;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserEntity> allUsers(){
        return userRepository.findAll();
    }

    public UserEntity findUser(String username, String password){
        UserEntity user = null;
        if(!userRepository.existsByUsername(username) && !userRepository.existsByEmail(username)){
            throw new NoUserEntityException("해당 정보와 일치하는 회원이 존재하지 않습니다");
        }else{
            if(userRepository.existsByUsername(username)){
                user = userRepository.findByUsername(username);
            }else if(userRepository.existsByEmail(username)){
                user = userRepository.findByEmail(username);
            }
        }
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new NoUserEntityException("해당 정보와 일치하는 회원이 존재하지 않습니다");
        }
        return user;
    }

    public UserEntity joinUser(UserEntity userEntity){
        if(existsUserEntity(userEntity)){
            throw new SameInfoUserEntityException("동일한 회원 정보가 이미 존재합니다");
        }
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        userEntity.addRole(userRole.get());
        userEntity.changeToBCryptPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));

        return userRepository.save(userEntity);
    }

    public void deleteUser(UserDeleteFormDto userDeleteFormDto){
        if(!userRepository.existsByUsername(userDeleteFormDto.getUsername())){
            throw new NoUserEntityException("해당 정보와 일치하는 회원이 존재하지 않습니다");
        }
        UserEntity user = userRepository.findByUsername(userDeleteFormDto.getUsername());
        if(!bCryptPasswordEncoder.matches(userDeleteFormDto.getPassword(), user.getPassword())){
            throw new NoUserEntityException("해당 정보와 일치하는 회원이 존재하지 않습니다");
        }

        if(bCryptPasswordEncoder.matches(userDeleteFormDto.getPassword(), user.getPassword())){
            userRepository.delete(user);
        }
    }

    @Transactional
    public boolean existsUserEntity(UserEntity userEntity){
        return existsByEmail(userEntity.getEmail()) || existsByNickname(userEntity.getNickname()) || existsByUsername(userEntity.getUsername());
    }

    public String getProfilePath(Long id){
        String profile = userRepository.findProfileImgById(id);
        return profile;
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByNickname(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findUserEntityById(Long memberId){
        return userRepository.findById(memberId);
    }
}
