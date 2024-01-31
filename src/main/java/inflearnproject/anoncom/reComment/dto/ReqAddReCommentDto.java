package inflearnproject.anoncom.reComment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqAddReCommentDto {

    @Length(min = 2)
    private String content;
}
