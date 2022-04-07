package com.KonDuckJoa.kuleumbridge.Grade;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.R;

import java.util.ArrayList;
import java.util.Arrays;


public class GradeAllList extends Fragment {
    private static ArrayList<Grade> gradeAllArray;

    private static ArrayList<String> yearArray = new ArrayList<>();       //년도 + 학기
    private static ArrayList<String> divisionArray = new ArrayList<>();   //이수구분
    private static ArrayList<String> nameArray = new ArrayList<>();       //이름
    private static ArrayList<String> gradeCountArray = new ArrayList<>(); //학점
    private static ArrayList<String> gradeRateArray = new ArrayList<>();  //성적

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView;
        TableLayout tableLayout;
        String selectedYear = "";

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.grade_detail_list, container, false);
        tableLayout = rootView.findViewById(R.id.tablelayout);

        if (getArguments() != null)
        {
            selectedYear = getArguments().getString("tabName"); //클릭한 탭 내용 받아옴(ex. 2021년 1학기)
        }

        for (int i = 0; i < yearArray.size(); i++)
        {
            //클릭한 탭내용과 일치하는 성적만 불러오는 과정(ex. 2021년 2학기 클릭시, 해당 학기 성적만 불러옴)
            if (yearArray.get(i).contains(selectedYear))
            {
                TableRow tableRow = new TableRow(getActivity());
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                for (int j = 0; j < 4; j++)
                {
                    TextView textView = getTextViewWithSettings();

                    switch (j)
                    {
                        case 0:
                            textView.setText(divisionArray.get(i));
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
                            break;
                        case 1:
                            textView.setText(nameArray.get(i));
                            textView.setGravity(Gravity.LEFT);
                            textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,2.5f));
                            break;
                        case 2:
                            textView.setText(gradeCountArray.get(i));
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.6f));
                            break;
                        case 3:
                            textView.setText(gradeRateArray.get(i));
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.6f));
                            break;
                    }

                    tableRow.addView(textView);
                }

                tableLayout.addView(tableRow);
            }
        }

        return rootView;
    }

    public static void setGradeAllInfo()
    {
        yearArray = new ArrayList<>();       //년도 + 학기
        divisionArray = new ArrayList<>();   //이수구분
        nameArray = new ArrayList<>();       //이름
        gradeCountArray = new ArrayList<>(); //학점
        gradeRateArray = new ArrayList<>();  //성적
        gradeAllArray=new ArrayList<>(); // 함수 속에 담겨있는 파일들 초기화


        gradeAllArray = UserInfo.getInstance().getGradeAll();

        // 소계 제외
        for (int i = 0; i < UserInfo.getInstance().getGradeAll().size(); i++)
        {
            if (gradeAllArray.get(i).getSemester().contains("소계")) continue;

            yearArray.add(gradeAllArray.get(i).getCompletedYear() + " " + gradeAllArray.get(i).getSemester());
            divisionArray.add(gradeAllArray.get(i).getCompletedDivision());
            nameArray.add(gradeAllArray.get(i).getSubjectName());
            gradeCountArray.add(gradeAllArray.get(i).getGradeCount());
            gradeRateArray.add(gradeAllArray.get(i).getGradeRate());
        }
    }

    private TextView getTextViewWithSettings()
    {
        TextView textView = new TextView(getActivity());

        textView.setTextSize(19);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setWidth(0);
        textView.setPadding(0,0,10,70);
        textView.setSelected(true);

        return textView;
    }
}







