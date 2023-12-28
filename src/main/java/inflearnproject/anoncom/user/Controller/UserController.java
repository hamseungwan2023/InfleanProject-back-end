package inflearnproject.anoncom.user.Controller;

import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.user.dto.ResUserJoinFormDto;
import inflearnproject.anoncom.user.dto.UserDeleteFormDto;
import inflearnproject.anoncom.user.dto.UserFormDto;
import inflearnproject.anoncom.user.dto.ReqUserJoinFormDto;
import inflearnproject.anoncom.user.exception.SameInfoUserEntityException;
import inflearnproject.anoncom.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


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
}
