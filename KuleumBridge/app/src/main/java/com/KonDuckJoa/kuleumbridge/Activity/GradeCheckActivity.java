package com.KonDuckJoa.kuleumbridge.Activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.Grade.Grade;
import com.KonDuckJoa.kuleumbridge.Grade.GradeAllList;
import com.KonDuckJoa.kuleumbridge.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GradeCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_detail);

        ArrayList<Grade> gradeAllArray = UserInfo.getInstance().getGradeAll();
        ArrayList<String> tabNameArray = new ArrayList<>();
        HashMap<String, Boolean> isDuplicatedTabNameMap = new HashMap<>();

        if(!GradeAllList.getIsSet())
        {
            GradeAllList.setGradeAllInfo();
        }

        for (int i = 0; i < gradeAllArray.size(); i++)
        {
            if(gradeAllArray.get(i).getSemester().contains("소계")) continue;

            tabNameArray.add(gradeAllArray.get(i).getCompletedYear() + " " + gradeAllArray.get(i).getSemester());
        }

        Collections.reverse(tabNameArray); // 순서 역순으로 바꿔줌

        TabLayout semesterTabLayout = findViewById(R.id.tabs);

        semesterTabLayout.setBackgroundColor(Color.parseColor("#9FF781"));
        semesterTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        semesterTabLayout.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#000000"));

        // 탭 추가 과정
        for (int i = 0; i < tabNameArray.size(); i++)
        {
            if(isDuplicatedTabNameMap.getOrDefault(tabNameArray.get(i), false)) continue;

            isDuplicatedTabNameMap.put(tabNameArray.get(i), true);

            semesterTabLayout.addTab(semesterTabLayout.newTab().setText(tabNameArray.get(i)));
        }

        if(savedInstanceState == null)
        {
            GradeAllList fragment = new GradeAllList();
            getSupportFragmentManager().beginTransaction().replace(R.id.grade_fragment, fragment).commit();

            Bundle bundle = new Bundle();
            bundle.putString("tabName", tabNameArray.get(0)); // 처음에는 0번 탭 선택

            fragment.setArguments(bundle);
        }

        // 탭 클릭했을 때
        semesterTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(savedInstanceState == null)
                {
                    GradeAllList fragment = new GradeAllList();
                    getSupportFragmentManager().beginTransaction().replace(R.id.grade_fragment, fragment).commit();

                    Bundle bundle = new Bundle();
                    bundle.putString("tabName", tab.getText().toString());

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
