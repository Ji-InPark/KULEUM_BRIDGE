package com.KonDuckJoa.kuleumbridge.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.KonDuckJoa.kuleumbridge.Notice.Notice;
import com.KonDuckJoa.kuleumbridge.Notice.NoticeHandler;
import com.KonDuckJoa.kuleumbridge.Notice.NoticeInfo;
import com.KonDuckJoa.kuleumbridge.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NoticeFragment extends Fragment {

    private Button noticeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_layout,container,false);
        addOnNoticeTabSelectedListener(view);
        for (int i = 0; i < 7; i++) {
            String category = NoticeHandler.getCategory(i);
            NoticeSuccess(view, category);
        }
        return view;
    }

    private void NoticeSuccess(View view, String notice_category)
    {
        TableLayout[] tables = {
                view.findViewById(R.id.notice_element_table0),
                view.findViewById(R.id.notice_element_table1),
                view.findViewById(R.id.notice_element_table2),
                view.findViewById(R.id.notice_element_table3),
                view.findViewById(R.id.notice_element_table4),
                view.findViewById(R.id.notice_element_table5),
                view.findViewById(R.id.notice_element_table6)
        };
        // 공지사항 테이블을 가져온 정보들을 바탕으로 채워준다.
        setNoticeTable(view, NoticeInfo.getInstance().getNotice(notice_category), tables[NoticeHandler.getIndex(notice_category)]);
    }

    private void setNoticeTable(View view,ArrayList<Notice> noticeArrayList, TableLayout table) {
        for(int i = 0; i < noticeArrayList.size(); i++)
        {
            TableRow tbr = new TableRow(view.getContext());
            tbr.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 3; j++)
            {
                TextView tv = new TextView(view.getContext());

                tv.setTextSize(16);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setPadding(10,0,20,50);
                tv.setWidth(0);

                // 글자 수 많으면 ... 으로 처리
                tv.setSelected(true);

                switch(j) {
                    case 0:
                        tv.setText(Integer.toString(i+1));
                        tv.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.3f));
                        break;
                    case 1:
                        String noticeTitle = noticeArrayList.get(i).getSUBJECT(); // 공지사항 제목
                        String noticeURL = noticeArrayList.get(i).getURL();       // 공지사항 URL
                        tv.setText(noticeTitle);
                        String regexNT = changeRegex(noticeTitle); // 정규표현식으로 바뀐 noticeTitle
                        Pattern pattern = Pattern.compile(regexNT); // 패턴에 컴파일되는 문자열은 정규표현식이 지켜져야 특수문자도 감지한다.
                        Linkify.TransformFilter mTransform = (matcher, s) -> {
                            return noticeURL;
                            // 스키마인 ""뒤에 noticeURL을 붙여서 리턴한다.
                        };
                        Linkify.addLinks(tv,pattern,"",null,mTransform);
                        tv.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,2.0f));
                        break;
                    case 2:
                        tv.setText(noticeArrayList.get(i).getPOSTED_DT());
                        tv.setLayoutParams(new TableRow.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,0.7f));
                        break;
                }
                tbr.addView(tv);
            }
            table.addView(tbr);
        }
    }

    private void addOnNoticeTabSelectedListener(View view) {
        TabLayout noticeTab = view.findViewById(R.id.notice_category_tab);
        setNoticeTabColor(noticeTab, "#000000");
        noticeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeTabNotice(pos,view);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    // 공지사항 탭바 상호작용 함수
    private void changeTabNotice(int selectedIndex, View view) {
        TableLayout[] tables = {
                view.findViewById(R.id.notice_element_table0),
                view.findViewById(R.id.notice_element_table1),
                view.findViewById(R.id.notice_element_table2),
                view.findViewById(R.id.notice_element_table3),
                view.findViewById(R.id.notice_element_table4),
                view.findViewById(R.id.notice_element_table5),
                view.findViewById(R.id.notice_element_table6)
        };

        for(int i = 0; i < 7; i++)
        {
            if(selectedIndex == i) {
                tables[i].setVisibility(View.VISIBLE);
            }
            else
                tables[i].setVisibility(View.GONE);
        }
    }


    // 문자열을 정규표현식을 만족하는 문자열로 바꿔주는 함수
    private String changeRegex(String str) {
        String changedStr = str.replace("(","\\(");
        changedStr = changedStr.replace(")","\\)");
        changedStr = changedStr.replace("[","\\[");
        changedStr = changedStr.replace("]","\\]");
        changedStr = changedStr.replace("★","\\★");
        changedStr = changedStr.replace("☆","\\☆");
        changedStr = changedStr.replace("?","\\?");
        changedStr = changedStr.replace("^","\\^");
        changedStr = changedStr.replace("&","\\&");
        changedStr = changedStr.replace("+","\\+");
        return changedStr;
    }

    // 공지사항 탭 텍스트 및 하단 표시부 색깔 변경
    public void setNoticeTabColor(TabLayout noticeTab, String colorString)
    {
        noticeTab.setSelectedTabIndicatorColor(Color.parseColor(colorString));
        noticeTab.setTabTextColors(Color.parseColor(colorString),Color.parseColor(colorString));
    }




}
