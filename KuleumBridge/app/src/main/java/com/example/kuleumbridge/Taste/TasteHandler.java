package com.example.kuleumbridge.Taste;

import com.example.kuleumbridge.R;

import java.util.HashMap;

// 맛집 분류 클래스
public class TasteHandler {
    private static HashMap<Integer, String> map = createMap();

    private static HashMap<Integer, String> createMap()
    {
        HashMap<Integer, String> map = new HashMap<Integer, String>();

        map.put(R.id.HanSik, "한식");
        map.put(R.id.BunSik, "분식");
        map.put(R.id.Caffe, "디저트");
        map.put(R.id.IlSik, "일식");
        map.put(R.id.Asian, "아시안");
        map.put(R.id.FastFood, "패스트푸드");
        map.put(R.id.JungSik, "중식");
        map.put(R.id.Meat, "고기");
        map.put(R.id.Alchol, "술집");

        return map;
    }

    public static String getValue(int key)
    {
        return map.get(key);
    }
}
