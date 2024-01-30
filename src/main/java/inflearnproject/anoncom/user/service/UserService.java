package inflearnproject.anoncom.user.service;

import inflearnproject.anoncom.domain.*;
import inflearnproject.anoncom.note.dto.NoteSpamDto;
import inflearnproject.anoncom.note.exception.NoSuchNoteException;
import inflearnproject.anoncom.role.repository.RoleRepository;
import inflearnproject.anoncom.spam.repository.SpamRepository;
import inflearnproject.anoncom.user.dto.DeleteSpamDto;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static inflearnproject.anoncom.domain.QUserEntity.userEntity;
import static org.springframework.util.StringUtils.*;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${upload.path}")
    private String uploadRootPath;

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final SpamRepository spamRepository;
    private final UserSpamService userSpamService;

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
        if(!user.isActive()){
            throw new NotActiveUser("해당 계정은 비활성화(삭제)된 상태입니다.");
        }
        if(user.getIsBlocked() != null && user.getIsBlocked()){
            LocalDateTime blockUntil = user.getBlockUntil();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            String formattedDate = blockUntil.format(formatter);
            throw new BlockedUserException("해당 계정은 관리자에 의해 정지 상태입니다. 정지 기한은 " + formattedDate + "까지입니다.");
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
            user.setActiveFalse();
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

    public void updateUser(String email, ReqUserUpdateDto userDto, MultipartFile profileImg) {
        UserEntity user = userRepository.findByEmail(email);

        try {
            setImage(profileImg,user);
        } catch (IOException e) {
            throw new FailImageLoadingException("이미지 업로드 중 문제가 발생했습니다.");
        }

        if(!hasText(userDto.getNewPassword())){
            user.updateNickname(userDto.getNickname());
        }else{
            if(hasText(userDto.getPassword()) && bCryptPasswordEncoder.matches(userDto.getPassword(), user.getPassword())){
                user.updateNickname(userDto.getNickname());
                user.changeToBCryptPassword(bCryptPasswordEncoder.encode(userDto.getNewPassword()));
            }else{
                throw new WrongPasswordException("비밀번호가 일치하지 않습니다.");
            }
        }
    }

    public void setImage(MultipartFile profileImg, UserEntity user) throws IOException {
        if(profileImg != null) {
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

    public List<String> searchUser(String nickname,Long userId){
        String nicknameAdded = "%" + nickname + "%";
        return userRepository.findNicknamesByNickname(nicknameAdded,userId);
    }

    public List<Spam> searchSpamUser(Long memberId) {
        UserEntity declaring = userRepository.findById(memberId).get();

        return spamRepository.findAllByDeclaring(declaring);
    }

    public List<Long> deleteSpamNote(DeleteSpamDto deleteSpamDto) {

        List<Long> deleteSpams = new ArrayList<>();
        for (Long deleteSpamId : deleteSpamDto.getDeleteSpamIds()) {
            try{
                userSpamService.deleteSpam(deleteSpamId);
            }catch (NoSuchNoteException e){
                long errorId = Long.parseLong(e.getMessage());
                deleteSpams.add(errorId);
            }
        }
        return deleteSpams;
    }

    public UserEntity findUserDetail(Long memberId) {
        return userRepository.findById(memberId).get();
    }
}
