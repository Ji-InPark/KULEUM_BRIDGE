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
    /* 공지사항 창을 새로 띄워줘야 할지 말지 잘 모르겠음.
       일단 디자인 개편안대로보면 뭔가 새로 띄워야될 거 같긴 한데.
       그러면 또 전체성적조회때처럼 putExtra 관련 문제가 100% 발생 */

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
