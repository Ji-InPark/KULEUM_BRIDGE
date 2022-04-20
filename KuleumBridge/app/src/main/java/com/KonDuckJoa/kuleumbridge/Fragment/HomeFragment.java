package com.KonDuckJoa.kuleumbridge.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.KonDuckJoa.kuleumbridge.Activity.MainActivity;
import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.Grade.Grade;
import com.KonDuckJoa.kuleumbridge.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.home_layout, container,false);
        editStudentID(view);
        gradeNowSuccess(view);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        MainActivity.stopLoadingAnimation();
    }

    // 학생증 정보 수정
    private void editStudentID(View view)
    {
        TextView name_menu = view.findViewById(R.id.menu_name);
        ImageView img = view.findViewById(R.id.studentCard_photo);
        TextView name = view.findViewById(R.id.studentCard_name);
        TextView stdNum = view.findViewById(R.id.studentCard_stdNum);
        TextView major = view.findViewById(R.id.studentCard_major);
        TextView birthday = view.findViewById(R.id.studentCard_birthday);

        try
        {
            // 로그인 후 안녕, ㅁㅁㅁ! 세팅
            name_menu.setText(getString(R.string.hello, UserInfo.getInstance().getUserName()));

            // 학생증 사진 세팅
            img.setImageBitmap(getImageBitMap());

            // 학생증 이름 세팅
            name.setText(getString(R.string.name, UserInfo.getInstance().getUserName()));

            // 학생증 학번 세팅
            stdNum.setText(getString(R.string.userid, UserInfo.getInstance().getUserId()));

            // 학생증 학과 세팅
            major.setText(getString(R.string.dept, UserInfo.getInstance().getDepartmentTotalName()));

            // 학생증 생년월일 세팅
            birthday.setText(getString(R.string.birth, UserInfo.getInstance().getBirthDate()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void gradeNowSuccess(View view)
    {
        try
        {
            // UserInfoClass.getInstance()에 얻어온 정보 저장 - 금학기성적
            TableLayout tableLayout = view.findViewById(R.id.grade_now_tablelayout_home);
            ArrayList<Grade> gradeNowArray = UserInfo.getInstance().getGradeNow();

            for (int i = 0; i < gradeNowArray.size(); i++)
            {
                TableRow tableRow = new TableRow(view.getContext());
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                for (int j = 0; j < 4; j++)
                {
                    TextView textView = new TextView(view.getContext());
                    textView.setTextSize(16);
                    textView.setTextColor(Color.parseColor("#000000"));
                    textView.setPadding(10, 0, 20, 50);
                    textView.setWidth(0);

                    //글자 수 많으면 ... 으로 처리
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setSelected(true);

                    switch (j)
                    {
                        case 0:
                            textView.setText(gradeNowArray.get(i).getCompletedDivision());
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                            break;
                        case 1:
                            textView.setText(gradeNowArray.get(i).getSubjectName());
                            textView.setGravity(Gravity.START);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));
                            break;
                        case 2:
                            textView.setText(gradeNowArray.get(i).getGradeCount());
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f));
                            break;
                        case 3:
                            textView.setText(gradeNowArray.get(i).getGradeRate());
                            textView.setGravity(Gravity.CENTER);
                            textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f));
                            break;
                    }
                    tableRow.addView(textView);
                }
                tableLayout.addView(tableRow);
            }
        }
        catch (NullPointerException e) //UserInfoClass.getInstance() 객체가 비었을때 예외처리
        {
            TextView textView = new TextView(view.getContext());
            textView.setText("해당 학기 성적이 존재하지 않습니다.");
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL); // 텍스트뷰 가로 세로 중앙 정렬
        }
    }

    // 이미지 비트맵 반환
    private Bitmap getImageBitMap()
    {
        byte[] encodeByte = Base64.decode(UserInfo.getInstance().getPhotoUrl(), Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    }
}
