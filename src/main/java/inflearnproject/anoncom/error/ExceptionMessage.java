package inflearnproject.anoncom.error;

public abstract class ExceptionMessage {

    public static final String NO_POST_MESSAGE = "해당 게시글은 존재하지 않습니다.";
    public static final String NO_COMMENT_MESSAGE = "해당 댓글은 존재하지 않습니다.";
    public static final String NO_SAME_INFO_USER_MESSAGE = "해당 정보와 일치하는 회원이 존재하지 않습니다";
    public static final String IS_ACTIVE_FALSE_USER = "해당 계정은 비활성화(삭제)된 상태입니다.";
    public static final String BLOCKED_USER_MESSAGE_TEMPLATE = "해당 계정은 관리자에 의해 정지 상태입니다. 정지 기한은 %s까지입니다.";
    public static final String ALREADY_SAME_INFO_USER = "동일한 회원 정보가 이미 존재합니다";
    public static final String DIFFERENT_PASSWORD = "비밀번호가 일치하지 않습니다.";
    public static final String UNKNOWN_IMAGE_UPLOAD_ERROR = "이미지 업로드 중 문제가 발생했습니다.";
    public static final String BE_RIGHT_LENGTH = "글자 수와 형식에 맞게 작성해주십시오";
    public static final String NOT_SAME_USER = "동일한 사용자가 아니기에 접근할 수 없습니다.";
}
