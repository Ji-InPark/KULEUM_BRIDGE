package com.example.kuleumbridge;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/* 리스트 클릭시 나타나는 맛집 세부 정보
   상호명 - 주소 - 지도 - 주소 복사하기 버튼 - 지도 앱에서 확인하기 버튼 으로 구성
 */

public class TastePlaceInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taste_info_main);

        Intent intent = getIntent();

        //TastePlaceList의 값 받아오는 과정
        String name = intent.getStringExtra("name");
        String address = intent.getStringExtra("address");
        Double latitude = intent.getDoubleExtra("latitude",0);
        Double longitude = intent.getDoubleExtra("longitude",0);


        if(savedInstanceState == null) {
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

    public void CopyClick(View view) { //주소 복사하기 버튼

        TextView textView= (TextView)findViewById(R.id.info_address); //텍스트뷰
        String address_copy= textView.getText().toString(); // 텍스트뷰 글자 가져옴
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy", address_copy);
        clipboard.setPrimaryClip(clip); //클립보드

        Toast myToast = Toast.makeText(this.getApplicationContext(), "주소가 복사되었습니다.", Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.BOTTOM, 0, 10);
        myToast.show();

    }

    public void MapClick(View view) {
       Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.google.co.kr/maps/@37.5425241,127.073699,15z"));
       startActivity(intent);

    }
}


