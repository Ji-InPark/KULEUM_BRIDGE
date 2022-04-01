package com.KonDuckJoa.kuleumbridge.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.KonDuckJoa.kuleumbridge.R;
import com.KonDuckJoa.kuleumbridge.Taste.TasteHandler;
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

public class TastePlaceActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap tasteMap;
    private static String[] kindArray = new String[120]; // 종류
    private static String[] nameArray = new String[120]; // 상호명
    private static String[] addressArray = new String[120]; // 주소
    private static double[] latitudeArray = new double[120]; // 위도
    private static double[] longitudeArray = new double[120]; // 경도
    private static int RowEnd = 0;  // 행의 개수를 세는 변수

    //엑셀 불러서 값 저장하는 과정
    public static void getDataFromExcel(InputStream inputStream)
    {
        Workbook workbook;
        Sheet sheet;

        try
        {
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);

            RowEnd = sheet.getRows() - 1;

            for (int row = 1; row <= RowEnd; row++)
            {
                kindArray[row] = sheet.getCell(0, row).getContents();

                nameArray[row] = sheet.getCell(1, row).getContents();
                addressArray[row] = sheet.getCell(2, row).getContents();

                NumberCell latitude = (NumberCell) sheet.getCell(3, row);
                latitude.getNumberFormat().setMaximumFractionDigits(5); // 소수점 5자리까지
                latitudeArray[row] = Double.parseDouble(latitude.getContents()); //위도

                NumberCell longitude = (NumberCell) sheet.getCell(4, row);
                longitude.getNumberFormat().setMaximumFractionDigits(5); // 소수점 5자리까지
                longitudeArray[row] = Double.parseDouble(longitude.getContents()); // 경도
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (BiffException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taste_place);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // 지도 화면 구성
    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        tasteMap = googleMap;

        // 마커 추가 과정
        for(int i = 1; i < RowEnd; i++)
        {
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(latitudeArray[i], longitudeArray[i])).title(nameArray[i]).snippet(addressArray[i]);

            // 음식 종류에 따라 마커 이미지 변경
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(TasteHandler.getDrawableValue(kindArray[i]))).getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap,80,80,false);
            marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            tasteMap.addMarker(marker);
        }

        tasteMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5425241,127.073699), 16));
    }
}





