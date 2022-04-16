package com.KonDuckJoa.kuleumbridge.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.KonDuckJoa.kuleumbridge.R;
import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 툴바 메뉴 버튼 생성
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_menu_button); // 메뉴 버튼 모양 설정

        mDrawerLayout = findViewById(R.id.DrawLayout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem ->
        {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            String title = menuItem.getTitle().toString();

            switch(id)
            {
                case R.id.Drawer_setting:
                    Toast.makeText(context, title+ ":환경설정 창으로 이동합니다", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Drawer_logout:
                    Toast.makeText(context, title + ": 로그아웃을 시도합니다.", Toast.LENGTH_SHORT).show();
                    break;
            }

            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home) // 왼쪽 상단 버튼 눌렀을 때
        {
            mDrawerLayout.openDrawer(GravityCompat.START);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}