package com.example.kuleumbridge;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.kuleumbridge.Data.Grade;

import java.util.ArrayList;
import java.util.Arrays;


public class GradeDetailList extends Fragment {
    View rootView;
    ArrayList<Grade> gradeAllArr2;
    ArrayList<String> a_yy; //년도 + 학기
    ArrayList<String> a_div;  //이수구분
    ArrayList<String> a_name; //이름
    ArrayList<String> a_hak; //학점
    ArrayList<String> a_grd; //성적

    TextView textView;
    String flag = "";

    String s_yy = "";
    String s_div = "";
    String s_name = "";
    String s_hak = "";
    String s_grd = "";


    TableLayout tableLayout;

    public GradeDetailList() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.grade_detail_list, container, false);
        tableLayout = (TableLayout) rootView.findViewById(R.id.tablelayout);
        if (getArguments() != null) {
            gradeAllArr2 = getArguments().getParcelableArrayList("gaa");
            flag= getArguments().getString("tabb"); //클릭한 탭 내용 받아옴(ex. 2021년 1학기)
        }

        for (int i = 0; i < gradeAllArr2.size(); i++) {
            s_yy += gradeAllArr2.get(i).getYY() + " " + gradeAllArr2.get(i).getSHTM_NM() + ",";
            s_div += gradeAllArr2.get(i).getPOBT_DIV() + ",";
            s_name += gradeAllArr2.get(i).getHAKSU_NM() + ",";
            s_hak += gradeAllArr2.get(i).getPNT() + ",";
            s_grd += gradeAllArr2.get(i).getGRD() + ",";
        }

        a_yy = new ArrayList<String>(Arrays.asList(s_yy.split(",")));
        a_div = new ArrayList<String>(Arrays.asList(s_div.split(",")));
        a_name = new ArrayList<String>(Arrays.asList(s_name.split(",")));
        a_hak = new ArrayList<String>(Arrays.asList(s_hak.split(",")));
        a_grd = new ArrayList<String>(Arrays.asList(s_grd.split(",")));

        //Arraylist에서 불필요한 요소들 없애주는 과정
        a_yy.remove(a_yy.size() - 1);
        a_div.remove(a_div.size() - 1);
        a_name.remove(a_name.size() - 1);
        a_hak.remove(a_hak.size() - 1);
        a_grd.remove(a_grd.size() - 1);

        for (int j = 0; j < a_yy.size() - 1; j++) {
            if (a_yy.get(j).contains("소계")) {
                a_yy.remove(j);
                a_div.remove(j);
                a_name.remove(j);
                a_hak.remove(j);
                a_grd.remove(j);
            }

        }



        for (int k = 0; k < a_yy.size() - 1; k++) {

            //클릭한 탭내용과 일치하는 성적만 불러오는 과정(ex. 2021년 2학기 클릭시, 해당 학기 성적만 불러옴)
            if (a_yy.get(k).contains(flag)) {

                TableRow tableRow = new TableRow(getActivity());
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                for (int h = 0; h < 4; h++) {

                    textView = new TextView(getActivity());
                    textView.setTextSize(17);
                    textView.setWidth(0);
                    textView.setPadding(10,10,10,30);
                    textView.setGravity(Gravity.CENTER);
                    textView.setSelected(true);

                    switch (h) {
                        case 0:
                            textView.setText(a_div.get(k));
                            break;
                        case 1:
                            textView.setText(a_name.get(k));
                            break;
                        case 2:
                            textView.setText(a_hak.get(k));
                            break;
                        case 3:
                            textView.setText(a_grd.get(k));
                            break;
                    }


                    tableRow.addView(textView);


                }

                tableLayout.addView(tableRow);


            }



        }
        return rootView;
    }
}







