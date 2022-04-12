package com.KonDuckJoa.kuleumbridge.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.R;

public class HomeFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.home_layout,container,false);
        editStudentID(view);

        return view;
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

    // 이미지 비트맵 반환
    public Bitmap getImageBitMap()
    {
        byte[] encodeByte = Base64.decode(UserInfo.getInstance().getPhotoUrl(), Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    }
}
