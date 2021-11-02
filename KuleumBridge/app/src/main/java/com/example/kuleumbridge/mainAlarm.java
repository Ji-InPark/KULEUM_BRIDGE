package com.example.kuleumbridge;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainAlarm extends Calendar{
    TextView whattodo;

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

            whattodo=(TextView)findViewById(R.id.whattodo);

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