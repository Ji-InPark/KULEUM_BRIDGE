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

    Response response;

    String idCorrect = "obandcass";
    String psCorrect = "36sh8133";
    String input_id;
    String input_pwd;

    // User의 정보들을 저장할 객체
    UserInfoClass uic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        uic = new UserInfoClass();
    }

    public void onLoginBtnClick(View view) {
        EditText et_id = findViewById(R.id.idInput);
        EditText et_pwd = findViewById(R.id.passwordInput);
        input_id = String.valueOf(et_id.getText());
        input_pwd = String.valueOf(et_pwd.getText());

        // 인터넷 연결은 스레드를 통해서 백그라운드로 돌아가야 하므로(안드로이드 정책) 스레드를 하나 만듦
        Thread th = new Thread(new Runnable() {

            // 스레드가 실행할 부분
            @Override
            public void run() {
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                OkHttpClient client = new OkHttpClient();

                JSONObject json = new JSONObject();
                try {
                    json.put("id", input_id);
                    json.put("pwd", input_pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // rest api 로그인 post로 보냄
                RequestBody body = RequestBody.create(JSON, json.toString());
                Request request = new Request.Builder()
                        .url("http://3.37.235.212:5000/login")
                        .addHeader("Connection", "close")
                        .post(body)
                        .build();

                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {

            String res_string = response.body().string();

            //System.out.println(response.body().string());

            // 로그인 실패했는지 판단
            if(res_string.contains("로그인 실패하였습니다."))
            {
                JSONObject err_json = new JSONObject(res_string);

                err_json = err_json.getJSONObject("ERRMSGINFO");

                String msg = err_json.getString("ERRMSG");

                // 로그인 실패 메세지 토스트로 띄움
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
            else
            {
                System.out.println("success test");
                /*
                여기는 로그인 성공부분

                유저 정보 싹다 긁어모아서 만들어둔 UserInfoClass 클래스에 저장할것
                현재 클래스 메소드만 구현되어있음
                클래스 필드는 하나도 구현안했으니 필요한 필드 구현해서 사용할 것    -   민규

                그다음 자동 로그인을 위한 암호화된 아이디 비밀번호 저장을 구현     -   지인

                최종적으로 뷰 전환      -   민규
                */

                // 유저 정보 저장하는 부분

                // 자동 로그인 구현 부분

            }
        } catch (Exception e) {
            e.printStackTrace();
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