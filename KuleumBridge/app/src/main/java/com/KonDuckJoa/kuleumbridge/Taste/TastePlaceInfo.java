package com.KonDuckJoa.kuleumbridge.Taste;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.KonDuckJoa.kuleumbridge.R;

/* 리스트 클릭시 나타나는 맛집 세부 정보
   상호명 - 주소 - 지도 - 주소 복사하기 버튼 - 지도 앱에서 확인하기 버튼 으로 구성
 */

public class TastePlaceInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taste_info);

        Intent intent = getIntent();

        //TastePlaceList 의 값 받아오는 과정
        String name = intent.getStringExtra("name");
        String address = intent.getStringExtra("address");
        double latitude = intent.getDoubleExtra("latitude",0);
        double longitude = intent.getDoubleExtra("longitude",0);

        if(savedInstanceState == null)
        {
            TastePlaceInfoMap mainFragment = new TastePlaceInfoMap();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragment, mainFragment,"main").commit();

            //TastePlaceInfo(Activity) -> TastePlaceInfoMap(fragment)로 값 전달하는 과정
            Bundle bundle = new Bundle();
            bundle.putString("name",name);
            bundle.putString("address",address);
            bundle.putDouble("latitude",latitude);
            bundle.putDouble("longitude",longitude);

            mainFragment.setArguments(bundle);
        }
    }

    //주소 복사하기 버튼
    public void CopyClick(View view)
    {
        TextView textView = findViewById(R.id.info_address); //텍스트뷰
        String addressCopy = textView.getText().toString(); // 텍스트뷰 글자 가져옴

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy", addressCopy);
        clipboard.setPrimaryClip(clip); //클립보드
    }

    // todo 이거 앱으로 연결을 해주자
    public void MapClick(View view)
    {
       Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.google.co.kr/maps/@37.5425241,127.073699,15z"));

       startActivity(intent);
    }
}


