package inflearnproject.anoncom.user.Controller;

import inflearnproject.anoncom.domain.RefreshToken;
import inflearnproject.anoncom.domain.Role;
import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.refreshToken.service.RefreshTokenService;
import inflearnproject.anoncom.security.jwt.util.JwtTokenizer;
import inflearnproject.anoncom.user.dto.*;
import inflearnproject.anoncom.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;


    @GetMapping()
    public ResponseEntity<List<UserFormDto>> allUsers(){
        List<UserEntity> userEntities = userService.allUsers();
        List<UserFormDto> dtos = userEntities.stream().map(user -> new UserFormDto(user.getNickname())).toList();
        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/signup")
    public ResponseEntity<ResUserJoinFormDto> join(@Valid @RequestBody ReqUserJoinFormDto reqUserJoinFormDto){

        UserEntity userEntity = new UserEntity(reqUserJoinFormDto);
        UserEntity createdUserEntity = userService.joinUser(userEntity);
        ResUserJoinFormDto res = ResUserJoinFormDto.builder().nickname(createdUserEntity.getNickname())
                .email(createdUserEntity.getEmail()).build();

        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody UserDeleteFormDto userDeleteFormDto){
        userService.deleteUser(userDeleteFormDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<ResUserLoginDto> login(@RequestBody @Valid ReqUserLoginDto loginDto) {

        UserEntity user = userService.findUser(loginDto.getUsername(),loginDto.getPassword());
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());


        String accessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getUsername(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getUsername(), roles);

        // RefreshToken을 DB에 저장한다. 성능 때문에 DB가 아니라 Redis에 저장하는 것이 좋다.
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setTokenValue(refreshToken);
        refreshTokenEntity.setUserEntityId(user.getId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        ResUserLoginDto loginResponse = ResUserLoginDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(user.getId())
                .nickname(user.getUsername())
                .build();
        return ResponseEntity.ok().body(loginResponse);
    }
}
