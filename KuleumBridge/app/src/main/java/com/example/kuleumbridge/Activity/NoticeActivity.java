package com.example.kuleumbridge.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kuleumbridge.Notice.Notice;
import com.example.kuleumbridge.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {

    TabLayout noticeTab;
    ArrayList<Notice>[] noticeAll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onNoticeText1Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        startActivity(intent);
    }

    public void onNoticeText2Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        startActivity(intent);
    }

    public void onNoticeText3Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        startActivity(intent);
    }

    public void onNoticeText4Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        startActivity(intent);
    }

    public void onNoticeText5Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        startActivity(intent);
    }
}
