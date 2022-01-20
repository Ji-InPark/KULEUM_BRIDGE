package com.example.kuleumbridge.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kuleumbridge.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Calendar extends AppCompatActivity{
        public String fname=null;
        public String str=null;
        public CalendarView calendarView;
        public Button cha_Btn,del_Btn,save_Btn;
        public TextView diaryTextView,textView2,textView3;
        public EditText contextEditText;
        public String userID;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.calender);
            calendarView=findViewById(R.id.calendarView);
            diaryTextView=findViewById(R.id.diaryTextView);
            save_Btn=findViewById(R.id.save_Btn);
            cha_Btn=findViewById(R.id.cha_Btn);
            textView2=findViewById(R.id.textView2);
            contextEditText=findViewById(R.id.contextEditText);
            //로그인 및 회원가입 엑티비티에서 이름을 받아옴
            Intent intent=getIntent();
            String name=intent.getStringExtra("userName");
            final String userID=intent.getStringExtra("userID");
            this.userID=userID;
            textView3.setText(name+"님의 달력 일기장");

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    diaryTextView.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth));
                    contextEditText.setText("");
                    checkDay(year,month,dayOfMonth,userID);
                }
            });
            save_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveDiary(fname);
                    str=contextEditText.getText().toString();
                    textView2.setText(str);
                    save_Btn.setVisibility(View.INVISIBLE);
                    cha_Btn.setVisibility(View.VISIBLE);
                    del_Btn.setVisibility(View.VISIBLE);
                    contextEditText.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.VISIBLE);

                }
            });
        }

        public String sendID(){
            return this.userID;
        }

        public void  checkDay(int cYear,int cMonth,int cDay,String userID){
            fname=""+userID+cYear+"-"+(cMonth+1)+""+"-"+cDay+".txt";            //저장할 파일 이름설정
            SharedPreferences pref = getSharedPreferences(fname, MODE_PRIVATE); // 안드로이드 내장 데이터 저장 장소 - 앱 외부에서는 접근 불가
            try{
                str = pref.getString("input", "오늘 할 일이 존재하지 않습니다."); // map과 같은 형식으로 구현됨

                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView2.setText(str);

                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);

                cha_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contextEditText.setVisibility(View.VISIBLE);
                        textView2.setVisibility(View.INVISIBLE);
                        contextEditText.setText(str);

                        save_Btn.setVisibility(View.VISIBLE);
                        cha_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                        textView2.setText(contextEditText.getText());
                    }

                });
                del_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textView2.setVisibility(View.INVISIBLE);
                        contextEditText.setText("");
                        contextEditText.setVisibility(View.VISIBLE);
                        save_Btn.setVisibility(View.VISIBLE);
                        cha_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                        removeDiary(fname);
                    }
                });
                if(textView2.getText()==null){
                    textView2.setVisibility(View.INVISIBLE);
                    diaryTextView.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    contextEditText.setVisibility(View.VISIBLE);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @SuppressLint("WrongConstant")
        public void removeDiary(String readDay){
            SharedPreferences pref = getSharedPreferences(readDay, MODE_PRIVATE);   // 안드로이드 내장 데이터 저장 장소 - 앱 외부에서는 접근 불가

            SharedPreferences.Editor editor = pref.edit();                          // 데이터 저장을 위한 변수

            try{
                editor.putString("input", null);                              // map과 같이 사용

                editor.apply();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @SuppressLint("WrongConstant")
        public void saveDiary(String readDay){
            SharedPreferences pref = getSharedPreferences(readDay, MODE_PRIVATE);   // 안드로이드 내장 데이터 저장 장소 - 앱 외부에서는 접근 불가

            SharedPreferences.Editor editor = pref.edit();                          // 데이터 저장을 위한 변수

            try{
                String content=contextEditText.getText().toString();

                editor.putString("input", content);                              // map과 같이 사용

                editor.apply();                                                     // 모든 변경 사항을 저장했으면 이 구문을 적어줘야 함
            }catch (Exception e){
                e.printStackTrace();
            }
        }

}