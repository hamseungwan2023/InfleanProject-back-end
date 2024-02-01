package inflearnproject.anoncom.enumType;

public enum LocationType {
    SEOUL("서울"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    INCHEON("인천"),
    DAEJEON("대전"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    DAEGU("대구"),
    BUSAN("부산"),
    JEONNAM("전남"),
    JEONBUK("전북"),
    GWANGJU("광주"),
    JEJU("제주");

    private String name;

    LocationType(String name) {
        this.name = name;
    }

}
