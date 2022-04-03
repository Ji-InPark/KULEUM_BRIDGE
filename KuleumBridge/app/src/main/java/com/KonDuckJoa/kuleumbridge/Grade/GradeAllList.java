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
    ArrayList<Grade> gradeAllArray;
    ArrayList<String> yearArray; //년도 + 학기
    ArrayList<String> divisionArray;  //이수구분
    ArrayList<String> nameArray; //이름
    ArrayList<String> gradeCountArray; //학점
    ArrayList<String> gradeRateArray; //성적

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView;

        String selectedYear = "";

        StringBuilder year = new StringBuilder();
        StringBuilder division = new StringBuilder();
        StringBuilder name = new StringBuilder();
        StringBuilder gradeCount = new StringBuilder();
        StringBuilder gradeRate = new StringBuilder();

        TableLayout tableLayout;

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.grade_detail_list, container, false);
        tableLayout = rootView.findViewById(R.id.tablelayout);

        if (getArguments() != null)
        {
            gradeAllArray = UserInfo.getInstance().getGradeAll();
            selectedYear = getArguments().getString("tabb"); //클릭한 탭 내용 받아옴(ex. 2021년 1학기)
        }

        for (int i = 0; i < gradeAllArray.size(); i++)
        {
            year.append(gradeAllArray.get(i).getCompletedYear()).append(" ").append(gradeAllArray.get(i).getSemester()).append(",");
            division.append(gradeAllArray.get(i).getCompletedDivision()).append(",");
            name.append(gradeAllArray.get(i).getSubjectName()).append(",");
            gradeCount.append(gradeAllArray.get(i).getGradeCount()).append(",");
            gradeRate.append(gradeAllArray.get(i).getGradeRate()).append(",");
        }

        yearArray = new ArrayList<>(Arrays.asList(year.toString().split(",")));
        divisionArray = new ArrayList<>(Arrays.asList(division.toString().split(",")));
        nameArray = new ArrayList<>(Arrays.asList(name.toString().split(",")));
        gradeCountArray = new ArrayList<>(Arrays.asList(gradeCount.toString().split(",")));
        gradeRateArray = new ArrayList<>(Arrays.asList(gradeRate.toString().split(",")));

        //Arraylist에서 불필요한 요소들 없애주는 과정
        yearArray.remove(yearArray.size() - 1);
        divisionArray.remove(divisionArray.size() - 1);
        nameArray.remove(nameArray.size() - 1);
        gradeCountArray.remove(gradeCountArray.size() - 1);
        gradeRateArray.remove(gradeRateArray.size() - 1);

        for (int i = 0; i < yearArray.size() - 1; i++)
        {
            if (yearArray.get(i).contains("소계"))
            {
                yearArray.remove(i);
                divisionArray.remove(i);
                nameArray.remove(i);
                gradeCountArray.remove(i);
                gradeRateArray.remove(i);
            }
        }

        for (int i = 0; i < yearArray.size() - 1; i++)
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







