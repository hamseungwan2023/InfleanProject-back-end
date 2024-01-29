package inflearnproject.anoncom.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteSpamDto {

    private List<Long> deleteSpamIds;
}
