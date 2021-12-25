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

    Button levelBtn;
    Button semesterBtn;
    LinearLayout base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gradecheck);
        levelBtn = (Button)findViewById(R.id.levelBtn);
        registerForContextMenu(levelBtn);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater1 = getMenuInflater();
        inflater1.inflate(R.menu.levelsemestermenu, menu);

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
