package com.KonDuckJoa.kuleumbridge.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.KonDuckJoa.kuleumbridge.API.ApiGradeAll;
import com.KonDuckJoa.kuleumbridge.API.ApiGradeNow;
import com.KonDuckJoa.kuleumbridge.API.ApiLogin;
import com.KonDuckJoa.kuleumbridge.API.ApiNotice;
import com.KonDuckJoa.kuleumbridge.Animation.AnimationProgress;
import com.KonDuckJoa.kuleumbridge.Common.CallBack;
import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.Common.Encrypt;
import com.KonDuckJoa.kuleumbridge.R;
import com.KonDuckJoa.kuleumbridge.Taste.TasteHandler;
import com.KonDuckJoa.kuleumbridge.Taste.TastePlaceList;
import com.KonDuckJoa.kuleumbridge.databinding.TabViewPagerBinding;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    private long backPressedTime = 0;

    // 로딩 애니메이션을 위한 객체
    public static AnimationProgress customProgress;

    private TabViewPagerBinding tabViewPagerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_login_layout);
        customProgress = new AnimationProgress(MainActivity.this);

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
    public void onLoginBtnClick(View view)
    {
        EditText editTextId = findViewById(R.id.idInput);
        EditText editTextPwd = findViewById(R.id.passwordInput);
        String inputId = String.valueOf(editTextId.getText());
        String inputPwd = String.valueOf(editTextPwd.getText());

        // 로딩 애니메이션 시작
        startLoadingAnimation();

        // 눈속임을 위한 레이아웃 전환
        setContentView(R.layout.auto_login_layout);

        // 로그인 함수
        Login(inputId, inputPwd);
    }

    // 로그인 함수
    public void Login(String input_id, String input_pwd)
    {
        // 인터넷 연결은 스레드를 통해서 백그라운드로 돌아가야 하므로(안드로이드 정책) AsyncTask 를 사용한다.
        // 그 AsyncTask 를 상속한 ApiConnectClass 클래스를 만들어서 객체로 사용하기로 함
        // 생성자의 파라매터로 id, pwd 를 받는다.
        ApiLogin apiLogin = new ApiLogin(input_id, input_pwd, this, new CallBack()
        {
            @Override
            public void callbackSuccess(String result)
            {
                loginSuccess(result);
                saveLoginInfo(input_id, input_pwd);
            }

            @Override
            public void callbackFail()
            {
                stopLoadingAnimation();
                setContentView(R.layout.login);
            }
        });
        apiLogin.execute();
    }

    // ApiLoginClass 통해 로그인 성공시
    public void loginSuccess(String result)
    {
        // 유저 정보 저장하는 부분
        UserInfo.getInstance().setLoginInfo(result);

        // GradeAll 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask 를 상속한 클래스를 활용해 값을 얻어온다.
        ApiGradeAll apiGradeAll = new ApiGradeAll(UserInfo.getInstance().getUserId(), new CallBack()
        {
            @Override
            public void callbackSuccess(String result)
            {
                UserInfo.getInstance().setGradeAllInfo(result);
            }

            @Override
            public void callbackFail()
            {
                stopLoadingAnimation();
                setContentView(R.layout.login);
            }
        });
        apiGradeAll.execute();

        // GradeNow 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask 를 상속한 클래스를 활요해 값을 얻어온다.
        ApiGradeNow apiGradeNow = new ApiGradeNow(UserInfo.getInstance().getUserId(), new CallBack()
        {
            @Override
            public void callbackSuccess(String result)
            {
                UserInfo.getInstance().setGradeNowInfo(result);
            }

            @Override
            public void callbackFail()
            {
                stopLoadingAnimation();
                setContentView(R.layout.login);
            }
        });
        apiGradeNow.execute();

        ApiNotice apiNotice = new ApiNotice(UserInfo.getInstance().getUserId(), new CallBack()
        {
            @Override
            public void callbackSuccess(String result)
            {
                // NoticeInfoClass.getInstance() 에 얻어온 정보 저장
                transformView();
            }

            @Override
            public void callbackFail() { }
        });
        apiNotice.execute();
    }

    // 로딩 화면 시작
    public static void startLoadingAnimation()
    {
        customProgress.show();
    }

    // 로딩 애니메이션 종료
    public static void stopLoadingAnimation()
    {
        customProgress.dismiss();
    }

    // 로그인 정보 저장
    public void saveLoginInfo(String inputId, String inputPwd)
    {
        // 자동 로그인을 위한 로그인 정보 암호화 부분
        Encrypt encrypt = new Encrypt(getEncryptKey());

        try
        {
            String encryptedId = encrypt.encrypt(inputId);
            String encryptedPwd = encrypt.encrypt(inputPwd);

            // 암호화된 로그인 정보 저장 부분
            // 이미 암호화된 정보가 있다면 저장 X
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

            if(sharedPreferences.getString("id", "").equals(""))
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("id", encryptedId);
                editor.putString("pwd", encryptedPwd);

                editor.apply();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // 자동 로그인 메소드
    public void autoLogin()
    {
        try
        {
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

            String encryptedId = sharedPreferences.getString("id", "");
            String encryptedPwd = sharedPreferences.getString("pwd", "");

            // login 파일에 저장된 정보가 없다면 함수 종료
            if(encryptedId.equals(""))
            {
                // 로딩 화면 중단
                stopLoadingAnimation();
                setContentView(R.layout.login);
                return;
            }

            Encrypt encrypt = new Encrypt(getEncryptKey());

            // 암호화된 id, pwd를 복호화
            String decryptId = encrypt.decrypt(encryptedId);
            String decryptPwd = encrypt.decrypt(encryptedPwd);

            // 복호화된 login 정보를 가지고 login
            Login(decryptId, decryptPwd);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // 암호화를 위한 key를 불러오는 메소드
    public String getEncryptKey()
    {
        try
        {
            SharedPreferences sharedPreferences = getSharedPreferences("key", MODE_PRIVATE);

            String key = sharedPreferences.getString("key", "");

            // 만약 저장되어 있는 키가 없다면 키를 랜덤으로 만든다.
            // 그리고 저장한다.
            if(key.equals(""))
            {
                Random random = new Random();
                StringBuilder sb = new StringBuilder(key);

                for(int i = 0; i < 16; i++)
                {
                    sb.append(random.nextInt(10));
                }

                key = sb.toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("key", key);

                editor.apply();
            }

            return key;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.move_web_notice_site_button: // notice_layout의 "웹 공지사항 이동" 버튼
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.konkuk.ac.kr/jsp/Plaza/plaza_01_01.jsp")));
                break;
            case R.id.view_on_map_button: // taste_place_layout의 "지도로 보기" 버튼
                startActivity(new Intent(this, TastePlaceActivity.class));
                break;
            case R.id.grade_all_check_button: // home_layout의 "전체 성적 조회" 버튼
                startActivity(new Intent(this, GradeCheckActivity.class));
                break;
        }
    }

    // 맛집 레이아웃의 9가지 맛집 아이콘 상호작용 메소드
    public void OnTasteButtonClick(View view)
    {
        String buttonName = TasteHandler.getStringValue(view.getId());
        Intent intentTastePlace = new Intent(this, TastePlaceList.class);
        intentTastePlace.putExtra("buttonName", buttonName);
        startActivity(intentTastePlace);
    }

    // 뷰 전환 및 탭바 이벤트 세팅
    public void transformView()
    {
        tabViewPagerBinding = TabViewPagerBinding.inflate(getLayoutInflater());

        // setContentView(R.layout.tab_viewpager);와 동일한 구문. 바인딩을 했으므로 이런식으로 View설정 가능
        setContentView(tabViewPagerBinding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // tab_viewpager.xml에는 view_pager라는 id를 가지고있는 뷰페이저 객체가 존재하는데,
        // binding 변수는 tab_viewpager.xml가 바인딩된 것이므로 이렇게 xml파일 내부의 객체를 직접 가져올수 있음.
        ViewPager viewPager = tabViewPagerBinding.viewPager;

        // 뷰페이저와 페이저어댑터를 연결
        viewPager.setAdapter(sectionsPagerAdapter);

        // 상단의 뷰페이저와 동일. tab_viewpager.xml에 존재하는 mainTab id를 가진 TabLayout 객체
        // 바인딩했기때문에 findViewById 없이 이런 식으로 직접 가져올 수 있다.
        TabLayout mainTab = tabViewPagerBinding.mainTab;

        // 탭과 뷰페이저를 연결
        mainTab.setupWithViewPager(viewPager);
        setMainTabColor(mainTab,"#000000");
    }

    // 메인 탭 텍스트 및 하단 표시부 색깔 변경 메소드
    public void setMainTabColor(TabLayout mainTab, String colorString)
    {
        mainTab.setSelectedTabIndicatorColor(Color.parseColor(colorString));
        mainTab.setTabTextColors(Color.parseColor(colorString),Color.parseColor(colorString));
    }

    @Override
    public void onBackPressed()
    {
        if(System.currentTimeMillis() - backPressedTime > 2000)
        {
            backPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        finish();
    }
}