package com.example.kuleumbridge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class TastePlaceList extends AppCompatActivity { //맛집 리스트 출력

    ListView list_excel;
    ArrayAdapter<String> arrayAdapter;
    String kinds[]= new String[200];
    String Taste_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taste_list);
        list_excel = (ListView)findViewById(R.id.list_excel);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        Excel();

        // 어떤 버튼을 눌렀는지 받아서 저장 (ex: 한식)
        Taste_Button = getIntent().getStringExtra("parameter");
        System.out.println(Taste_Button);
    }
    public void Excel() {
        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("place.xls");
            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);
            int  RowEnd = sheet.getColumn(0).length - 1;

            for(int row = 1;row <= RowEnd;row++) {
//                kinds[row]=sheet.getCell(0, row).getContents();
//                if(kinds[row].equals("한식")) {
//                    String excelload = kinds[row];
                String excelload = sheet.getCell(1, row).getContents();
                arrayAdapter.add(excelload);
            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            list_excel.setAdapter(arrayAdapter);

            workbook.close();

            list_excel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(TastePlaceList.this,TastePlaceInfo.class);
                    startActivity(intent);
                }
            });
        }
    }


    }
