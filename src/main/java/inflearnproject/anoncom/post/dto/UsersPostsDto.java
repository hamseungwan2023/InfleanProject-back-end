package inflearnproject.anoncom.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UsersPostsDto {

    private List<ResPostDetailDto> resPostDetailDtos;
    private int currentPage;
    private int totalPage;
}
