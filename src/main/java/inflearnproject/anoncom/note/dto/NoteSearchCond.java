package inflearnproject.anoncom.note.dto;

import lombok.Data;

@Data
public class NoteSearchCond {


    private Boolean isSpam; //스팸 여부

    private Boolean isKeep; //보관 여부

    private Boolean isReceiverDelete; //수신자 삭제 여부

    private Integer unit; //몇 개 단위로 쪽지를 가져올건가

}
