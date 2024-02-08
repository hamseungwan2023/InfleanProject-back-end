package inflearnproject.anoncom.alarm.controller;

import inflearnproject.anoncom.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AlarmController {

    private final AlarmService alarmService;


}
