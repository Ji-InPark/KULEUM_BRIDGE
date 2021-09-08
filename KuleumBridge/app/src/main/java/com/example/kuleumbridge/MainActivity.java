package com.example.kuleumbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
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
            System.out.println("auto login success");
            // 뷰 전환 코드
        }
    }

    public void onLoginBtnClick(View view){
        EditText et_id = findViewById(R.id.idInput);
        EditText et_pwd = findViewById(R.id.passwordInput);
        String input_id = String.valueOf(et_id.getText());
        String input_pwd = String.valueOf(et_pwd.getText());

        // 인터넷 연결은 스레드를 통해서 백그라운드로 돌아가야 하므로(안드로이드 정책) 스레드를 하나 만듦
        // 그 스레드를 상속한 ApiConnetClass 클래스를 만들어서 객체로 사용하기로 함
        // 생성자의 파라매터로 id, pwd 를 받는다.
        ApiLoginClass alc = new ApiLoginClass(input_id, input_pwd);
        alc.start();
        try {
            alc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 로그인 성공 실패 여부 반환
        try
        {
            String res_string = alc.getResult();

            System.out.println(res_string);

            // 로그인 실패했는지 판단
            if(res_string.contains("로그인 실패하였습니다.") || res_string.contains("비밀번호를 변경해주세요."))
            {
                // json으로 받은 에러메세지에서 원하는 부분만 파싱하는 과정
                JSONObject err_json = new JSONObject(res_string);

                err_json = err_json.getJSONObject("ERRMSGINFO");

                String msg = err_json.getString("ERRMSG");

                // 토스트로 에러메세지 출력
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
            else
            {
                /*
                여기는 로그인 성공부분

                autoLogin 부분과 겹치므로 함수화 할 필요 있음

                유저 정보 싹다 긁어모아서 만들어둔 UserInfoClass 클래스에 저장할것
                현재 클래스 메소드만 구현되어있음
                클래스 필드는 하나도 구현안했으니 필요한 필드 구현해서 사용할 것    -   민규

                그다음 자동 로그인을 위한 암호화된 아이디 비밀번호 저장을 구현     -   지인

                최종적으로 뷰 전환      -   민규
                */

                // 유저 정보 저장하는 부분
                // 유저 정보 저장하는 부분도 자동 로그인에 사용되니 함수화할 필요 있음.

                // 자동 로그인을 위한 로그인 정보 암호화 부분
                EncryptClass ec = new EncryptClass(getKey());

                String ec_id = ec.encrypt(input_id);
                String ec_pwd = ec.encrypt(input_pwd);

                // 암호화된 로그인 정보 저장 부분
                SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);

                SharedPreferences.Editor editor = pref.edit();

                editor.putString("id", ec_id);
                editor.putString("pwd", ec_pwd);

                editor.commit();
                // 뷰 전환 부분
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
            }
        }
        catch (Exception e)
        {

        }

    }

    // 자동 로그인 함수
    public boolean autoLogin(){
        try {
            SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);

            String ec_id = pref.getString("id", "");
            String ec_pwd = pref.getString("pwd", "");

            // login 파일에 저장된 정보가 없다면 false 리턴
            if(ec_id.equals(""))
                return false;

            EncryptClass ec = new EncryptClass(getKey());

            // 암호화된 id, pwd를 복호화
            String dc_id = ec.decrypt(ec_id);
            String dc_pwd = ec.decrypt(ec_pwd);

            // 복호화된 login 정보를 가지고 login
            ApiLoginClass alc = new ApiLoginClass(dc_id, dc_pwd);
            alc.start();
            try {
                alc.join();
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
            SharedPreferences pref = getSharedPreferences("key",MODE_PRIVATE);

            String key = pref.getString("key", "");

            System.out.println(key);

            // 만약 저장되어 있는 키가 없다면 키를 랜덤으로 만든다.
            // 그리고 저장한다.
            if(key.equals(""))
            {
                Random rand = new Random();
                key = "";
                for(int i = 0; i < 16; i++)
                {
                    key += rand.nextInt(10);
                }

                SharedPreferences.Editor editor = pref.edit();

                editor.putString("key", key);

                editor.commit();
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

    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        if (pos == 0) { // 첫 번째 탭 선택.

        }
    }


    private void changeView(int index) {
        LinearLayout[] layouts = {
                (LinearLayout) findViewById(R.id.frag1),
                (TableLayout) findViewById(R.id.frag2),
                (LinearLayout) findViewById(R.id.frag3),
                (LinearLayout) findViewById(R.id.frag4),
                (TableLayout) findViewById(R.id.frag5)
        };

        for(int i = 0; i < 5; i++)
        {
            if(index == i)
                layouts[i].setVisibility(View.VISIBLE);
            else
                layouts[i].setVisibility(View.GONE);
        }
    }
}