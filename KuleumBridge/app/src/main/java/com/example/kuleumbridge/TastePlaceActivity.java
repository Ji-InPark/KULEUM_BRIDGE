package com.example.kuleumbridge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class TastePlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String name[]= new String[200];
    String address[]= new String[200];
    double latitude[]= new double[200]; //위도
    double longitude[]= new double[200]; //경도


    public void Excel() {
        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("place.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
            int  RowEnd = sheet.getColumn(0).length-1;

            for(int row = 1;row <= RowEnd;row++) {

                name[row]=sheet.getCell(1, row).getContents();
                address[row]=sheet.getCell(2,row).getContents();
                latitude[row]=Double.parseDouble(sheet.getCell(3,row).getContents());
                longitude[row] = Double.parseDouble(sheet.getCell(4,row).getContents());

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

    @Override
    public void onMapReady(final GoogleMap googleMap) {



        mMap = googleMap;


//건대 마커
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(new LatLng(37.5425241,127.073699)).title("건국대학교").snippet("창조적 혁신으로 미래를 선도하는 대학");
//
//        mMap.addMarker(markerOptions);

        for(int i =1; i< name.length; i++) {
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(latitude[i],longitude[i])).title(name[i]).snippet(address[i]);

            mMap.addMarker(marker);
        } //다중 마커 추가

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5425241,127.073699), 15));


    }

    }





