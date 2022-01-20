package com.example.kuleumbridge.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuleumbridge.Data.Grade;
import com.example.kuleumbridge.R;
import com.example.kuleumbridge.Taste.TastePlaceListData;

import java.util.ArrayList;

public class GradeCheckActivity extends AppCompatActivity {
    String gradeAT;
    LinearLayout base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_check);

        gradeAT = getIntent().getStringExtra("gradeAll");
        TextView gradeAll = findViewById(R.id.gradeAllText);
        gradeAll.setText(gradeAT);

    }

    

}
