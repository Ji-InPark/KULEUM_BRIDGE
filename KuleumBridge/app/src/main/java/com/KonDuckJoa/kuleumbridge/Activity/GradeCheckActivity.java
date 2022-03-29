package com.KonDuckJoa.kuleumbridge.Activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.Grade.Grade;
import com.KonDuckJoa.kuleumbridge.Grade.GradeDetailList;
import com.KonDuckJoa.kuleumbridge.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GradeCheckActivity extends AppCompatActivity {
    ArrayList<Grade> gradeAllArr = new ArrayList<>();
    ArrayList<String> tab;
    ArrayList<String> tab2;

    TabLayout tabs;

    String year = "";
    String tabb ="";
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_detail);
        gradeAllArr = UserInfo.getInstance().getGradeAll();

        for (int i = 0; i < gradeAllArr.size(); i++) // String에 년도랑 학기 임시 저장(쉼표로 구분)
            year += gradeAllArr.get(i).getYY() + " " + gradeAllArr.get(i).getSHTM_NM() + ",";

        tab = new ArrayList<>(Arrays.asList(year.split(",")));
        tab2 = new ArrayList<>();

        Collections.reverse(tab); // 순서 역순으로 바꿔줌
        tab.remove(0); // 전체 소계 제거

        for (int i = 0; i < tab.size(); i++)
        {
            if (tab.get(i).contains("소계")) //소계 다 제거함
            {
                tab.remove(i);
            }
        }

        for (String content : tab) //중복 된 요소 제거
        {
            if (!tab2.contains(content))
            {
                tab2.add(content);
            }
        }

        tabs = findViewById(R.id.tabs);
        tabs.setBackgroundColor(Color.parseColor("#9FF781"));
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabs.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#000000"));

        // 탭 추가 과정
        for (int i = 0; i < tab2.size(); i++)
        {
            tabs.addTab(tabs.newTab().setText(tab2.get(i)));
        }

        // 탭 아무것도 안눌렀을 때, 세부성적 버튼 클릭시 화면 초기화
        tabb = tab2.get(0);

        if(savedInstanceState == null)
        {
            GradeDetailList fragment = new GradeDetailList();
            getSupportFragmentManager().beginTransaction().replace(R.id.grade_fragment, fragment).commit();

            Bundle bundle = new Bundle();
            bundle.putString("tabb",tabb);

            fragment.setArguments(bundle);
        }

        // 탭 클릭했을 때
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                position = tab.getPosition();
                tabb = tab2.get(position);

                if(savedInstanceState == null)
                {
                    GradeDetailList fragment = new GradeDetailList();
                    getSupportFragmentManager().beginTransaction().replace(R.id.grade_fragment, fragment).commit();

                    Bundle bundle = new Bundle();
                    bundle.putString("tabb",tabb);

                    fragment.setArguments(bundle);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
}
