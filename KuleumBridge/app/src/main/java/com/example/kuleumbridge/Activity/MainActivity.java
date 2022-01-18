package com.example.kuleumbridge.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.kuleumbridge.API.ApiGradeAllClass;
import com.example.kuleumbridge.API.ApiGradeNowClass;
import com.example.kuleumbridge.API.ApiLoginClass;
import com.example.kuleumbridge.Animation.CustomProgress;
import com.example.kuleumbridge.Common.CallBack;
import com.example.kuleumbridge.Common.EncryptClass;
import com.example.kuleumbridge.Data.UserInfoClass;
import com.example.kuleumbridge.R;
import com.example.kuleumbridge.Taste.TastePlaceActivity;
import com.example.kuleumbridge.Taste.TastePlaceList;
import com.example.kuleumbridge.Calendar.mainAlarm;
import com.google.android.material.tabs.TabLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
     // User의 정보들을 저장할 객체
    UserInfoClass uic;
    TextView calenderTV;
    CustomProgress customProgress;
    String gradeAT = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        uic = new UserInfoClass();

        customProgress = new CustomProgress(MainActivity.this);

        // 로딩 화면 시작
        customProgress.show();

        // 자동로그인
        autoLogin();
    }

    // 로그인 버튼이 눌렸을 때
    public void onLoginBtnClick(View view){
        EditText et_id = findViewById(R.id.idInput);
        EditText et_pwd = findViewById(R.id.passwordInput);
        String input_id = String.valueOf(et_id.getText());
        String input_pwd = String.valueOf(et_pwd.getText());

        // 로딩 화면 시작
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
                        // uic에 얻어온 정보 저장 - 전체성적
                        uic.setGradeAllInfo(result);

                        TextView gradeAll = findViewById(R.id.gradeAllText);
                        gradeAT = uic.getGrade_all_txt();

                        //gradeAll.setText(gradeAT);
                        //※새로운 창에서 생성되는 TextView 객체에 setText를 진행할경우 앱이 비정상종료되는 문제 발생※
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

                // GradeNow 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask를 상속한 클래스를 활요해 값을 얻어온다.
                ApiGradeNowClass agnc = new ApiGradeNowClass(uic.getUSER_ID(), new CallBack() {
                    @Override
                    public void callback_login(String result) {

                    }

                    @Override
                    public void callback_grade(String result) {
                        // uic에 얻어온 정보 저장 - 금학기성적
                        uic.setGradeNowInfo(result);

                        TextView gradeNow = findViewById(R.id.gradeNowText);

                        String txt = uic.getGrade_now_txt();

                        gradeNow.setText(txt);
                    }

                    @Override
                    public void callback_fail() {
                        // 연결 실패시 작동
                        // 애니메이션 동작 중단
                        // 적정한 화면으로 전환

                        customProgress.dismiss();
                    }
                });
                agnc.execute();

                try {
                    //
                    //  자동로그인 정보는 로그아웃 후 지우는 기능이 추가되어야 함
                    //

                    saveLoginInfo(input_id, input_pwd);

                    // 뷰 전환 부분
                    setContentView(R.layout.after_log);

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

                    // 로딩 애니메이션 종료
                    customProgress.dismiss();

                    // 작은 캘린더에 출력하는 부분
                    calenderTV = findViewById(R.id.calendarview);
                    mainAlarm mA = new mainAlarm();
                    String today = mA.getTime();

                    SharedPreferences pref = getSharedPreferences(today ,MODE_PRIVATE); // 날짜를 기준으로 여는 것

                    String fileData = pref.getString("input", "오늘 할 일이 존재하지 않습니다."); // fileData 변수에 저장된 것을 저장

                    calenderTV.setText(today + "\n" + fileData); // 로그인 후 작은 캘린더 화면에 출력

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
                customProgress.dismiss();
            }
        });
        alc.execute();
    }

    public void saveLoginInfo(String input_id, String input_pwd)
    {
        // 자동 로그인을 위한 로그인 정보 암호화 부분
        EncryptClass ec = new EncryptClass(getKey());

        try {
            String ec_id = ec.encrypt(input_id);
            String ec_pwd = ec.encrypt(input_pwd);

            // 암호화된 로그인 정보 저장 부분
            // 이미 암호화된 정보가 있다면 저장 X
            SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);

            if(pref.getString("id", "").equals(""))
            {
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("id", ec_id);
                editor.putString("pwd", ec_pwd);

                editor.apply();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // 작은 캘린더 화면 글 불러오기 함수
    public void setCalenderText()
    {
        calenderTV = findViewById(R.id.calendarview);
        mainAlarm mA = new mainAlarm();
        String today = mA.getTime();

        SharedPreferences pref = getSharedPreferences(today ,MODE_PRIVATE);                 // 날짜를 기준으로 여는 것

        String fileData = pref.getString("input", "오늘 할 일이 존재하지 않습니다.");      // fileData 변수에 저장된 것을 저장

        calenderTV.setText(getString(R.string.calender, today, fileData));                  // 로그인 후 작은 캘린더 화면에 출력
    }

    // 학생증 정보 수정
    public void editStudentID()
    {
        ImageView img = findViewById(R.id.photo);
        TextView name = findViewById(R.id.user_nm);
        TextView user_id = findViewById(R.id.user_id);
        TextView major = findViewById(R.id.dpet_ttnm);

        try {
            // 학생 사진 세팅
            byte[] encodeByte = Base64.decode(uic.getPHOTO(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            img.setImageBitmap(bitmap);

            // 학생 이름 세팅
            name.setText(uic.getUSER_NM());

            // 학생 학번 세팅
            user_id.setText(getString(R.string.userid, uic.getUSER_ID()));

            // 학생 학과 세팅
            major.setText(getString(R.string.dept, uic.getDEPT_TTNM()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                // 로딩 화면 중단
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

    // 캘린더 버튼 상호작용 함수
    public void onCalenderBtnClick(View view) {
        //CalenderActivity 실행, 기존 창은 유지.
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);

    }

    // 맛 버튼 상호작용 함수
    public void onTastePlaceBtnClick(View view) {

        //onTastePlaceActivity 실행, 기존 창은 유지.
        Intent intent = new Intent(this, TastePlaceActivity.class);
        startActivity(intent);

    }

    // 맛집 장르별 버튼 상호작용 함수
    public void OnTasteBtnClick(View view) {
        String parameter = "";
        switch(view.getId())
        {
            case R.id.HanSik:
                parameter = "한식";
                break;
            case R.id.BunSik:
                parameter = "분식";
                break;
            case R.id.Caffe:
                parameter = "디저트";
                break;
            case R.id.IlSik:
                parameter = "일식";
                break;
            case R.id.Asian:
                parameter = "아시안";
                break;
            case R.id.FastFood:
                parameter = "패스트푸트";
                break;
            case R.id.JungSik:
                parameter = "중식";
                break;
            case R.id.Meat:
                parameter = "고기";
                break;
            case R.id.Alchol:
                parameter = "술집";
                break;
        }
        Intent intent = new Intent(this, TastePlaceList.class);
        intent.putExtra("parameter", parameter);
        startActivity(intent);
    }


    // 성적 보기 버튼 상호작용 함수
    public void onGradeCheckAcBtnClick(View view) {
        //GradeCheckActivity 실행, 기존 창은 유지.
        Intent intent2 = new Intent(this, GradeCheckActivity.class);
        intent2.putExtra("gradeAll", gradeAT);
        startActivity(intent2);
    }

    // 관련 링크 버튼 상호작용 함수
    public void onSiteBtnClick(View view) {
        String uri = view.getResources().getResourceEntryName(view.getId());    // id의 String을 그대로 가져오는 구문
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));         // intent 만들고
        startActivity(intent);                                                  // 실행
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