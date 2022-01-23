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
import androidx.appcompat.app.AppCompatActivity;

public class NoticeActivity extends AppCompatActivity {

    public void onNoticeText1Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("")); // nic.get공지사항구분().get(0).getURL()
        startActivity(intent);
    }

    public void onNoticeText2Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("")); // nic.get공지사항구분().get(1).getURL()
        startActivity(intent);
    }

    public void onNoticeText3Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("")); // nic.get공지사항구분().get(2).getURL()
        startActivity(intent);
    }

    public void onNoticeText4Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("")); // nic.get공지사항구분().get(3).getURL()
        startActivity(intent);
    }

    public void onNoticeText5Clicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("")); // nic.get공지사항구분().get(4).getURL()
        startActivity(intent);
    }
}
