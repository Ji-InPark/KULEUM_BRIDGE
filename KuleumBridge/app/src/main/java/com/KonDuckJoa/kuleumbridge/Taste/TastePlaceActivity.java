package com.KonDuckJoa.kuleumbridge.Taste;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.KonDuckJoa.kuleumbridge.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;

import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/* 맛집 메인 화면에서 하단 부분에 위치한
 지도로 보기 버튼 누르면 나타나는 화면 구성*/

// todo 카카오 api로 바꾸고 싶다 ui가 너무 구림
public class TastePlaceActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    static String kind[] = new String[200]; //종류
    static String name[]= new String[200]; //상호명
    static String address[] = new String[200]; //주소
    static double latitude[] = new double[200]; //위도
    static double longitude[] = new double[200]; //경도
    static int RowEnd = 0;  // 행 개수 세는 변수

    //엑셀 불러서 값 저장하는 과정
    public static void getDataFromExcel(InputStream inputStream) {
        Workbook workbook = null;
        Sheet sheet = null;

        try {
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);

            RowEnd = sheet.getRows() - 1;

            for (int row = 1; row <= RowEnd; row++) {

                kind[row] = sheet.getCell(0, row).getContents();

                name[row] = sheet.getCell(1, row).getContents();
                address[row] = sheet.getCell(2, row).getContents();

                NumberCell latitude2 = (NumberCell) sheet.getCell(3, row);
                latitude2.getNumberFormat().setMaximumFractionDigits(5); //소수점 5자리까지
                latitude[row] = Double.parseDouble(latitude2.getContents()); //위도

                NumberCell longitude2 = (NumberCell) sheet.getCell(4, row);
                longitude2.getNumberFormat().setMaximumFractionDigits(5); //소수점 5자리까지
                longitude[row] = Double.parseDouble(longitude2.getContents()); //경도
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taste_place);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //지도 화면 구성
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        //마커 추가 과정
        for(int i =1; i< RowEnd; i++) {
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(latitude[i],longitude[i])).title(name[i]).snippet(address[i]);

            // 음식 종류에 따라 마커 이미지 변경
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(TasteHandler.getDrawableValue(kind[i]))).getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap,80,80,false);
            marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            mMap.addMarker(marker);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5425241,127.073699), 16));
    }
}





