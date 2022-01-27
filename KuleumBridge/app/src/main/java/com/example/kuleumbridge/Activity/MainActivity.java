package com.example.kuleumbridge.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.kuleumbridge.API.ApiGradeAllClass;
import com.example.kuleumbridge.API.ApiGradeNowClass;
import com.example.kuleumbridge.API.ApiLoginClass;
import com.example.kuleumbridge.API.ApiNoticeClass;
import com.example.kuleumbridge.Animation.CustomProgress;
import com.example.kuleumbridge.Common.CallBack;
import com.example.kuleumbridge.Common.EncryptClass;
import com.example.kuleumbridge.Grade.Grade;
import com.example.kuleumbridge.Data.UserInfoClass;
import com.example.kuleumbridge.Notice.Notice;
import com.example.kuleumbridge.Notice.NoticeInfoClass;
import com.example.kuleumbridge.R;
import com.example.kuleumbridge.Taste.TasteHandler;
import com.example.kuleumbridge.Taste.TastePlaceActivity;
import com.example.kuleumbridge.Taste.TastePlaceList;
import com.example.kuleumbridge.Calendar.mainAlarm;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
     // User의 정보들을 저장할 객체
    UserInfoClass uic;
    NoticeInfoClass nic;
    CustomProgress customProgress;
    String gradeAT = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        uic = new UserInfoClass();
        nic = new NoticeInfoClass();
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

        // 로딩 애니메이션 시작
        startLoadingAnimation();

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

            @Override
            public void callback_success(String result) {
                loginSuccess(result);
                saveLoginInfo(input_id, input_pwd);
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 로딩 애니메이션 종료
                stopLoadingAnimation();
            }
        });
        alc.execute();
    }

    // 로그인 성공시
    public void loginSuccess(String result)
    {
        // 유저 정보 저장하는 부분
        uic.setLoginInfo(result);

        // GradeAll 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask를 상속한 클래스를 활용해 값을 얻어온다.
        ApiGradeAllClass agac = new ApiGradeAllClass(uic.getUSER_ID(), new CallBack() {

            @Override
            public void callback_success(String result) {
                gradeAllSuccess(result);
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환
                stopLoadingAnimation();
            }
        });
        agac.execute();

        // GradeNow 정보도 인터넷을 통해서 얻어오는 것이므로 AsyncTask를 상속한 클래스를 활요해 값을 얻어온다.
        ApiGradeNowClass agnc = new ApiGradeNowClass(uic.getUSER_ID(), new CallBack() {

            @Override
            public void callback_success(String result) {
                gradeNowSuccess(result);
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환

                stopLoadingAnimation();
            }
        });
        agnc.execute();

        // 순서는 학사 - 장학 - 취창업 - 국제 - 학생 - 산학 - 일반
        ApiNoticeClass anc_haksa = new ApiNoticeClass(uic.getUSER_ID(), "학사", new CallBack() {
            @Override
            public void callback_success(String result) {
                NoticeSuccess(result, "학사");
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환

                stopLoadingAnimation();
            }
        });
        anc_haksa.execute();

        ApiNoticeClass anc_janghak = new ApiNoticeClass(uic.getUSER_ID(), "장학", new CallBack() {
            @Override
            public void callback_success(String result) {
                NoticeSuccess(result, "장학");
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환

                stopLoadingAnimation();
            }
        });
        anc_janghak.execute();

        ApiNoticeClass anc_chwichangup = new ApiNoticeClass(uic.getUSER_ID(), "취창업", new CallBack() {
            @Override
            public void callback_success(String result) {
                NoticeSuccess(result, "취창업");
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환

                stopLoadingAnimation();
            }
        });
        anc_chwichangup.execute();

        ApiNoticeClass anc_gukje = new ApiNoticeClass(uic.getUSER_ID(), "국제", new CallBack() {
            @Override
            public void callback_success(String result) {
                NoticeSuccess(result, "국제");
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환

                stopLoadingAnimation();
            }
        });
        anc_gukje.execute();

        ApiNoticeClass anc_haksaeng = new ApiNoticeClass(uic.getUSER_ID(), "학생", new CallBack() {
            @Override
            public void callback_success(String result) {
                NoticeSuccess(result, "학생");
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환

                stopLoadingAnimation();
            }
        });
        anc_haksaeng.execute();

        ApiNoticeClass anc_sanhak = new ApiNoticeClass(uic.getUSER_ID(), "산학", new CallBack() {
            @Override
            public void callback_success(String result) {
                NoticeSuccess(result, "산학");
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환

                stopLoadingAnimation();
            }
        });
        anc_sanhak.execute();

        ApiNoticeClass anc_ilban = new ApiNoticeClass(uic.getUSER_ID(), "일반", new CallBack() {
            @Override
            public void callback_success(String result) {
                NoticeSuccess(result, "일반");
            }

            @Override
            public void callback_fail() {
                // 연결 실패시 작동
                // 애니메이션 동작 중단
                // 적정한 화면으로 전환

                stopLoadingAnimation();
            }
        });
        anc_ilban.execute();

        try {
            // 뷰 전환
            viewTransform();

            // 로딩 애니메이션 종료
            stopLoadingAnimation();

        }
        catch (Exception e)
        {
        }

    }

    // ApiNoticeClass 통해 공지사항 정보 가져오기 성공시
    public void NoticeSuccess(String result, String notice_category) {
        // nic에 얻어온 정보 저장
        nic.setNoticeInfo(result,notice_category);

        // 정보 접근은 이런식으로
        // nic.getNotice("장학").get(0).getSUBJECT();
    }

    // ApiGradeAllClass 통해 전체 성적 정보 가져오기 성공시
    public void gradeAllSuccess(String result)
    {
        // uic에 얻어온 정보 저장 - 전체성적
        uic.setGradeAllInfo(result);

        gradeAT = uic.getGrade_all_txt();


        //※새로운 창에서 생성되는 TextView 객체에 setText를 진행할경우 앱이 비정상종료되는 문제 발생※
        // 학생증 정보 수정
        editStudentID();
    }

    // ApiGradeNowClass 통해 금학기 성적 정보 가져오기 성공시
    public void gradeNowSuccess(String result)
    {
        // uic에 얻어온 정보 저장 - 금학기성적
        uic.setGradeNowInfo(result);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.grade_now_tablelayout);

        LinearLayout linear = (LinearLayout) findViewById(R.id.frag3);


        ArrayList<Grade> gradeNow= uic.getGrade_now();
        ArrayList<String> a_div = new ArrayList<>(); // 이수구분
        ArrayList<String> a_name= new ArrayList<>(); // 과목명
        ArrayList<String> a_hak= new ArrayList<>(); // 학점
        ArrayList<String> a_grd= new ArrayList<>(); // 등급


        for(int i=0; i<gradeNow.size(); i++) {
            a_div.add(gradeNow.get(i).getPOBT_DIV());
            a_name.add(gradeNow.get(i).getHAKSU_NM());
            a_hak.add(gradeNow.get(i).getPNT());
            a_grd.add(gradeNow.get(i).getGRD());
        }

        for(int j=0; j<a_div.size(); j++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int h = 0; h < 4; h++) {
                TextView textView = new TextView(this);
                textView.setTextSize(16);
                textView.setPadding(10,0,20,50);
                textView.setWidth(0);


                //글자 수 많으면 ... 으로 처리
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setSelected(true);

                switch (h) {
                    case 0:
                        textView.setText(a_div.get(j));
                        textView.setGravity(Gravity.CENTER);
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
                        break;
                    case 1:
                        textView.setText(a_name.get(j));
                        textView.setGravity(Gravity.LEFT);
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,2.0f));
                        break;
                    case 2:
                        textView.setText(a_hak.get(j));
                        textView.setGravity(Gravity.CENTER);
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.5f));
                        break;
                    case 3:
                        textView.setText(a_grd.get(j));
                        textView.setGravity(Gravity.CENTER);
                        textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.7f));
                        break;
                }
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);

        }


    }

    // 뷰 전환 및 tablayout 세팅
    public void viewTransform()
    {
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
        TextView birthday = findViewById(R.id.studentCard_birthday);
        TextView major = findViewById(R.id.studentCard_major);

        try {
            // 로그인 후 메인 메뉴 우측 상단 학생 사진 세팅
            img_menu.setImageBitmap(getImageBitMap());

            // 로그인 후 안녕, ㅁㅁㅁ! 세팅
            name_menu.setText("안녕, " + uic.getUSER_NM());

            // 학생증 사진 세팅
            img.setImageBitmap(getImageBitMap());

            // 학생증 이름 세팅
            name.setText("이름 : " + uic.getUSER_NM());

            // 학생증 학번 세팅
            stdNum.setText("학번 : " + uic.getUSER_ID());

            // 학생증 학과 세팅
            major.setText("학과 : " + uic.getDEPT_TTNM());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 이미지 비트맵 반환
    public Bitmap getImageBitMap()
    {
        byte[] encodeByte = Base64.decode(uic.getPHOTO(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        return bitmap;
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
    public void onNoticeBtnClick(View view) {
        //NoticeActivity 실행
        Intent intent_notice = new Intent(this, NoticeActivity.class);
        //intent_notice.putParcelableArrayListExtra("Notice");
        startActivity(intent_notice);
    }

    // 맛 버튼 상호작용 함수
    public void onTastePlaceBtnClick(View view) {
        //onTastePlaceActivity 실행, 기존 창은 유지.
        Intent intent = new Intent(this, TastePlaceActivity.class);
        startActivity(intent);
    }

    // 맛집 장르별 버튼 상호작용 함수
    public void OnTasteBtnClick(View view) {
        String parameter = TasteHandler.getValue(view.getId());

        Intent intent_tastePlace = new Intent(this, TastePlaceList.class);
        intent_tastePlace.putExtra("parameter", parameter);
        startActivity(intent_tastePlace);
    }


    // 세부성적 보기 버튼 상호작용 함수
    public void onGradeCheckAcBtnClick(View view) {
        //GradeCheckActivity 실행, 기존 창은 유지.
        Intent intent_gradeAll = new Intent(this, GradeCheckActivity.class);
        intent_gradeAll.putParcelableArrayListExtra("GAA", uic.getGrade_all()); // uic 객체를 UIC라는 이름으로 포장해서 GradeCheckActivity로 보낸다.
        startActivity(intent_gradeAll);
    }

    // 관련 링크 버튼 상호작용 함수
    public void onSiteBtnClick(View view) {
        String uri = view.getResources().getResourceEntryName(view.getId());    // id의 String을 그대로 가져오는 구문
        Intent intent_links = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));   // intent 만들고
        startActivity(intent_links);                                            // 실행
    }


    // 탭바 상호작용 함수
    private void changeView(int index) {
        LinearLayout[] layouts = {
                (LinearLayout) findViewById(R.id.frag1),
                (TableLayout) findViewById(R.id.frag2),
                (LinearLayout) findViewById(R.id.frag3),
                (TableLayout) findViewById(R.id.frag4)
        };
        for(int i = 0; i < 4; i++)
        {
            if(index == i)
                layouts[i].setVisibility(View.VISIBLE);
            else
                layouts[i].setVisibility(View.GONE);
        }
    }
}