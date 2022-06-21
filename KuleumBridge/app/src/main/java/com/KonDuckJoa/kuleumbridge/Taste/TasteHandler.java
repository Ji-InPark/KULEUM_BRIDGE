package com.KonDuckJoa.kuleumbridge.Taste;

import com.KonDuckJoa.kuleumbridge.R;

import java.util.HashMap;

// 맛집 분류 클래스
public class TasteHandler {
    private static HashMap<Integer, String> idToStringMap = createIdToStringMap();

    private static HashMap<String, Integer> stringToDrawableMap = createStringToIDMap();

    private static HashMap<Integer, String> createIdToStringMap()
    {
        HashMap<Integer, String> idToStringMap = new HashMap<>();

        idToStringMap.put(R.id.HanSik, "한식");
        idToStringMap.put(R.id.BunSik, "분식");
        idToStringMap.put(R.id.Caffe, "디저트");
        idToStringMap.put(R.id.IlSik, "일식");
        idToStringMap.put(R.id.Asian, "아시안");
        idToStringMap.put(R.id.FastFood, "치킨");
        idToStringMap.put(R.id.JungSik, "중식");
        idToStringMap.put(R.id.Meat, "고기");
        idToStringMap.put(R.id.Alchol, "술집");

        return idToStringMap;
    }

    private static HashMap<String, Integer> createStringToIDMap()
    {
        HashMap<String, Integer> stringToDrawableMap = new HashMap<>();

        stringToDrawableMap.put("한식", R.drawable.hansik);
        stringToDrawableMap.put("분식", R.drawable.bunsik);
        stringToDrawableMap.put("카페/디저트", R.drawable.dessert);
        stringToDrawableMap.put("돈까스/회/일식", R.drawable.sushi);
        stringToDrawableMap.put("치킨/햄버거", R.drawable.hamburger);
        stringToDrawableMap.put("아시안/양식", R.drawable.asian);
        stringToDrawableMap.put("중식", R.drawable.chinese);
        stringToDrawableMap.put("고기", R.drawable.meat);
        stringToDrawableMap.put("술집", R.drawable.alchol);

        return stringToDrawableMap;
    }

    public static String getStringValue(int key)
    {
        return idToStringMap.get(key);
    }

    public static int getDrawableValue(String key)
    {
        return stringToDrawableMap.get(key);
    }
}
