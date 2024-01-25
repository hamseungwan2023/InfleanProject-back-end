package inflearnproject.anoncom.user.Controller;

import inflearnproject.anoncom.domain.RefreshToken;
import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.error.ErrorDTO;
import inflearnproject.anoncom.post.dto.PostSearchCondition;
import inflearnproject.anoncom.refreshToken.dto.RefreshTokenDto;
import inflearnproject.anoncom.refreshToken.repository.RefreshTokenRepository;
import inflearnproject.anoncom.refreshToken.service.RefreshTokenService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.JwtTokenizer;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.user.dto.*;
import inflearnproject.anoncom.user.repository.UserRepository;
import inflearnproject.anoncom.user.service.UserService;
import inflearnproject.anoncom.user.util.FileUploadUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Value("${upload.path}")
    private String uploadRootPath;

    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @GetMapping
    public ResponseEntity<List<UserFormDto>> allUsers(){
        List<UserEntity> userEntities = userService.allUsers();
        List<UserFormDto> dtos = userEntities.stream().map(user -> new UserFormDto(user.getNickname())).toList();
        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> join(@Valid @RequestPart("reqUserJoinFormDto") ReqUserJoinFormDto reqUserJoinFormDto,
                                  @RequestPart(value = "profileImg",required = false) MultipartFile profileImg) {

        try {
            UserEntity userEntity = new UserEntity(reqUserJoinFormDto);

            ifProfileImgNotNull(profileImg, userEntity);
            UserEntity createdUserEntity = userService.joinUser(userEntity);
            ResUserJoinFormDto res = ResUserJoinFormDto.builder().nickname(createdUserEntity.getNickname())
                    .email(createdUserEntity.getEmail()).build();

            return ResponseEntity.ok().body(res);

        }catch(RuntimeException | IOException e){
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }

    }

    private void ifProfileImgNotNull(MultipartFile profileImg, UserEntity userEntity) throws IOException {
        if(profileImg != null){
            String originalFilename = profileImg.getOriginalFilename();
            String uploadFileName = UUID.randomUUID() + "_" + originalFilename;
            String newUploadPath = FileUploadUtil.makeUploadDirectory(uploadRootPath);
            File uploadFile = new File(newUploadPath + File.separator + uploadFileName);
            profileImg.transferTo(uploadFile);
            String savePath
                    = newUploadPath.substring(uploadRootPath.length());

            userEntity.setProfileImg(savePath + File.separator + uploadFileName);

        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody UserDeleteFormDto userDeleteFormDto){
        userService.deleteUser(userDeleteFormDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 로그인을 하게되면 프론트엔드 쪽에서 accessToken localStorage에 저장
     */
    @PostMapping("/login")
    public ResponseEntity<ResUserLoginDto> login(@RequestBody ReqUserLoginDto loginDto) {

        UserEntity user = userService.findUser(loginDto.getUsername(),loginDto.getPassword());
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getUsername(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getUsername(), roles);

        // RefreshToken을 DB에 저장한다. 성능 때문에 DB가 아니라 Redis에 저장하는 것이 좋다.
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setTokenValue(refreshToken);
        refreshTokenEntity.setUserEntityId(user.getId());
        refreshTokenService.addRefreshToken(refreshTokenEntity,user.getId());

        ResUserLoginDto loginResponse = ResUserLoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(user.getId())
                .nickname(user.getUsername())
                .rank(user.getRank())
                .build();
        return ResponseEntity.ok().body(loginResponse);
    }

    /**
     * 로그아웃 시 localStorage쪽의 refreshtoken, access토큰 모두 지우기
     */
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * accessToken의 만료시간이 현재시간을 지나면 이 메서드를 호출하게 만들어서 refreshToken을 통해 accessToken을 재발급받음
     * @param refreshTokenDto
     * @return
     */
    @PostMapping("/refreshToken")
    public ResponseEntity<ResUserLoginDto> requestRefresh(@IfLogin LoginUserDto userDto, @RequestBody RefreshTokenDto refreshTokenDto) {

        UserEntity user = userRepository.findByEmail(userDto.getEmail());
        RefreshToken refreshTokenByUserEntityId = refreshTokenRepository.findRefreshTokenByUserEntityId(user.getId());
        refreshTokenDto.setRefreshToken(refreshTokenByUserEntityId.getTokenValue());

        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenDto.getRefreshToken()).orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken.getTokenValue());

        Long memberId = Long.valueOf((Integer)claims.get("memberId"));

        UserEntity userEntity = userService.findUserEntityById(memberId).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));


        List roles =  (List) claims.get("roles");
        String email = claims.getSubject();

        String accessToken = jwtTokenizer.createAccessToken(memberId, email, userEntity.getUsername(), roles);

        ResUserLoginDto loginResponse = ResUserLoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenDto.getRefreshToken())
                .memberId(userEntity.getId())
                .nickname(userEntity.getUsername())
                .build();
        return  ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping("/load-profile")
    public ResponseEntity<?> loadProfile(@IfLogin LoginUserDto userDto) throws IOException {

        String profilePath = userService.getProfilePath(userDto.getMemberId());

        String fullPath = uploadRootPath + File.separator + profilePath;
        File targetFile = new File(fullPath);
        if(!targetFile.exists()) return ResponseEntity.notFound().build();
        byte[] rawImageData = FileCopyUtils.copyToByteArray(targetFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(FileUploadUtil.getMediaType(profilePath));

        return ResponseEntity.ok().headers(headers).body(rawImageData);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@IfLogin LoginUserDto userDto, @RequestBody ReqUserUpdateDto userUpdateDto){
        userService.updateUser(userDto.getEmail(),userUpdateDto);
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/api/search")
    public ResponseEntity<?> searchUser(@IfLogin LoginUserDto userDto,
                                        @ModelAttribute(value = "keyword") String keyword){
        List<String> nicknamesByNickname = userService.searchUser(keyword);
        return ResponseEntity.ok().body(nicknamesByNickname);
    }
}
