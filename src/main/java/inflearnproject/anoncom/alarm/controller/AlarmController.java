package inflearnproject.anoncom.alarm.controller;

import inflearnproject.anoncom.alarm.dto.AlarmShowDto;
import inflearnproject.anoncom.alarm.dto.AlarmsDto;
import inflearnproject.anoncom.alarm.service.AlarmService;
import inflearnproject.anoncom.domain.Alarm;
import inflearnproject.anoncom.security.jwt.util.IfLogin;
import inflearnproject.anoncom.security.jwt.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/alarm")
    public ResponseEntity<AlarmsDto> getAlarms(@IfLogin LoginUserDto userDto, @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Alarm> alarms = alarmService.getAlarms(userDto.getMemberId(), pageable);

        List<AlarmShowDto> dtos = alarms.getContent().stream().map(AlarmShowDto::new).toList();
        return ResponseEntity.ok().body(new AlarmsDto(dtos, page, alarms.getTotalPages()));
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
