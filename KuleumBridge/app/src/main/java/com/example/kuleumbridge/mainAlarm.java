package com.example.kuleumbridge;

import android.os.Bundle;
import android.widget.TextView;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainAlarm extends Calendar{
    TextView whattodo;

    // 시간을 불러오는 함수
    public String getTime() {
        long mNow;
        mNow = System.currentTimeMillis();
        Date mDate;
        mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        String Day = mFormat.format(mDate);
        return Day;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String today = getTime();

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.afterlog);

            whattodo=(TextView)findViewById(R.id.calendarView);

            String todayfile = "" + userID + today + ".txt";
            String filedata = null;
            FileInputStream fis = null;//FileStream fis 변수
             fis = openFileInput(todayfile);


            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            filedata = new String(fileData);

            whattodo.setText(today+"/n"+filedata);


        } catch (Exception e) {
            whattodo.setText(today+"/n"+"오늘의 할 일이 존재하지 않습니다.");

        }
    }


}