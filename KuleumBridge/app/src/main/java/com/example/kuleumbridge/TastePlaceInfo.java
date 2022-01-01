package com.example.kuleumbridge;

import android.content.ClipData;
import android.content.ClipboardManager;
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

public class TastePlaceInfo extends AppCompatActivity { //리스트 클릭시 나타나는 맛집 세부 정보

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taste_info);

        if (savedInstanceState == null) {

            TasteInfoContent mainFragment = new TasteInfoContent();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragment, mainFragment, "main")
                    .commit();
        }


    }

    public void CopyClick(View view) {

        TextView textView= (TextView)findViewById(R.id.address); //텍스트뷰
        String address_copy= textView.getText().toString(); // 텍스트뷰 글자 가져옴
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy", address_copy);
        clipboard.setPrimaryClip(clip); //클립보드

        Toast myToast = Toast.makeText(this.getApplicationContext(), "주소가 복사되었습니다.", Toast.LENGTH_SHORT);
        myToast.setGravity(Gravity.BOTTOM, 0, 10);
        myToast.show();

    }
}


