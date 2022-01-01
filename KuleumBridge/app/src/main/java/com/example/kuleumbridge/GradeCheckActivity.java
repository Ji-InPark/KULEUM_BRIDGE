package com.example.kuleumbridge;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GradeCheckActivity extends AppCompatActivity {
    UserInfoClass uic;
    Button levelBtn;
    Button semesterBtn;
    LinearLayout base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade_check);
        levelBtn = (Button)findViewById(R.id.levelBtn);
        registerForContextMenu(levelBtn);
        uic = (UserInfoClass) getIntent().getSerializableExtra("uic");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater1 = getMenuInflater();
        inflater1.inflate(R.menu.level_semester_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.lev1sem1:
                levelBtn.setText("1학년 1학기");
                return true;
            case R.id.lev1sem2:
                levelBtn.setText("1학년 2학기");
                return true;
            case R.id.lev2sem1:
                levelBtn.setText("2학년 1학기");
                return true;
            case R.id.lev2sem2:
                levelBtn.setText("2학년 2학기");
                return true;
            case R.id.lev3sem1:
                levelBtn.setText("3학년 1학기");
                return true;
            case R.id.lev3sem2:
                levelBtn.setText("3학년 2학기");
                return true;
            case R.id.lev4sem1:
                levelBtn.setText("4학년 1학기");
                return true;
            case R.id.lev4sem2:
                levelBtn.setText("4학년 2학기");
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
