package inflearnproject.anoncom.user.Controller;

import inflearnproject.anoncom.domain.UserEntity;
import inflearnproject.anoncom.user.dto.UserFormDto;
import inflearnproject.anoncom.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserFormDto> join(@Valid @RequestBody UserFormDto userFormDto){

        UserEntity userEntity = new UserEntity(userFormDto);
        UserEntity createdUserEntity = userService.joinUser(userEntity);
        UserFormDto createdUserFormDto = new UserFormDto(createdUserEntity);

        return ResponseEntity.ok().body(createdUserFormDto);
    }
}
