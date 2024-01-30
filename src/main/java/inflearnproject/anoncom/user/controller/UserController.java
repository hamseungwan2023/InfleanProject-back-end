package inflearnproject.anoncom.user.controller;

import inflearnproject.anoncom.domain.RefreshToken;
import inflearnproject.anoncom.domain.Spam;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.refreshToken.dto.RefreshTokenDto;
import inflearnproject.anoncom.refreshToken.service.RefreshTokenService;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.JwtTokenizer;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import inflearnproject.anoncom.spam.service.SpamService;
import inflearnproject.anoncom.user.dto.*;
import inflearnproject.anoncom.user.service.UserService;
import inflearnproject.anoncom.user.util.FileUploadUtil;
import io.jsonwebtoken.Claims;
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
    private final SpamService spamService;


    @GetMapping
    public ResponseEntity<List<UserFormDto>> allUsers() {
        List<UserEntity> userEntities = userService.allUsers();
        List<UserFormDto> dtos = userEntities.stream().map(user -> new UserFormDto(user.getNickname())).toList();
        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> join(@Valid @RequestPart("reqUserJoinFormDto") ReqUserJoinFormDto reqUserJoinFormDto,
                                  @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
        UserEntity userEntity = userService.buildLoginUser(reqUserJoinFormDto);

        if (profileImg != null) {
            userService.setImage(profileImg, userEntity);
        }

        UserEntity createdUserEntity = userService.joinUser(userEntity);
        ResUserJoinFormDto res = userService.buildJoinForm(createdUserEntity);

        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody UserDeleteFormDto userDeleteFormDto) {
        userService.deleteUser(userDeleteFormDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<ResUserLoginDto> login(@RequestBody ReqUserLoginDto loginDto) {
        ResUserLoginDto loginResponse = refreshTokenService.login(loginDto);
        return ResponseEntity.ok().body(loginResponse);
    }


    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken());
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping("/refreshToken")
    public ResponseEntity<ResUserLoginDto> requestRefresh(@RequestHeader("Authorization") String refreshTokenValue) {

        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenValue);
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken.getTokenValue());

        Long memberId = Long.valueOf((Integer) claims.get("memberId"));
        UserEntity userEntity = userService.findUserEntityById(memberId);


        List roles = (List) claims.get("roles");
        String email = claims.getSubject();

        String accessToken = jwtTokenizer.createAccessToken(memberId, email, userEntity.getUsername(), roles);

        ResUserLoginDto loginResponse = refreshTokenService.buildLoginedInfo(refreshTokenValue, accessToken, userEntity);
        return ResponseEntity.ok().body(loginResponse);
    }


    @GetMapping("/load-profile")
    public ResponseEntity<?> loadProfile(@IfLogin LoginUserDto userDto) throws IOException {

        String profilePath = userService.getProfilePath(userDto.getMemberId());

        String fullPath = uploadRootPath + File.separator + profilePath;
        File targetFile = new File(fullPath);
        if (!targetFile.exists()) return ResponseEntity.notFound().build();
        byte[] rawImageData = FileCopyUtils.copyToByteArray(targetFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(FileUploadUtil.getMediaType(profilePath));

        return ResponseEntity.ok().headers(headers).body(rawImageData);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@IfLogin LoginUserDto userDto, @Valid @RequestPart("userUpdateDto") ReqUserUpdateDto userUpdateDto
            , @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {

        userService.updateUser(userDto.getEmail(), userUpdateDto, profileImg);
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/api/search")
    public ResponseEntity<?> searchUser(@IfLogin LoginUserDto userDto,
                                        @RequestParam(value = "keyword", required = false) String keyword) {
        List<String> nicknamesByNickname = userService.searchUser(keyword, userDto.getMemberId());
        return ResponseEntity.ok().body(nicknamesByNickname);
    }

    @GetMapping("/api/spamUsers")
    public ResponseEntity<List<SpamUsersDto>> searchSpamUsers(@IfLogin LoginUserDto userDto) {
        List<Spam> spams = spamService.searchSpamUser(userDto.getMemberId());
        List<SpamUsersDto> list = spams.stream().map(spam -> new SpamUsersDto(spam.getDeclared().getId(), spam.getDeclared().getNickname())).toList();
        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping("/api/spamUser")
    public ResponseEntity<List<Long>> DeleteSpamUsers(@RequestBody DeleteSpamDto deleteSpamDto) {
        List<Long> fails = spamService.deleteSpamNote(deleteSpamDto);
        return ResponseEntity.ok().body(fails);
    }

    @GetMapping("/api/userDetail")
    public ResponseEntity<UserDetailDto> userDetail(@IfLogin LoginUserDto userDto) {
        UserEntity user = userService.findUserDetail(userDto.getMemberId());
        UserDetailDto userDetailDto = new UserDetailDto(user);
        return ResponseEntity.ok().body(userDetailDto);
    }

}
