package com.KonDuckJoa.kuleumbridge.Taste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.KonDuckJoa.kuleumbridge.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class TastePlaceList extends AppCompatActivity { //맛집 리스트 출력
    ListView listExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taste_list);
        listExcel = findViewById(R.id.list_excel);

        getExcelData();
    }

    //엑셀 값 읽어들이는 과정
    public void getExcelData()
    {
        ArrayList<TastePlaceListData> tastePlaceListDataArray = new ArrayList<>();
        Workbook workbook = null;
        Sheet sheet;

        // 어떤 버튼을 눌렀는지 받아서 저장 (ex: 한식)
        String tasteButtonName = getIntent().getStringExtra("buttonName");

        try
        {
            InputStream inputStream = getBaseContext().getResources().getAssets().open("place.xls");

            workbook = Workbook.getWorkbook(inputStream);
            sheet = workbook.getSheet(0);

            int RowEnd = sheet.getColumn(0).length - 1;

            for (int i = 1; i <= RowEnd; i++)
            {
                TastePlaceListData tastePlaceListData = new TastePlaceListData();
                String kinds = sheet.getCell(0, i).getContents(); //종류(ex: 한식, 중식..)

                if (kinds.contains(tasteButtonName))
                {
                    tastePlaceListData.name = sheet.getCell(1, i).getContents(); //상호명
                    tastePlaceListData.address = sheet.getCell(2,i).getContents(); //주소
                    tastePlaceListData.oneLineComment = sheet.getCell(5,i).getContents(); //한줄평
                    tastePlaceListData.latitude = Double.parseDouble(sheet.getCell(3,i).getContents()); //위도
                    tastePlaceListData.longitude = Double.parseDouble(sheet.getCell(4,i).getContents()); //경도

                    tastePlaceListDataArray.add(tastePlaceListData);
                }
            }
        }
        catch (IOException | BiffException e)
        {
            e.printStackTrace();
        }
        finally
        {
            ListAdapter tastePlaceListAdapter = new TastePlaceListAdapter(tastePlaceListDataArray);
            listExcel.setAdapter(tastePlaceListAdapter);

            workbook.close();

            /* 리스트 중 한개 클릭 시, 해당 값(상호명, 주소, 위도, 경도)을
             TastePlaceList(Activity) -> TastePlaceInfo(Activity)로 전달하는 과정 */
            listExcel.setOnItemClickListener((adapterView, view, position, id) -> {
                Intent intent = new Intent(getApplicationContext(),TastePlaceInfo.class);

                intent.putExtra("name",tastePlaceListDataArray.get(position).name);
                intent.putExtra("address",tastePlaceListDataArray.get(position).address);
                intent.putExtra("latitude",tastePlaceListDataArray.get(position).latitude);
                intent.putExtra("longitude",tastePlaceListDataArray.get(position).longitude);

                startActivity(intent);
            });
        }
    }
}
