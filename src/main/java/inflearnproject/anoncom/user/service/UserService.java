package inflearnproject.anoncom.user.service;

import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity joinUser(UserEntity userEntity){
        userEntity.changeToBCryptPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        return userEntity;
    }
}
