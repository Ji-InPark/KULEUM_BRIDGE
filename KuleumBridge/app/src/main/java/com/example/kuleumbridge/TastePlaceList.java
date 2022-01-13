package com.example.kuleumbridge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class TastePlaceList extends AppCompatActivity { //맛집 리스트 출력

    ListView list_excel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taste_list_main);
        list_excel = (ListView)findViewById(R.id.list_excel);

        Excel();



    }
    public void Excel() { //엑셀 값 읽어들이는 과정
        ArrayList<TastePlaceListData> listViewData = new ArrayList<>();
        Workbook workbook = null;
        Sheet sheet = null;
        // 어떤 버튼을 눌렀는지 받아서 저장 (ex: 한식)
        String Taste_Button = getIntent().getStringExtra("parameter");

        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("place.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
            int  RowEnd = sheet.getColumn(0).length - 1;

            for (int row = 1; row <= RowEnd; row++) {
                TastePlaceListData listData = new TastePlaceListData();
                String kinds = sheet.getCell(0, row).getContents();
                if (kinds.contains(Taste_Button)) {
                    listData.name = sheet.getCell(1, row).getContents();
                    listData.address = sheet.getCell(2,row).getContents();
                    listData.mention = sheet.getCell(5,row).getContents();
                    listData.latitude = Double.parseDouble(sheet.getCell(3,row).getContents());
                    listData.longitude = Double.parseDouble(sheet.getCell(4,row).getContents());


                    listViewData.add(listData);

                }

            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            ListAdapter oAdapter = new TastePlaceListAdapter(listViewData);
            list_excel.setAdapter(oAdapter);

            workbook.close();

            list_excel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(),TastePlaceInfo.class);
                    intent.putExtra("name",listViewData.get(position).name);
                    intent.putExtra("address",listViewData.get(position).address);
                    intent.putExtra("latitude",listViewData.get(position).latitude);
                    intent.putExtra("longitude",listViewData.get(position).longitude);
                    startActivity(intent);



                }
            });
        }
    }


    }
