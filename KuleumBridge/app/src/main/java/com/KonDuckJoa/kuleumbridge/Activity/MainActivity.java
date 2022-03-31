package com.KonDuckJoa.kuleumbridge.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.KonDuckJoa.kuleumbridge.API.ApiGradeAll;
import com.KonDuckJoa.kuleumbridge.API.ApiGradeNow;
import com.KonDuckJoa.kuleumbridge.API.ApiLogin;
import com.KonDuckJoa.kuleumbridge.API.ApiNotice;
import com.KonDuckJoa.kuleumbridge.Animation.CustomProgress;
import com.KonDuckJoa.kuleumbridge.Common.CallBack;
import com.KonDuckJoa.kuleumbridge.Common.Encrypt;
import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.R;
import com.KonDuckJoa.kuleumbridge.Taste.TasteHandler;
import com.KonDuckJoa.kuleumbridge.Taste.TastePlaceList;
import com.KonDuckJoa.kuleumbridge.databinding.TabViewpagerBinding;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    // 로딩 애니메이션을 위한 객체
    CustomProgress customProgress;

    private TabViewpagerBinding tabViewpagerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        customProgress = new CustomProgress(MainActivity.this);

        // 로딩 화면 시작
        customProgress.show();

        // 맛집 정보 불러오기
        setPlaceData();

        // 자동 로그인
        autoLogin();
    }

    public void setPlaceData()
    {
        try
        {
            TastePlaceActivity.getDataFromExcel(getBaseContext().getResources().getAssets().open("place.xls"));
        }
        catch (IOException e)
        {
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
        ApiLogin alc = new ApiLogin(input_id, input_pwd, this, new CallBack() {

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
        UserInfo.getInstance().setLoginInfo(result);

        // GradeAll 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask 를 상속한 클래스를 활용해 값을 얻어온다.
        ApiGradeAll agac = new ApiGradeAll(UserInfo.getInstance().getUserId(), new CallBack() {

            @Override
            public void callback_success(String result) {
                UserInfo.getInstance().setGradeAllInfo(result);
            }

            @Override
            public void callback_fail() {
                stopLoadingAnimation();
            }
        });
        agac.execute();

        // GradeNow 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask 를 상속한 클래스를 활요해 값을 얻어온다.
        ApiGradeNow agnc = new ApiGradeNow(UserInfo.getInstance().getUserId(), new CallBack() {

            @Override
            public void callback_success(String result) {
                UserInfo.getInstance().setGradeNowInfo(result);
            }

            @Override
            public void callback_fail() {
                stopLoadingAnimation();
            }
        });
        agnc.execute();

        ApiNotice anc;
        anc = new ApiNotice(UserInfo.getInstance().getUserId(), new CallBack(){

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
        Encrypt ec = new Encrypt(getEncryptKey());

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

            Encrypt ec = new Encrypt(getEncryptKey());

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

    // 뷰 전환 및 탭바 이벤트 세팅
    public void viewTransform()
    {
        tabViewpagerBinding = TabViewpagerBinding.inflate(getLayoutInflater());

        // setContentView(R.layout.tab_viewpager);와 동일한 구문. 바인딩을 했으므로 이런식으로 View설정 가능
        setContentView(tabViewpagerBinding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // tab_viewpager.xml에는 view_pager라는 id를 가지고있는 뷰페이저 객체가 존재하는데,
        // binding 변수는 tab_viewpager.xml가 바인딩된 것이므로 이렇게 xml파일 내부의 객체를 직접 가져올수 있음.
        ViewPager viewPager = tabViewpagerBinding.viewPager;

        // 뷰페이저와 페이저어댑터를 연결
        viewPager.setAdapter(sectionsPagerAdapter);

        // 상단의 뷰페이저와 동일. tab_viewpager.xml에 존재하는 mainTab id를 가진 TabLayout 객체
        // 바인딩했기때문에 findViewById 없이 이런 식으로 직접 가져올 수 있다.
        TabLayout mainTab = tabViewpagerBinding.mainTab;

        // 탭과 뷰페이저를 연결
        mainTab.setupWithViewPager(viewPager);
        setMainTabColor(mainTab,"#000000");

        // 로딩 애니메이션 종료
        stopLoadingAnimation();
    }

    // 메인 탭 텍스트 및 하단 표시부 색깔 변경
    public void setMainTabColor(TabLayout mainTab, String colorString)
    {
        mainTab.setSelectedTabIndicatorColor(Color.parseColor(colorString));
        mainTab.setTabTextColors(Color.parseColor(colorString),Color.parseColor(colorString));
    }


}