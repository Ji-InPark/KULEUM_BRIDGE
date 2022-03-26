package com.KonDuckJoa.kuleumbridge.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;

import com.KonDuckJoa.kuleumbridge.fragment.gradeCheckFragment;
import com.KonDuckJoa.kuleumbridge.fragment.homeFragment;
import com.KonDuckJoa.kuleumbridge.fragment.noticeFragment;
import com.KonDuckJoa.kuleumbridge.fragment.tastePlaceFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    Fragment[] fragments = new Fragment[4];

    // 요건 별거아님
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        initFragments();
    }

    private void initFragments() {
        fragments[0] = new homeFragment();
        fragments[1] = new noticeFragment();
        fragments[2] = new tastePlaceFragment();
        fragments[3] = new gradeCheckFragment();
    }


    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // 스와이프 후 현재 포지션에 따라 각각의 Fragment(페이지)를 리턴

        return fragments[position];
    }

    @Nullable
    @Override
    // 레이아웃 하단의 탭 이름을 세팅하는 함수
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0:
                return "홈";

            case 1:
                return "공지사항";

            case 2:
                return "맛집";

            case 3:
                return "성적조회";
        }
        return null;
    }

    @Override
    public int getCount() {
        // 리턴값은 페이지의 총 개수를 의미함.
        return 4;
    }
}