package com.KonDuckJoa.kuleumbridge.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.KonDuckJoa.kuleumbridge.Activity.MainActivity;
import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.Grade.Grade;
import com.KonDuckJoa.kuleumbridge.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomeFragment extends Fragment
{
    private DrawerLayout mDrawer;
    Context context;

    @Override
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.home_layout, container,false);

        setActionBarSetting(view.findViewById(R.id.toolbar));

        mDrawer = view.findViewById(R.id.DrawLayout);
        context = container.getContext();

        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem ->
        {
            menuItem.setChecked(true);
            mDrawer.closeDrawers();

            int id = menuItem.getItemId();

            switch(id)
            {
                case R.id.Drawer_setting:
                    Toast.makeText(context, ":???????????? ????????? ???????????????", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Drawer_logout:
                    clearAutoLoginInfo();
                    startActivity(Intent.makeRestartActivityTask(getActivity().getIntent().getComponent()));    // Activity ????????? ??????
                    break;
            }

            return true;
        });

        editStudentID(view);
        gradeNowSuccess(view);

        return view;
    }

    private void clearAutoLoginInfo()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", "");
        editor.putString("pwd", "");

        editor.apply();
    }

    // ????????? ?????? ??????
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
            // ????????? ??? ??????, ?????????! ??????
            name_menu.setText(getString(R.string.hello, UserInfo.getInstance().getUserName()));

            // ????????? ?????? ??????
            img.setImageBitmap(getImageBitMap());

            // ????????? ?????? ??????
            name.setText(getString(R.string.name, UserInfo.getInstance().getUserName()));

            // ????????? ?????? ??????
            stdNum.setText(getString(R.string.userid, UserInfo.getInstance().getUserId()));

            // ????????? ?????? ??????
            major.setText(getString(R.string.dept, UserInfo.getInstance().getDepartmentTotalName()));

            // ????????? ???????????? ??????
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
            // UserInfoClass.getInstance()??? ????????? ?????? ?????? - ???????????????
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
                    setTextViewProperty(textView);
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
        catch (NullPointerException e) //UserInfoClass.getInstance() ????????? ???????????? ????????????
        {
            TextView textView = new TextView(view.getContext());
            textView.setText("?????? ?????? ????????? ???????????? ????????????.");
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL); // ???????????? ?????? ?????? ?????? ??????
        }
    }

    // ????????? ????????? ??????
    private Bitmap getImageBitMap()
    {
        byte[] encodeByte = Base64.decode(UserInfo.getInstance().getPhotoUrl(), Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    }

    private void setTextViewProperty(TextView textView)
    {
        textView.setTextSize(16);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setPadding(10, 0, 20, 50);
        textView.setWidth(0);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSelected(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home) // ?????? ?????? ?????? ????????? ???
        {
            mDrawer.openDrawer(GravityCompat.START);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarSetting(Toolbar toolbar)
    {
        AppCompatActivity activity = (AppCompatActivity)getActivity();

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ?????? ?????? ?????? ??????
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_menu_button); // ?????? ?????? ?????? ??????
    }
}
