package inflearnproject.anoncom.enumType;

public enum CategoryType {

    PREVIEW("시사"),
    CLUB_AND_STUDY("모임&스터디"),
    USED_TRADE("중고거래"),
    GAME("게임");

    private String typeName;

    CategoryType(String typeName) {
        this.typeName = typeName;
    }
}
