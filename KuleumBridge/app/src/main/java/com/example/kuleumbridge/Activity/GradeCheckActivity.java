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

import com.example.kuleumbridge.R;

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
