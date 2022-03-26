package com.KonDuckJoa.kuleumbridge.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.KonDuckJoa.kuleumbridge.databinding.TabViewpagerBinding;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity{
    // 로딩 애니메이션을 위한 객체
    CustomProgress customProgress;

    private TabViewpagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
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
        UserInfoClass.getInstance().setLoginInfo(result);

        // GradeAll 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask 를 상속한 클래스를 활용해 값을 얻어온다.
        ApiGradeAllClass agac = new ApiGradeAllClass(UserInfoClass.getInstance().getUSER_ID(), new CallBack() {

            @Override
            public void callback_success(String result) {
                UserInfoClass.getInstance().setGradeAllInfo(result);
            }

            @Override
            public void callback_fail() {
                stopLoadingAnimation();
            }
        });
        agac.execute();

        // GradeNow 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask 를 상속한 클래스를 활요해 값을 얻어온다.
        ApiGradeNowClass agnc = new ApiGradeNowClass(UserInfoClass.getInstance().getUSER_ID(), new CallBack() {

            @Override
            public void callback_success(String result) {
                UserInfoClass.getInstance().setGradeNowInfo(result);
            }

            @Override
            public void callback_fail() {
                stopLoadingAnimation();
            }
        });
        agnc.execute();

        ApiNoticeClass anc;
        anc = new ApiNoticeClass(UserInfoClass.getInstance().getUSER_ID(), new CallBack(){

            @Override
            public void callback_success(String result) {
                // NoticeInfoClass.getInstance() 에 얻어온 정보 저장
                viewTransform();
            }

            @Override
            public void callback_fail() {

            }
        });
        anc.execute();

        try {
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
        // UserInfoClass.getInstance()에 얻어온 정보 저장 - 전체성적
        UserInfoClass.getInstance().setGradeAllInfo(result);

        // 학생증 정보 수정
        editStudentID();
    }

    // ApiGradeNowClass 통해 금학기 성적 정보 가져오기 성공시
    public void gradeNowSuccess(String result)
    {

        try {
            // UserInfoClass.getInstance()에 얻어온 정보 저장 - 금학기성적
            UserInfoClass.getInstance().setGradeNowInfo(result);
            TableLayout tableLayout = findViewById(R.id.grade_now_tablelayout);
            ArrayList<Grade> gradeNow = UserInfoClass.getInstance().getGradeNow();

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
        }catch (NullPointerException e) { //UserInfoClass.getInstance() 객체가 비었을때 예외처리
            TextView textView = new TextView(this);
            textView.setText("해당 학기 성적이 존재하지 않습니다.");
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL); //텍스트뷰 가로 세로 중앙 정렬

        }
    }

    // ApiNoticeClass 통해 공지사항 정보 가져오기 성공시
    public void NoticeSuccess(String result, String notice_category)
    {
        // NoticeInfoClass.getInstance() 에 얻어온 정보 저장
        NoticeInfoClass.getInstance().setNoticeInfo(result,notice_category);

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
        //setNoticeTable(NoticeInfoClass.getInstance().getNotice(notice_category), tables[NoticeHandler.getIndex(notice_category)]);
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
            name_menu.setText(getString(R.string.hello, UserInfoClass.getInstance().getUSER_NM()));

            // 학생증 사진 세팅
            img.setImageBitmap(getImageBitMap());

            // 학생증 이름 세팅
            name.setText(getString(R.string.name, UserInfoClass.getInstance().getUSER_NM()));

            // 학생증 학번 세팅
            stdNum.setText(getString(R.string.userid, UserInfoClass.getInstance().getUSER_ID()));

            // 학생증 학과 세팅
            major.setText(getString(R.string.dept, UserInfoClass.getInstance().getDEPT_TTNM()));

            // 학생증 생년월일 세팅
            birthday.setText(getString(R.string.birth, UserInfoClass.getInstance().getRESNO()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 이미지 비트맵 반환
    public Bitmap getImageBitMap()
    {
        byte[] encodeByte = Base64.decode(UserInfoClass.getInstance().getPHOTO(), Base64.DEFAULT);

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
    public void onGradeAllCheckBtnClick(View view) {
        //GradeCheckActivity 실행, 기존 창은 유지.
        startActivity(new Intent(this, GradeCheckActivity.class));
    }

    /*// 공지사항 레이아웃의 카테고리별 공지 테이블을 채우는 함수
    public void setNoticeTable(ArrayList<Notice> noticeArrayList, TableLayout table) {
        for(int i = 0; i < noticeArrayList.size(); i++)
        {
            TableRow tbr = new TableRow(this);
            tbr.setLayoutParams(new ViewGroup.LayoutParams(
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
                        String noticeTitle = noticeArrayList.get(i).getSUBJECT(); // 공지사항 제목
                        String noticeURL = noticeArrayList.get(i).getURL();       // 공지사항 URL
                        textView.setText(noticeTitle);

                        String regexNT = changeRegex(noticeTitle); // 정규표현식으로 바뀐 noticeTitle
                        Pattern pattern = Pattern.compile(regexNT); // 패턴에 컴파일되는 문자열은 정규표현식이 지켜져야 특수문자도 감지한다.
                        Linkify.TransformFilter mTransform = (matcher, s) -> noticeURL; // 스키마인 ""뒤에 noticeURL을 붙여서 리턴한다.
                        Linkify.addLinks(textView, pattern,"",null, mTransform);
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,2.0f));
                        break;
                    case 2:
                        textView.setText(noticeArrayList.get(i).getPOSTED_DT());
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.7f));
                        break;
                }
                tbr.addView(textView);
            }
            table.addView(tbr);
        }
    }*/

    // 뷰 전환 및 탭바 이벤트 세팅
    public void viewTransform()
    {
        //editStudentID();
        binding = TabViewpagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.mainTab;
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabs.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#000000"));
        tabs.setupWithViewPager(viewPager);

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