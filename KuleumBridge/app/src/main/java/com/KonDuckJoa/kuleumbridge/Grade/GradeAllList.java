package com.KonDuckJoa.kuleumbridge.Grade;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
                TextView average = getTextViewWithSettings();
                double[] grade3 = gradeCalculate(gradeRateArray, gradeCountArray, divisionArray);
                average.setText("평균" + grade3[0] +" "+grade3[1]);

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

//        double[] Test = gradeCalculate(gradeRateArray,gradeCountArray,divisionArray);
//        System.out.println("평균 계산 테스트" +Test[0] +" "+ Test[1]);

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

    //평균 계산
    private double[] gradeCalculate(ArrayList<String> grade, ArrayList<String> hak, ArrayList<String> division) {
        double[] gradeResult  = new double[2]; //Grade[0] = 전체 평균 , Grade[1] = 전공 평균
        double[] gradeRate = new double[grade.size()]; //숫자 성적(ex. 4.5, 4.0)
        double[] hakRate = new double[hak.size()]; //학점
        double[] majorGrade = new double[grade.size()]; //전공 과목 숫자 성적
        double[] majorHak = new double[hak.size()]; //전공 과목 학점
        double gradeRateRes = 0.0; //성적 합계
        double hakRateRes = 0.0; //학점 합계

        if(grade!=null) {
            for(int i=0; i<grade.size(); i++) {
                switch (grade.get(i)) {
                    case "A+":
                        gradeRate[i] = 4.5;
                    case "A" :
                        gradeRate[i] = 4.0;
                    case "B+" :
                        gradeRate[i] = 3.5;
                    case "B" :
                        gradeRate[i] = 3.0;
                    case "C+" :
                        gradeRate[i] = 2.5;
                    case "C" :
                        gradeRate[i] = 2.0;
                    case "D+" :
                        gradeRate[i] = 1.5;
                    case "D" :
                        gradeRate[i] = 1.0;
                    case "F": case "P": case "N":
                        gradeRate[i] = 0;

                }

            }

        }

        /* 평균 계산법
        ex) 미분적분학 4.5(A+) * 3(학점) = 13.5
            선형대수학 3.0(B)  * 3(학점) = 9
            학점 : (13.5 + 9) / 6(총 학점) = 3.75
         */

        for(int i=0; i<hak.size(); i++) { //학점 수 다 더하는 과정
            hakRate[i] = Double.parseDouble(hak.get(i));
            hakRateRes+=hakRate[i];
        }

        for(int i =0; i<hakRate.length; i++) { //전체 평균 계산 과정
            gradeRateRes+=(gradeRate[i]*hakRate[i]);
        }

        gradeResult[0] = gradeRateRes/hakRateRes; //전체 평균

        //전공 평균 구하는 과정
        for(int i=0; i<division.size(); i++) {
            if(division.get(i).equals("전필")||division.get(i).equals("전선")) {
                majorGrade[i] = gradeRate[i];
                majorHak[i] = hakRate[i];
            }
        }

        for(int i=0; i<majorGrade.length; i++) {
            gradeRateRes = 0.0;
            hakRateRes = 0.0;
            hakRateRes += majorHak[i];
            gradeRateRes += (majorHak[i] * majorGrade[i]);
        }

        gradeResult[1] = gradeRateRes/hakRateRes;


        return gradeResult;
    }



}







