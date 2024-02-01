package inflearnproject.anoncom.enumType;

import static inflearnproject.anoncom.enumType.CategoryType.*;

public enum PostCategory {

    IT(PREVIEW, "IT"),
    PREVIEW_GAME(PREVIEW, "게임"),
    EDUCATION(PREVIEW, "게임"),
    ENTERTAINMENTS(PREVIEW, "연예"),
    MOVIE(PREVIEW, "영화"),

    STUDY(CLUB_AND_STUDY, "스터디"),
    CLUB(CLUB_AND_STUDY, "동호회"),
    NEIGHBORHOOD(CLUB_AND_STUDY, "동네친구"),

    BUY(USED_TRADE, "삽니다"),
    SELL(USED_TRADE, "팝니다"),
    REVIEW(USED_TRADE, "후기"),

    LOL(GAME, "LOL"),
    BATTLEGROUND(GAME, "배그"),
    OVERWATCH(GAME, "오버워치");

    private CategoryType type;
    private String name;

    PostCategory(CategoryType type, String name) {
        this.type = type;
        this.name = name;
    }
}
