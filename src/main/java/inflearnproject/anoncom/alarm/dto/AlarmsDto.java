package inflearnproject.anoncom.alarm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AlarmsDto {

    private List<AlarmShowDto> dtos;
    private int page;
    private int totalPages;

    public AlarmsDto(List<AlarmShowDto> dtos, int page, int totalPages) {
        this.dtos = dtos;
        this.page = page;
        this.totalPages = totalPages;
    }
}
