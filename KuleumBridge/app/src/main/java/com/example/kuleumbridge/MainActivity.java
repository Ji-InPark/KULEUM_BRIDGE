package com.example.kuleumbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

// test

public class MainActivity extends AppCompatActivity {
     // User의 정보들을 저장할 객체
    UserInfoClass uic;
    TextView calenderTV;
    CustomProgress customProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        uic = new UserInfoClass();

        customProgress = new CustomProgress(MainActivity.this);

        customProgress.show();

        // 자동로그인 중에 로딩화면이 돌아야함
        // 자동로그인이 가능하다면 알아서 로딩화면에서 화면전환 될 것이고
        // 자동로그인이 안된다고하면 로딩화면만 없앰
        autoLogin();
    }

    // 로그인 버튼이 눌렸을 때
    public void onLoginBtnClick(View view){
        EditText et_id = findViewById(R.id.idInput);
        EditText et_pwd = findViewById(R.id.passwordInput);
        String input_id = String.valueOf(et_id.getText());
        String input_pwd = String.valueOf(et_pwd.getText());

        customProgress.show();

        // 로그인 함수
        Login(input_id, input_pwd);
    }

    // 로그인 함수
    public void Login(String input_id, String input_pwd)
    {
        // 인터넷 연결은 스레드를 통해서 백그라운드로 돌아가야 하므로(안드로이드 정책) AsyncTask를 사용한다.
        // 그 AsyncTask를 상속한 ApiConnetClass 클래스를 만들어서 객체로 사용하기로 함
        // 생성자의 파라매터로 id, pwd 를 받는다.
        ApiLoginClass alc = new ApiLoginClass(input_id, input_pwd, this, new CallBack() {
            // 로그인 과정이 끝나고 실행할 부분
            @Override
            public void callback_login(String result) {
                // 유저 정보 저장하는 부분
                uic.setLoginInfo(result);

                // GradeAll 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask를 상속한 클래스를 활용해 값을 얻어온다.
                ApiGradeAllClass agac = new ApiGradeAllClass(uic.getUSER_ID(), new CallBack() {
                    // 정보를 얻어오는 과정이 끝나고 실행할 부분
                    @Override
                    public void callback_login(String result) {

                    }

                    @Override
                    public void callback_grade(String result) {
                        // uic에 얻어온 정보 저장
                        uic.setGradeAllInfo(result);

                        // 학생증 정보 수정
                        editStudentID();
                    }

                    @Override
                    public void callback_fail() {
                        // 연결 실패시 작동
                        // 애니메이션 동작 중단
                        // 적정한 화면으로 전환

                        customProgress.dismiss();
                    }
                });
                agac.execute();

                try {
                    //
                    //  자동로그인 정보는 로그아웃 후 지우는 기능이 추가되어야 함
                    //

                    // 자동 로그인을 위한 로그인 정보 암호화 부분
                    EncryptClass ec = new EncryptClass(getKey());

                    String ec_id = ec.encrypt(input_id);
                    String ec_pwd = ec.encrypt(input_pwd);

                    // 암호화된 로그인 정보 저장 부분
                    // 이미 암호화된 정보가 있다면 저장 X
                    SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);

                    System.out.println(pref.getString("id", "").equals(""));

                    if(pref.getString("id", "").equals(""))
                    {
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("id", ec_id);
                        editor.putString("pwd", ec_pwd);

                        editor.apply();
                    }

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

                    customProgress.dismiss();

                    calenderTV = findViewById(R.id.calendarview);
                    mainAlarm mA = new mainAlarm();
                    String today = mA.getTime();
                    System.out.println(today);
                    String userID = mA.sendID(); // sendID()가 null값을 가져옴
                    String todayfile = "" + userID + today + ".txt"; // "null2021-11-09.txt" 이렇게 저장됨
                    String filedata = null;
                    FileInputStream fis = null;//FileStream fis 변수
                    fis = openFileInput(todayfile); // todayfile 값이 위처럼 생겼다보니 당연히 안열림.

                    /* 당연히 이부분은 파일 오픈에 실패했으므로 정상적으로 수행되지 않음.*/
                    byte[] fileData = new byte[fis.available()];
                    fis.read(fileData);
                    for (int i = 0; i<fileData.length; i++) {
                        System.out.print(fileData[i]);
                    }
                    fis.close();
                    filedata = new String(fileData);
                    //calenderTV.setText(today+"/n"+filedata);

                }
                catch (Exception e)
                {
                    /* 당일 캘린더 탭에서 저장해놓은 오늘의 할 일이 없을 때 */
                    mainAlarm temp = new mainAlarm();
                    calenderTV.setText(temp.getTime()+"\n오늘의 할 일이 존재하지 않습니다.");
                }
            }

            @Override
            public void callback_grade(String result) {
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환
                customProgress.dismiss();
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


        try {
            byte[] encodeByte = Base64.decode(uic.getPHOTO(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setText(uic.getUSER_NM());

        // 경고를 지우기 위해 string.xml 파일을 만든후 string 처리
        birth.setText(getString(R.string.birth, uic.getRESNO()));
        major.setText(getString(R.string.dept, uic.getDEPT_TTNM()));

    }

    // 자동 로그인 함수
    public void autoLogin(){
        try {
            SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);

            String ec_id = pref.getString("id", "");
            String ec_pwd = pref.getString("pwd", "");

            // login 파일에 저장된 정보가 없다면 함수 종료
            if(ec_id.equals(""))
            {
                System.out.println("로그인 정보가 없다");
                customProgress.dismiss();
                return;
            }

            EncryptClass ec = new EncryptClass(getKey());

            // 암호화된 id, pwd를 복호화
            String dc_id = ec.decrypt(ec_id);
            String dc_pwd = ec.decrypt(ec_pwd);

            // 복호화된 login 정보를 가지고 login
            Login(dc_id, dc_pwd);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // 암호화를 위한 key를 불러오는 함수
    public String getKey(){
        try {
            SharedPreferences pref = getSharedPreferences("key",MODE_PRIVATE);

            String key = pref.getString("key", "");

            // 만약 저장되어 있는 키가 없다면 키를 랜덤으로 만든다.
            // 그리고 저장한다.
            if(key.equals(""))
            {
                Random rand = new Random();
                StringBuilder sb = new StringBuilder(key);

                for(int i = 0; i < 16; i++)
                {
                    sb.append(rand.nextInt(10));
                }

                key = sb.toString();

                SharedPreferences.Editor editor = pref.edit();

                editor.putString("key", key);

                editor.apply();
            }

            return key;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void onCalenderBtnClick(View view) {
        //CalenderActivity 실행, 기존 창은 유지.
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);

    }

    // 무슨 이유에서인지 모르겠으나 TastePlaceActivity, GradeCheckActivity 클래스를 불러오면 앱이 종료됨
    // 일단 임시로 캘린더 3개 때려박아놓음. xml파일의 문제는 없음.
    public void onTastePlaceBtnClick(View view) {
        //onTastePlaceActivity 실행, 기존 창은 유지.
        Intent intent = new Intent(this, TastePlaceActivity.class);
        startActivity(intent);
    }

    // 성적 보기 버튼 상호작용 함수
    public void onGradeCheckAcBtnClick(View view) {
        //GradeCheckActivity 실행, 기존 창은 유지.
        Intent intent = new Intent(this, GradeCheckActivity.class);
        startActivity(intent);
    }


    // 탭바 상호작용 함수
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