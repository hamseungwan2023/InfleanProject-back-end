package inflearnproject.anoncom.alarm.dto;

import inflearnproject.anoncom.domain.Alarm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmShowDto {

    private Long id;
    private String content;

    public AlarmShowDto(Alarm alarm) {
        this.id = alarm.getId();
        this.content = alarm.getMessage();
    }
}
