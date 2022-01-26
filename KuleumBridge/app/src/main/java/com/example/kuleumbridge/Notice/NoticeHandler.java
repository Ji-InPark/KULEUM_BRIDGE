package com.example.kuleumbridge.Notice;

import com.example.kuleumbridge.R;

import java.util.HashMap;

public class NoticeHandler {
    private static HashMap<String, String> firstLink = createFirstLink();
    private static HashMap<String, String> secodLink = createSecondLink();

    private static HashMap<String, Integer> indexMap = createMap();

    private static HashMap<String, Integer> createMap()
    {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("Haksa", 0);
        map.put("Janghak", 1);
        map.put("Chwichangup", 2);
        map.put("Gukje", 3);
        map.put("Haksaeng", 4);
        map.put("Sanhak", 5);
        map.put("Ilban", 6);

        return map;
    }

    private static HashMap<String, String> createFirstLink()
    {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("Haksa", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=notice?&sort=6&id=");
        map.put("Janghak", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=11688412?&sort=6&id=");
        map.put("Chwichangup", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=11731332?&sort=6&id=");
        map.put("Gukje", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=notice?&sort=6&id=");
        map.put("Haksaeng", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=notice?&sort=6&id=");
        map.put("Sanhak", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=65659?&sort=6&id=");
        map.put("Ilban", "http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp?src=http://www.konkuk.ac.kr:80/do/MessageBoard/ArticleRead.do?forum=notice?&sort=6&id=");

        return map;
    }

    private static HashMap<String, String> createSecondLink()
    {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("Haksa", "&cat=0000300001");
        map.put("Janghak", "&cat=");
        map.put("Chwichangup", "&cat=");
        map.put("Gukje", "&cat=0000300002");
        map.put("Haksaeng", "&cat=0000300003");
        map.put("Sanhak", "&cat=");
        map.put("Ilban", "&cat=0000300006");

        return map;
    }

    public static String getLink(String key, String id)
    {
        return firstLink.get(key) + id + secodLink.get(key);
    }

    public static int getIndex(String key)
    {
        return indexMap.get(key);
    }
}
