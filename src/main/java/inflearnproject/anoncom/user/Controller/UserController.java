package inflearnproject.anoncom.user.Controller;

import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.user.dto.UserDeleteFormDto;
import inflearnproject.anoncom.user.dto.UserFormDto;
import inflearnproject.anoncom.user.dto.UserJoinFormDto;
import inflearnproject.anoncom.user.repository.UserRepository;
import inflearnproject.anoncom.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/join")
    public ResponseEntity<UserJoinFormDto> join(@Valid @RequestBody UserJoinFormDto userJoinFormDto){

        UserEntity userEntity = new UserEntity(userJoinFormDto);
        UserEntity createdUserEntity = userService.joinUser(userEntity);
        UserJoinFormDto createdUserJoinFormDto = new UserJoinFormDto(createdUserEntity);

        return ResponseEntity.ok().body(createdUserJoinFormDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody UserDeleteFormDto userDeleteFormDto){
        userService.deleteUser(userDeleteFormDto);
        return ResponseEntity.ok().build();
    }
}
