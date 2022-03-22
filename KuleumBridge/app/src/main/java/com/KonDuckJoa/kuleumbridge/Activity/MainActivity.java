package com.KonDuckJoa.kuleumbridge.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.KonDuckJoa.kuleumbridge.API.ApiGradeAllClass;
import com.KonDuckJoa.kuleumbridge.API.ApiGradeNowClass;
import com.KonDuckJoa.kuleumbridge.API.ApiLoginClass;
import com.KonDuckJoa.kuleumbridge.API.ApiNoticeClass;
import com.KonDuckJoa.kuleumbridge.Animation.CustomProgress;
import com.KonDuckJoa.kuleumbridge.Common.CallBack;
import com.KonDuckJoa.kuleumbridge.Common.EncryptClass;
import com.KonDuckJoa.kuleumbridge.Grade.Grade;
import com.KonDuckJoa.kuleumbridge.Data.UserInfoClass;
import com.KonDuckJoa.kuleumbridge.Notice.Notice;
import com.KonDuckJoa.kuleumbridge.Notice.NoticeHandler;
import com.KonDuckJoa.kuleumbridge.Notice.NoticeInfoClass;
import com.KonDuckJoa.kuleumbridge.R;
import com.KonDuckJoa.kuleumbridge.Taste.TasteHandler;
import com.KonDuckJoa.kuleumbridge.Taste.TastePlaceActivity;
import com.KonDuckJoa.kuleumbridge.Taste.TastePlaceList;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity{
    // User 의 정보들을 저장할 객체
    UserInfoClass uic;
    NoticeInfoClass nic;


    // 로딩 애니메이션을 위한 객체
    CustomProgress customProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        uic = new UserInfoClass();
        nic = new NoticeInfoClass();
        customProgress = new CustomProgress(MainActivity.this);

        // 로딩 화면 시작
        customProgress.show();

        // 맛집 정보 불러오기
        setPlaceData();

        // 자동로그인
        autoLogin();
    }

    public void setPlaceData()
    {
        try {
            TastePlaceActivity.getDataFromExcel(getBaseContext().getResources().getAssets().open("place.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 로그인 버튼이 눌렸을 때
    public void onLoginBtnClick(View view){
        EditText et_id = findViewById(R.id.idInput);
        EditText et_pwd = findViewById(R.id.passwordInput);
        String input_id = String.valueOf(et_id.getText());
        String input_pwd = String.valueOf(et_pwd.getText());

        // 로딩 애니메이션 시작
        startLoadingAnimation();

        // 로그인 함수
        Login(input_id, input_pwd);
    }

    // 로그인 함수
    public void Login(String input_id, String input_pwd)
    {
        // 인터넷 연결은 스레드를 통해서 백그라운드로 돌아가야 하므로(안드로이드 정책) AsyncTask 를 사용한다.
        // 그 AsyncTask 를 상속한 ApiConnectClass 클래스를 만들어서 객체로 사용하기로 함
        // 생성자의 파라매터로 id, pwd 를 받는다.
        ApiLoginClass alc = new ApiLoginClass(input_id, input_pwd, this, new CallBack() {

            @Override
            public void callback_success(String result) {
                loginSuccess(result);
                saveLoginInfo(input_id, input_pwd);
            }

            @Override
            public void callback_fail() {
                stopLoadingAnimation();
            }
        });
        alc.execute();
    }

    // ApiLoginClass 통해 로그인 성공시
    public void loginSuccess(String result)
    {
        // 유저 정보 저장하는 부분
        uic.setLoginInfo(result);

        // GradeAll 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask 를 상속한 클래스를 활용해 값을 얻어온다.
        ApiGradeAllClass agac = new ApiGradeAllClass(uic.getUSER_ID(), new CallBack() {

            @Override
            public void callback_success(String result) {
                gradeAllSuccess(result);
            }

            @Override
            public void callback_fail() {
                stopLoadingAnimation();
            }
        });
        agac.execute();

        // GradeNow 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask 를 상속한 클래스를 활요해 값을 얻어온다.
        ApiGradeNowClass agnc = new ApiGradeNowClass(uic.getUSER_ID(), new CallBack() {

            @Override
            public void callback_success(String result) {
                gradeNowSuccess(result);
            }

            @Override
            public void callback_fail() {
                stopLoadingAnimation();
            }
        });
        agnc.execute();

        ApiNoticeClass anc;
        // 순서는 학사 - 장학 - 취창업 - 국제 - 학생 - 산학 - 일반
        for(int i = 0; i < 7; i++)
        {
            String category = NoticeHandler.getCategory(i);
            anc = new ApiNoticeClass(uic.getUSER_ID(), category, new CallBack(){

                @Override
                public void callback_success(String result) {
                    NoticeSuccess(result, category);
                }

                @Override
                public void callback_fail() {

                }
            });
            anc.execute();
        }

        try {
            // 뷰 전환
            viewTransform();

            // 로딩 애니메이션 종료
            stopLoadingAnimation();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // ApiGradeAllClass 통해 전체 성적 정보 가져오기 성공시
    public void gradeAllSuccess(String result)
    {
        // uic에 얻어온 정보 저장 - 전체성적
        uic.setGradeAllInfo(result);

        // 학생증 정보 수정
        editStudentID();
    }

    // ApiGradeNowClass 통해 금학기 성적 정보 가져오기 성공시
    public void gradeNowSuccess(String result)
    {

        try {
            // uic에 얻어온 정보 저장 - 금학기성적
            uic.setGradeNowInfo(result);
            TableLayout tableLayout = findViewById(R.id.grade_now_tablelayout);
            ArrayList<Grade> gradeNow = uic.getGrade_now();

            for (int i = 0; i < gradeNow.size(); i++) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                for (int j = 0; j < 4; j++) {
                    TextView textView = new TextView(this);
                    textView.setTextSize(16);
                    textView.setTextColor(Color.parseColor("#000000"));
                    textView.setPadding(10, 0, 20, 50);
                    textView.setWidth(0);

                    //글자 수 많으면 ... 으로 처리
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setSelected(true);

                    switch (j) {
                        case 0:
                            textView.setText(gradeNow.get(i).getPOBT_DIV());
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                            break;
                        case 1:
                            textView.setText(gradeNow.get(i).getHAKSU_NM());
                            textView.setGravity(Gravity.START); // Gravity Start를 대신 쓰라고 해서 Start로 수정함
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                            break;
                        case 2:
                            textView.setText(gradeNow.get(i).getPNT());
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
                            break;
                        case 3:
                            textView.setText(gradeNow.get(i).getGRD());
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f));
                            break;
                    }
                    tableRow.addView(textView);
                }
                tableLayout.addView(tableRow);
            }
        }catch (NullPointerException e) { //uic 객체가 비었을때 예외처리
            TextView textView = new TextView(this);
            textView.setText("해당 학기 성적이 존재하지 않습니다.");
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL); //텍스트뷰 가로 세로 중앙 정렬

        }
    }

    // ApiNoticeClass 통해 공지사항 정보 가져오기 성공시
    public void NoticeSuccess(String result, String notice_category)
    {
        // nic 에 얻어온 정보 저장
        nic.setNoticeInfo(result,notice_category);

        TableLayout[] tables = {
                findViewById(R.id.notice_element_table0),
                findViewById(R.id.notice_element_table1),
                findViewById(R.id.notice_element_table2),
                findViewById(R.id.notice_element_table3),
                findViewById(R.id.notice_element_table4),
                findViewById(R.id.notice_element_table5),
                findViewById(R.id.notice_element_table6)
        };
        // 공지사항 테이블을 가져온 정보들을 바탕으로 채워준다.
        setNoticeTable(nic.getNotice(notice_category), tables[NoticeHandler.getIndex(notice_category)]);
    }

    // 로딩 화면 시작
    public void startLoadingAnimation()
    {
        customProgress.show();
    }

    // 로딩 애니메이션 종료
    public void stopLoadingAnimation()
    {
        customProgress.dismiss();
    }


    // 로그인 정보 저장
    public void saveLoginInfo(String input_id, String input_pwd)
    {
        // 자동 로그인을 위한 로그인 정보 암호화 부분
        EncryptClass ec = new EncryptClass(getEncryptKey());

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

    // 학생증 정보 수정
    public void editStudentID()
    {
        ImageView img_menu = findViewById(R.id.menu_img);
        TextView name_menu = findViewById(R.id.menu_name);
        ImageView img = findViewById(R.id.studentCard_photo);
        TextView name = findViewById(R.id.studentCard_name);
        TextView stdNum = findViewById(R.id.studentCard_stdNum);
        TextView major = findViewById(R.id.studentCard_major);
        TextView birthday = findViewById(R.id.studentCard_birthday);

        try {
            // 로그인 후 메인 메뉴 우측 상단 학생 사진 세팅
            img_menu.setImageBitmap(getImageBitMap());

            // 로그인 후 안녕, ㅁㅁㅁ! 세팅
            name_menu.setText(getString(R.string.hello, uic.getUSER_NM()));

            // 학생증 사진 세팅
            img.setImageBitmap(getImageBitMap());

            // 학생증 이름 세팅
            name.setText(getString(R.string.name, uic.getUSER_NM()));

            // 학생증 학번 세팅
            stdNum.setText(getString(R.string.userid, uic.getUSER_ID()));

            // 학생증 학과 세팅
            major.setText(getString(R.string.dept, uic.getDEPT_TTNM()));

            // 학생증 생년월일 세팅
            birthday.setText(getString(R.string.birth, uic.getRESNO()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 이미지 비트맵 반환
    public Bitmap getImageBitMap()
    {
        byte[] encodeByte = Base64.decode(uic.getPHOTO(), Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
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

            EncryptClass ec = new EncryptClass(getEncryptKey());

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
    public String getEncryptKey(){
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

    // 공지사항 레이아웃의 "웹 공지사항 이동" 버튼 상호작용 함수
    public void onNoticeBtnClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp"));
        startActivity(intent);
    }

    // 맛집 레이아웃의 "지도로 보기" 버튼 상호작용 함수
    public void onTastePlaceBtnClick(View view) {
        //onTastePlaceActivity 실행, 기존 창은 유지.
        Intent intent = new Intent(this, TastePlaceActivity.class);
        startActivity(intent);
    }

    // 맛집 레이아웃의 9가지 맛집 아이콘 상호작용 함수
    public void OnTasteBtnClick(View view) {
        String parameter = TasteHandler.getStringValue(view.getId());
        Intent intent_tastePlace = new Intent(this, TastePlaceList.class);
        intent_tastePlace.putExtra("parameter", parameter);
        startActivity(intent_tastePlace);
    }

    // 성적조회 레이아웃의 "세부 성적 조회" 버튼 상호작용 함수
    public void onGradeCheckAcBtnClick(View view) {
        //GradeCheckActivity 실행, 기존 창은 유지.
        Intent intent_gradeAll = new Intent(this, GradeCheckActivity.class);
        intent_gradeAll.putParcelableArrayListExtra("GAA", uic.getGrade_all()); // uic 객체를 UIC라는 이름으로 포장해서 GradeCheckActivity로 보낸다.
        startActivity(intent_gradeAll);
    }

    // 공지사항 레이아웃의 카테고리별 공지 테이블을 채우는 함수
    public void setNoticeTable(ArrayList<Notice> na, TableLayout table) {
        for(int i = 0; i < na.size(); i++)
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 3; j++)
            {
                TextView textView = new TextView(this);

                textView.setTextSize(16);
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setPadding(10,0,20,50);
                textView.setWidth(0);

                // 글자 수 많으면 ... 으로 처리
                textView.setSelected(true);

                switch(j) {
                    case 0:
                        textView.setText(Integer.toString(i+1));
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.3f));
                        break;
                    case 1:
                        String noticeTitle = na.get(i).getSUBJECT(); // 공지사항 제목
                        String noticeURL = na.get(i).getURL();       // 공지사항 URL
                        textView.setText(noticeTitle);
                        String regexNT = changeRegex(noticeTitle); // 정규표현식으로 바뀐 noticeTitle
                        Pattern pattern = Pattern.compile(regexNT); // 패턴에 컴파일되는 문자열은 정규표현식이 지켜져야 특수문자도 감지한다.
                        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
                            @Override
                            public String transformUrl(Matcher matcher, String s) {
                                return noticeURL;
                                // 스키마인 ""뒤에 noticeURL을 붙여서 리턴한다.
                            }
                        };
                        Linkify.addLinks(textView,pattern,"",null,mTransform);
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,2.0f));
                        break;
                    case 2:
                        textView.setText(na.get(i).getPOSTED_DT());
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.7f));
                        break;
                }
                tableRow.addView(textView);
            }
            table.addView(tableRow);
        }
    }

    // 시간표 레이아웃의 시간표를 채우는 함수
    public void setTimeTable(TableLayout table)
    {
        //  24행 추가로 만들어 총 25행
        for (int i = 0; i < 24; i++)
        {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            String[] time = {"9","","10","","11","","12","","1","","2","","3","","4","",
                    "5","","6","","","","",""};

            // 6열
            for (int j = 0; j < 6; j++)
            {
                TextView textView = new TextView(this);

                textView.setTextSize(16);
                textView.setBackground(getDrawable(i % 2 == 0 ? R.drawable.border_textview_even : R.drawable.border_textview_odd));
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setPadding(10,0,20,10);
                textView.setWidth(0);

                switch(j)
                {
                    case 0: // 시간
                        textView.setText(time[i]);
                        textView.setTypeface(null, Typeface.BOLD);
                        textView.setGravity(Gravity.RIGHT);
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.2f));
                        break;
                    case 1: // 월
                    case 2: // 화
                    case 3: // 수
                    case 4: // 목
                    case 5: // 금
                        textView.setGravity(Gravity.CENTER);
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.4f));
                        break;
                }
                tableRow.addView(textView);
            }
            table.addView(tableRow);
        }
    }
    // 뷰 전환 및 탭바 이벤트 세팅
    public void viewTransform()
    {
        setContentView(R.layout.home_layout);

        TabLayout tabLayout = findViewById(R.id.main_tab_layout);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabLayout.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#000000"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                changeTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        TabLayout tabLayout_notice = findViewById(R.id.notice_category_tab);
        tabLayout_notice.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabLayout_notice.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#000000"));
        tabLayout_notice.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                changeTabNotice(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        setTimeTable(findViewById(R.id.timeTable_table));
    }

    // 탭바 상호작용 함수
    private void changeTab(int selectedIndex) {
        LinearLayout[] layouts = {
                findViewById(R.id.home_layout),
                findViewById(R.id.notice_layout),
                findViewById(R.id.taste_place_layout),
                findViewById(R.id.grade_check_layout),
                findViewById(R.id.timeTable_layout)
        };

        for(int i = 0; i < 5; i++)
            layouts[i].setVisibility(selectedIndex == i ? View.VISIBLE : View.GONE);
    }

    // 공지사항 탭바 상호작용 함수
    private void changeTabNotice(int selectedIndex) {
        TableLayout[] tables = {
                findViewById(R.id.notice_element_table0),
                findViewById(R.id.notice_element_table1),
                findViewById(R.id.notice_element_table2),
                findViewById(R.id.notice_element_table3),
                findViewById(R.id.notice_element_table4),
                findViewById(R.id.notice_element_table5),
                findViewById(R.id.notice_element_table6)
        };

        for(int i = 0; i < 7; i++)
            tables[i].setVisibility(selectedIndex == i ? View.VISIBLE : View.GONE);
    }

    // 문자열을 정규표현식을 만족하는 문자열로 바꿔주는 함수
    private String changeRegex(String str) {
        String changedStr = str.replace("(","\\(");
        changedStr = changedStr.replace(")","\\)");
        changedStr = changedStr.replace("[","\\[");
        changedStr = changedStr.replace("]","\\]");
        changedStr = changedStr.replace("★","\\★");
        changedStr = changedStr.replace("☆","\\☆");
        changedStr = changedStr.replace("?","\\?");
        changedStr = changedStr.replace("^","\\^");
        changedStr = changedStr.replace("&","\\&");
        changedStr = changedStr.replace("+","\\+");
        return changedStr;
    }



}