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

import com.KonDuckJoa.kuleumbridge.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.R;

public class homeFragment extends Fragment {

    public homeFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout_sep,container,false);
        editStudentID(view);
        return view;
    }

    // 학생증 정보 수정
    private void editStudentID(View view)
    {
        System.out.println("editStudentID 실행");
        ImageView img_menu = view.findViewById(R.id.menu_img_sep);
        TextView name_menu = view.findViewById(R.id.menu_name_sep);
        ImageView img = view.findViewById(R.id.studentCard_photo_sep);
        TextView name = view.findViewById(R.id.studentCard_name_sep);
        TextView stdNum = view.findViewById(R.id.studentCard_stdNum_sep);
        TextView major = view.findViewById(R.id.studentCard_major_sep);
        TextView birthday = view.findViewById(R.id.studentCard_birthday_sep);

        try {
            // 로그인 후 메인 메뉴 우측 상단 학생 사진 세팅
            img_menu.setImageBitmap(getImageBitMap());

            // 로그인 후 안녕, ㅁㅁㅁ! 세팅
            name_menu.setText(getString(R.string.hello, UserInfo.getInstance().getUSER_NM()));

            // 학생증 사진 세팅
            img.setImageBitmap(getImageBitMap());

            // 학생증 이름 세팅
            name.setText(getString(R.string.name, UserInfo.getInstance().getUSER_NM()));

            // 학생증 학번 세팅
            stdNum.setText(getString(R.string.userid, UserInfo.getInstance().getUSER_ID()));

            // 학생증 학과 세팅
            major.setText(getString(R.string.dept, UserInfo.getInstance().getDEPT_TTNM()));

            // 학생증 생년월일 세팅
            birthday.setText(getString(R.string.birth, UserInfo.getInstance().getRESNO()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 이미지 비트맵 반환
    public Bitmap getImageBitMap()
    {
        byte[] encodeByte = Base64.decode(UserInfo.getInstance().getPHOTO(), Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    }

}
