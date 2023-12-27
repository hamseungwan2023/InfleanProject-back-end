package inflearnproject.anoncom.user.service;

import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.user.dto.UserDeleteFormDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public List<UserEntity> allUsers(){
        return userRepository.findAll();
    }

    public UserEntity joinUser(UserEntity userEntity){
        userEntity.changeToBCryptPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return userEntity;
    }

    public void deleteUser(UserDeleteFormDto userDeleteFormDto){

        UserEntity userEntity = userRepository.findByUsername(userDeleteFormDto.getUsername());

        if(bCryptPasswordEncoder.matches(userDeleteFormDto.getPassword(), userEntity.getPassword())){
            userRepository.delete(userEntity);
        }
    }
}
