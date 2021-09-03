package com.example.kuleumbridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    boolean login_success = false;

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
        
        // 자동 로그인 가능할 경우 바로 뷰전환
        if(autoLogin())
        {
            // 뷰 전환 코드
        }
    }

    public void onLoginBtnClick(View view){
        EditText et_id = findViewById(R.id.idInput);
        EditText et_pwd = findViewById(R.id.passwordInput);
        input_id = String.valueOf(et_id.getText());
        input_pwd = String.valueOf(et_pwd.getText());

        // 인터넷 연결은 스레드를 통해서 백그라운드로 돌아가야 하므로(안드로이드 정책) 스레드를 하나 만듦
        // 그 스레드를 상속한 ApiConnetClass 클래스를 만들어서 객체로 사용하기로 함
        // 생성자의 파라매터로 id, pwd 를 받는다.
        ApiConnetClass acc = new ApiConnetClass(input_id, input_pwd);
        acc.start();
        try {
            acc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 로그인 성공 실패 여부 반환
        try
        {
            String res_string = acc.getResult();

            System.out.println(res_string);

            // 로그인 실패했는지 판단
            if(res_string.contains("로그인 실패하였습니다."))
            {
                JSONObject err_json = new JSONObject(res_string);

                err_json = err_json.getJSONObject("ERRMSGINFO");

                String msg = err_json.getString("ERRMSG");

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

                // 자동 로그인을 위한 아이디 비번 암호화 및 저장 부분
                EncryptClass ec = new EncryptClass(getKey());

                String ec_id = ec.encrypt(input_id);
                String ec_pwd = ec.encrypt(input_pwd);

                FileOutputStream fos = openFileOutput("login.txt", Context.MODE_PRIVATE);

                PrintWriter writer= new PrintWriter(fos);

                writer.print(ec_id + " : " + ec_pwd);

                login_success = true;

                // 뷰 전환 부분

            }
        }
        catch (Exception e)
        {

        }

    }

    public boolean autoLogin(){
        FileInputStream fis = null;
        try {
            fis = openFileInput("login.txt");
            Scanner scan = new Scanner(fis);

            String[] ec_id_pwd = scan.next().split(" : ");

            EncryptClass ec = new EncryptClass(getKey());

            String dc_id = ec.decrypt(ec_id_pwd[0]);
            String dc_pwd = ec.decrypt(ec_id_pwd[1]);

            ApiConnetClass acc = new ApiConnetClass(input_id, input_pwd);
            acc.start();
            try {
                acc.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public String getKey(){
        try {
            FileInputStream fis = openFileInput("key.txt");

            Scanner scan = new Scanner(fis);

            String key = scan.next();

            // 만약 저장되어 있는 키가 없다면 키를 랜덤으로 만든다.
            // 그리고 저장한다.
            if(key == null)
            {
                Random rand = new Random();
                key = "";
                for(int i = 0; i < 16; i++)
                {
                    key += rand.nextInt(10);
                }

                FileOutputStream fos = openFileOutput("key.txt", Context.MODE_PRIVATE);

                PrintWriter writer= new PrintWriter(fos);

                writer.print(key);
            }

            return key;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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