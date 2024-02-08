package inflearnproject.anoncom.alarm.controller;

import inflearnproject.anoncom.alarm.dto.AlarmShowDto;
import inflearnproject.anoncom.alarm.service.AlarmService;
import inflearnproject.anoncom.domain.Alarm;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/alarm")
    public ResponseEntity<List<AlarmShowDto>> getAlarms(@IfLogin LoginUserDto userDto) {
        List<Alarm> alarms = alarmService.getAlarms(userDto.getMemberId());
        List<AlarmShowDto> dtos = alarms.stream().map(AlarmShowDto::new).toList();
        return ResponseEntity.ok().body(dtos);
    }

    @DeleteMapping("/alarm/{alarmId}")
    public ResponseEntity<?> deleteOneAlarm(@IfLogin LoginUserDto userDto, @PathVariable("alarmId") Long alarmId) {
        alarmService.deleteOneAlarm(userDto.getMemberId(), alarmId);
        return ResponseEntity.ok().body("ok");
    }

    @DeleteMapping("/alarm")
    public ResponseEntity<?> deleteAlarms(@IfLogin LoginUserDto userDto) {
        alarmService.deleteAlarms(userDto.getMemberId());
        return ResponseEntity.ok().body("ok");
    }
}
