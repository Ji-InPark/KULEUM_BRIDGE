package com.example.kuleumbridge.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuleumbridge.Data.Grade;
import com.example.kuleumbridge.Data.UserInfoClass;
import com.example.kuleumbridge.R;

import java.util.ArrayList;
import java.util.Iterator;

public class GradeCheckActivity extends AppCompatActivity {
    ArrayList<Grade> gradeAllArr;
    String gradeAT;
    LinearLayout base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_check);
        gradeAllArr = new ArrayList<>();
        gradeAllArr = getIntent().getParcelableArrayListExtra("GAA"); // 넘어온 ArrayList 객체
        Iterator it = gradeAllArr.iterator(); // ArrayList 내에서 반복해야 할 일이 필요할때 사용
        TextView gradeAll = findViewById(R.id.gradeAllText);
        gradeAll.setText(gradeAllArr.get(0).getHAKSU_NM()+gradeAllArr.get(0).getGRD() + "\n"
        +"ArrayList 크기 : " + gradeAllArr.size());
        /* 일단 2학년 2학기 수강했던 과목 한개 과목명이랑 등급만 나옴 */

    }

}
