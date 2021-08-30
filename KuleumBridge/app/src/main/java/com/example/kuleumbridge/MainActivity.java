package com.example.kuleumbridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String idCorrect = "obandcass";
    String psCorrect = "36sh8133";
    String idTyped;
    String psTyped;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

    }

    public void onLoginBtnClick(View view) {

        EditText et1 = findViewById(R.id.idInput);
        idTyped = String.valueOf(et1.getText());
        EditText et2 = findViewById(R.id.passwordInput);
        psTyped = String.valueOf(et2.getText());

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                OkHttpClient client = new OkHttpClient();

                JSONObject json = new JSONObject();
                try {
                    json.put("id", idTyped);
                    json.put("pwd", psTyped);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(JSON, json.toString());
                Request request = new Request.Builder()
                        .url("http://3.37.235.212:5000/login")
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        th.start();

        if (check(idTyped,idCorrect) && check(psTyped,psCorrect)) {
            setContentView(R.layout.afterlog);


            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    // tab의 상태가 선택 상태로 변경
                    int pos = tab.getPosition();
                    changeView(pos);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    // tab의 상태가 선택되지 않음으로 변경
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    // 이미 선택된 상태의 tab이 사용자에 의해 다시 선택됨
                }
            });

        } else {
            Toast.makeText(this, "로그인에 실패했습니다.\n 아이디와 패스워드를 다시 확인해주세요"
                    , Toast.LENGTH_LONG).show();
        }
    }

    public void onCalenderBtnClick(View view) {
        setContentView(R.layout.calender);
    }


    boolean check(String test, String correct) {
        if (test.equals(correct) ) {
            return true;
        } else {
            return false;
        }
    }

    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        if (pos == 0) { // 첫 번째 탭 선택.

        }
    }

    private void changeView(int index) {
        FrameLayout frameLayout1 = (FrameLayout) findViewById(R.id.frame);

        LinearLayout notice = (LinearLayout) findViewById(R.id.frag1);
        TableLayout tastePlace = (TableLayout) findViewById(R.id.frag2);
        LinearLayout gradeCheck = (LinearLayout) findViewById(R.id.frag3);
        LinearLayout studentCard = (LinearLayout) findViewById(R.id.frag4);
        TableLayout relatedLinks = (TableLayout) findViewById(R.id.frag5);

        switch (index) {
            case 0 :
                notice.setVisibility(View.VISIBLE);
                tastePlace.setVisibility(View.GONE);
                gradeCheck.setVisibility(View.GONE);
                studentCard.setVisibility(View.GONE);
                relatedLinks.setVisibility(View.GONE);
                break ;
            case 1 :
                notice.setVisibility(View.GONE);
                tastePlace.setVisibility(View.VISIBLE);
                gradeCheck.setVisibility(View.GONE);
                studentCard.setVisibility(View.GONE);
                relatedLinks.setVisibility(View.GONE);
                break ;
            case 2 :
                notice.setVisibility(View.GONE);
                tastePlace.setVisibility(View.GONE);
                gradeCheck.setVisibility(View.VISIBLE);
                studentCard.setVisibility(View.GONE);
                relatedLinks.setVisibility(View.GONE);
                break ;
            case 3 :
                notice.setVisibility(View.GONE);
                tastePlace.setVisibility(View.GONE);
                gradeCheck.setVisibility(View.GONE);
                studentCard.setVisibility(View.VISIBLE);
                relatedLinks.setVisibility(View.GONE);
                break ;
            case 4 :
                notice.setVisibility(View.GONE);
                tastePlace.setVisibility(View.GONE);
                gradeCheck.setVisibility(View.GONE);
                studentCard.setVisibility(View.GONE);
                relatedLinks.setVisibility(View.VISIBLE);
                break ;
        }
    }

}