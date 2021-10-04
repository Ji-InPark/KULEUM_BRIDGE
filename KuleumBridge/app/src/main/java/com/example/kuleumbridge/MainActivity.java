package com.example.kuleumbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
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
        /*
        if(autoLogin())
        {
            System.out.println("auto login success");
            // 뷰 전환 코드
            // 유저 정보 저장 코드
        }
        */
    }

    public void onLoginBtnClick(View view){
        EditText et_id = findViewById(R.id.idInput);
        EditText et_pwd = findViewById(R.id.passwordInput);
        String input_id = String.valueOf(et_id.getText());
        String input_pwd = String.valueOf(et_pwd.getText());

        // 인터넷 연결은 스레드를 통해서 백그라운드로 돌아가야 하므로(안드로이드 정책) 스레드를 하나 만듦
        // 그 스레드를 상속한 ApiConnetClass 클래스를 만들어서 객체로 사용하기로 함
        // 생성자의 파라매터로 id, pwd 를 받는다.
        ApiLoginClass alc = new ApiLoginClass(input_id, input_pwd, this, new CallBack() {
            @Override
            public void callback_login(String result) {
                // 유저 정보 저장하는 부분
                uic.setLoginInfo(result);
                System.out.println(uic.getUSER_NM());
                System.out.println(uic.getUSER_ID());
                System.out.println(uic.getRESNO());
                System.out.println(uic.getDEPT_TTNM());

                ApiGradeAllClass agac = new ApiGradeAllClass(uic.getUSER_ID(), new CallBack() {
                    @Override
                    public void callback_login(String result) {

                    }

                    @Override
                    public void callback_grade(String result) {
                        uic.setGradeAllInfo(result);
                    }
                });
                agac.execute();

                try {
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

                    System.out.println(ec_id);

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

                    // 학생증 정보 수정
                    editStudentID();
                }
                catch (Exception e)
                {
                    return;
                }
            }

            @Override
            public void callback_grade(String result) {
            }
        });
        alc.execute();

    }

    // 학생증 정보 수정
    public void editStudentID()
    {
        ImageView img = findViewById(R.id.photo);
        TextView name = findViewById(R.id.user_nm);
        TextView birth = findViewById(R.id.resno);
        TextView major = findViewById(R.id.dpet_ttnm);

        //img.setImageResource("anything");
        name.setText(uic.getUSER_NM());
        birth.setText("생년월일: " + uic.getRESNO());
        major.setText("소속: " + uic.getDEPT_TTNM());
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
            //ApiLoginClass alc = new ApiLoginClass(dc_id, dc_pwd, this);
            //alc.execute();

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public String getTime() {
        long mNow;
        String Day = "";
        return Day;
    }

    public String getKey(){
        try {
            SharedPreferences pref = getSharedPreferences("key",MODE_PRIVATE);

            String key = pref.getString("key", "");

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