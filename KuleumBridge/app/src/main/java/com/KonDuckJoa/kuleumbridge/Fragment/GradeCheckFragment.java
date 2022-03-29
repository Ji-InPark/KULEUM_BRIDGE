package com.KonDuckJoa.kuleumbridge.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.Grade.Grade;
import com.KonDuckJoa.kuleumbridge.R;

import java.util.ArrayList;


public class GradeCheckFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grade_check_layout,container,false);
        gradeNowSuccess(view);
        return view;
    }

    private void gradeNowSuccess(View view)
    {
        try {
            // UserInfoClass.getInstance()에 얻어온 정보 저장 - 금학기성적
            TableLayout tableLayout = view.findViewById(R.id.grade_now_tablelayout);
            ArrayList<Grade> gradeNow = UserInfo.getInstance().getGradeNow();

            for (int i = 0; i < gradeNow.size(); i++) {
                TableRow tableRow = new TableRow(view.getContext());
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                for (int j = 0; j < 4; j++) {
                    TextView textView = new TextView(view.getContext());
                    textView.setTextSize(16);
                    textView.setTextColor(Color.parseColor("#000000"));
                    textView.setPadding(10, 0, 20, 50);
                    textView.setWidth(0);

                    //글자 수 많으면 ... 으로 처리
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setSelected(true);

                    switch (j) {
                        case 0:
                            textView.setText(gradeNow.get(i).getPOBT_DIV());
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                            break;
                        case 1:
                            textView.setText(gradeNow.get(i).getHAKSU_NM());
                            textView.setGravity(Gravity.START);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                            break;
                        case 2:
                            textView.setText(gradeNow.get(i).getPNT());
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
                            break;
                        case 3:
                            textView.setText(gradeNow.get(i).getGRD());
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f));
                            break;
                    }
                    tableRow.addView(textView);
                }
                tableLayout.addView(tableRow);
            }
        }catch (NullPointerException e) { //UserInfoClass.getInstance() 객체가 비었을때 예외처리
            TextView textView = new TextView(view.getContext());
            textView.setText("해당 학기 성적이 존재하지 않습니다.");
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL); // 텍스트뷰 가로 세로 중앙 정렬

        }
    }



}
