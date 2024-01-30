package inflearnproject.anoncom.user.service;

import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.role.repository.RoleRepository;
import inflearnproject.anoncom.user.dto.ReqUserUpdateDto;
import inflearnproject.anoncom.user.dto.UserDeleteFormDto;
import inflearnproject.anoncom.user.exception.*;
import inflearnproject.anoncom.user.repository.UserRepository;
import inflearnproject.anoncom.user.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static inflearnproject.anoncom.user.exception.ExceptionMessage.*;
import static org.springframework.util.StringUtils.hasText;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${upload.path}")
    private String uploadRootPath;

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserEntity> allUsers() {
        return userRepository.findAll();
    }

    public UserEntity findUser(String usernameOrEmail, String password) {
        UserEntity user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new NoUserEntityException(NO_SAME_INFO_USER_MESSAGE));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new NoUserEntityException(NO_SAME_INFO_USER_MESSAGE);
        }
        if (!user.isActive()) {
            throw new NotActiveUser(IS_ACTIVE_FALSE_USER);
        }
        if (Boolean.TRUE.equals(user.getIsBlocked())) {
            throwBlockedUserError(user);
        }
        return user;
    }

    private void throwBlockedUserError(UserEntity user) {
        LocalDateTime blockUntil = user.getBlockUntil();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedDate = blockUntil.format(formatter);
        String errorMessage = String.format(BLOCKED_USER_MESSAGE_TEMPLATE, formattedDate);
        throw new BlockedUserException(errorMessage);
    }

    public UserEntity joinUser(UserEntity user) {
        if (userRepository.existsUserEntity(user.getEmail(), user.getNickname(), user.getUsername())) {
            throw new SameInfoUserEntityException(ALREADY_SAME_INFO_USER);
        }
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        user.addRole(userRole.get());
        user.changeToBCryptPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public void deleteUser(UserDeleteFormDto userDeleteFormDto) {
        UserEntity user = userRepository.findByUsername(userDeleteFormDto.getUsername());
        if (user == null) {
            throw new NoUserEntityException(NO_SAME_INFO_USER_MESSAGE);
        }
        if (!bCryptPasswordEncoder.matches(userDeleteFormDto.getPassword(), user.getPassword())) {
            throw new NoUserEntityException(NO_SAME_INFO_USER_MESSAGE);
        }
        if (bCryptPasswordEncoder.matches(userDeleteFormDto.getPassword(), user.getPassword())) {
            user.setActiveFalse();
        }
    }

    public void updateUser(String email, ReqUserUpdateDto userDto, MultipartFile profileImg) {
        UserEntity user = userRepository.findByEmail(email);

        try {
            setImage(profileImg, user);
        } catch (IOException e) {
            throw new FailImageLoadingException(UNKNOWN_IMAGE_UPLOAD_ERROR);
        }

        if (!hasText(userDto.getNewPassword())) {
            user.updateNickname(userDto.getNickname());
        } else {
            if (hasText(userDto.getPassword()) && bCryptPasswordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                user.updateNickname(userDto.getNickname());
                user.changeToBCryptPassword(bCryptPasswordEncoder.encode(userDto.getNewPassword()));
            } else {
                throw new WrongPasswordException(DIFFERENT_PASSWORD);
            }
        }
    }

    public void setImage(MultipartFile profileImg, UserEntity user) throws IOException {
        if (profileImg != null) {
            String originalFilename = profileImg.getOriginalFilename();
            String uploadFileName = UUID.randomUUID() + "_" + originalFilename;
            String newUploadPath = FileUploadUtil.makeUploadDirectory(uploadRootPath);
            File uploadFile = new File(newUploadPath + File.separator + uploadFileName);
            profileImg.transferTo(uploadFile);
            String savePath
                    = newUploadPath.substring(uploadRootPath.length());

            user.setProfileImg(savePath + File.separator + uploadFileName);
        }
    }

    @Transactional(readOnly = true)
    public String getProfilePath(Long id) {
        String profile = userRepository.findProfileImgById(id);
        return profile;
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findUserEntityById(Long memberId) {
        return userRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public List<String> searchUser(String nickname, Long userId) {
        String nicknameAdded = "%" + nickname + "%";
        return userRepository.findNicknamesByNickname(nicknameAdded, userId);
    }

    @Transactional(readOnly = true)
    public UserEntity findUserDetail(Long memberId) {
        return userRepository.findById(memberId).orElseThrow(() -> new NoUserEntityException(NO_SAME_INFO_USER_MESSAGE));
    }
}
