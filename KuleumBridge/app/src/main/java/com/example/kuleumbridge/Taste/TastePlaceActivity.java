package com.example.kuleumbridge.Taste;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.kuleumbridge.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/* 맛집 메인 화면에서 하단 부분에 위치한
 지도로 보기 버튼 누르면 나타나는 화면 구성*/

public class TastePlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String kind[] = new String[200]; //종류
    String name[]= new String[200]; //상호명
    String address[] = new String[200]; //주소
    double latitude[] = new double[200]; //위도
    double longitude[] = new double[200]; //경도
    int RowEnd = 0;  // 행 개수 세는 변수

    //엑셀 불러서 값 저장하는 과정
    public void Excel() {
        Workbook workbook = null;
        Sheet sheet = null;

        try {

            InputStream inputStream = getBaseContext().getResources().getAssets().open("place.xls");
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
        Excel();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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

            //종류에 따라 마커 이미지 변경
            if (kind[i].contains("한식")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.hansik);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }else if(kind[i].contains("분식")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bunsik);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            } else if(kind[i].contains("카페/디저트")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.dessert);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            } else if(kind[i].contains("돈까스/회/일식")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.sushi);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            } else if(kind[i].contains("치킨/햄버거")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.hamburger);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            } else if(kind[i].contains("아시안/양식")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.asian);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }else if(kind[i].contains("중식")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.jjang);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            }else if(kind[i].contains("고기")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.meat);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }else if(kind[i].contains("술집")) {
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.alchol);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,80,80,false);
                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }

            mMap.addMarker(marker);


        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5425241,127.073699), 16));


    }

    }





