package com.example.kuleumbridge.Notice;

import java.util.HashMap;

public class NoticeHandler {
    private static HashMap<String, String> firstLink = createFirstLink();
    private static HashMap<String, String> secondLink = createSecondLink();

    private static HashMap<String, Integer> indexMap = createIndexMap();
    private static HashMap<Integer, String> categoryMap = createCategoryMap();

    private static HashMap<Integer, String> createCategoryMap()
    {
        HashMap<Integer, String> map = new HashMap<Integer, String>();

        map.put(0, "학사");
        map.put(1, "장학");
        map.put(2, "취창업");
        map.put(3, "국제");
        map.put(4, "학생");
        map.put(5, "산학");
        map.put(6, "일반");

        return map;
    }

    private static HashMap<String, Integer> createIndexMap()
    {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("학사", 0);
        map.put("장학", 1);
        map.put("취창업", 2);
        map.put("국제", 3);
        map.put("학생", 4);
        map.put("산학", 5);
        map.put("일반", 6);

        return map;
    }

    private static HashMap<String, String> createFirstLink()
    {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("학사", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=notice?&sort=6&id=");
        map.put("장학", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=11688412?&sort=6&id=");
        map.put("취창업", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=11731332?&sort=6&id=");
        map.put("국제", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=notice?&sort=6&id=");
        map.put("학생", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=notice?&sort=6&id=");
        map.put("산학", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=65659?&sort=6&id=");
        map.put("일반", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=notice?&sort=6&id=");

        return map;
    }

    private static HashMap<String, String> createSecondLink()
    {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("학사", "&cat=0000300001");
        map.put("장학", "&cat=");
        map.put("취창업", "&cat=");
        map.put("국제", "&cat=0000300002");
        map.put("학생", "&cat=0000300003");
        map.put("산학", "&cat=");
        map.put("일반", "&cat=0000300006");

        return map;
    }

    public static String getLink(String key, String id)
    {
        return firstLink.get(key) + id + secondLink.get(key);
    }

    public static int getIndex(String key)
    {
        return indexMap.get(key);
    }

    public static String getCategory(int index)
    {
        return categoryMap.get(index);
    }
}
