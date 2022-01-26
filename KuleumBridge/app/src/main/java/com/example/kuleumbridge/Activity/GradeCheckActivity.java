package com.example.kuleumbridge.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuleumbridge.Grade.Grade;
import com.example.kuleumbridge.Grade.GradeDetailList;
import com.example.kuleumbridge.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class GradeCheckActivity extends AppCompatActivity {
    ArrayList<Grade> gradeAllArr;
    ArrayList<String> tab;
    ArrayList<String> tab2;


    TabLayout tabs;


    String year = "";
    int position = 0;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_detail);
        gradeAllArr = new ArrayList<>();
        gradeAllArr = getIntent().getParcelableArrayListExtra("GAA"); // 넘어온 ArrayList 객체
        Iterator it = gradeAllArr.iterator(); // ArrayList 내에서 반복해야 할 일이 필요할때 사용


        for (int h = 0; h < gradeAllArr.size(); h++) {
            year += gradeAllArr.get(h).getYY() + " " + gradeAllArr.get(h).getSHTM_NM() + ",";

//            System.out.println(gradeAllArr.get(h).getHAKSU_NM()+","+gradeAllArr.get(h).getPOBT_DIV()+","+gradeAllArr.get(h).getGRD());
        } // String에 년도랑 학기 임시 저장(쉼표로 구분)

        tab = new ArrayList<String>(Arrays.asList(year.split(",")));
        tab2 = new ArrayList<String>();


        Collections.reverse(tab); //순서 역순으로 바꿔줌
        tab.remove(0); //전체 소계 제거

        for (int i = 0; i < tab.size(); i++) {
            if (tab.get(i).contains("소계")) { //소계 다 제거함
                tab.remove(i);
            }
        }
        for (String a : tab) {
            if (!tab2.contains(a)) {
                tab2.add(a);
            }
        } //중복 된 요소 제거


        tabs = findViewById(R.id.tabs);

        //탭 추가 과정
        for (int i = 0; i < tab2.size(); i++) {
            tabs.addTab(tabs.newTab().setText(tab2.get(i)));
        }






        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                position = tab.getPosition();
                String tabb = tab2.get(position);


                if(savedInstanceState == null) {
                    GradeDetailList fragment = new GradeDetailList();
                    getSupportFragmentManager().beginTransaction().replace(R.id.grade_fragment, fragment).commit();

                    Bundle bundle = new Bundle();
                    bundle.putString("tabb",tabb);
                    bundle.putParcelableArrayList("gaa",gradeAllArr);
                    fragment.setArguments(bundle);
                }


            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });


    }

}
