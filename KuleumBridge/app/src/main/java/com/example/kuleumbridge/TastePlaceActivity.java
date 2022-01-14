package com.example.kuleumbridge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class TastePlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String kind[] = new String[200]; //종류
    String name[]= new String[200]; //상호명
    String address[]= new String[200]; //주소
    double latitude[] = new double[200]; //위도
    double longitude[]= new double[200]; //경도
    Cell[] test;
    int RowEnd=0;

    public void Excel() {
        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("place.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
            RowEnd = sheet.getRows()-1;



            for(int row = 1;row <= RowEnd;row++) {

                kind[row]=sheet.getCell(0,row).getContents();

                name[row]=sheet.getCell(1, row).getContents();
                address[row]=sheet.getCell(2,row).getContents();

                NumberCell latitude2 = (NumberCell)sheet.getCell(3,row);
                latitude2.getNumberFormat().setMaximumFractionDigits(5);
                latitude[row]= Double.parseDouble(latitude2.getContents()); //위도

                NumberCell longitude2 = (NumberCell)sheet.getCell(4,row);
                longitude2.getNumberFormat().setMaximumFractionDigits(5);
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

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        String kinds = "";


        for(int i =1; i< RowEnd; i++) {
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(latitude[i],longitude[i])).title(name[i]).snippet(address[i]);

            if (kind[i].contains("한식")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }else if(kind[i].contains("분식")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            } else if(kind[i].contains("카페/디저트")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            } else if(kind[i].contains("돈까스/회/일식")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else if(kind[i].contains("치킨/햄버거")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            } else if(kind[i].contains("아시안/양식")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }else if(kind[i].contains("중식")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

            }else if(kind[i].contains("고기")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            }else if(kind[i].contains("술집")) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            }

            mMap.addMarker(marker);





        } //다중 마커 추가

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5425241,127.073699), 16));


    }

    }





