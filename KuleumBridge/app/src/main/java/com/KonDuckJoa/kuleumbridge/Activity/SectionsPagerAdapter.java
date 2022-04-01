package com.KonDuckJoa.kuleumbridge.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.KonDuckJoa.kuleumbridge.Fragment.GradeCheckFragment;
import com.KonDuckJoa.kuleumbridge.Fragment.HomeFragment;
import com.KonDuckJoa.kuleumbridge.Fragment.NoticeFragment;
import com.KonDuckJoa.kuleumbridge.Fragment.TastePlaceFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    Fragment[] fragments = new Fragment[4];

    public SectionsPagerAdapter(FragmentManager fm)
    {
        super(fm);
        initializeFragments();
    }

    private void initializeFragments()
    {
        fragments[0] = new HomeFragment();
        fragments[1] = new NoticeFragment();
        fragments[2] = new TastePlaceFragment();
        fragments[3] = new GradeCheckFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Nullable
    @Override
    // 레이아웃 하단의 탭 이름을 세팅하는 함수
    public CharSequence getPageTitle(int position)
    {
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
    public int getCount()
    {
        // 리턴값은 페이지의 총 개수를 의미함.
        return fragments.length;
    }
}