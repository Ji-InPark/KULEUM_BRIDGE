package com.KonDuckJoa.kuleumbridge.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.KonDuckJoa.kuleumbridge.Fragment.HomeFragment;
import com.KonDuckJoa.kuleumbridge.Fragment.NoticeFragment;
import com.KonDuckJoa.kuleumbridge.Fragment.TastePlaceFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments = new Fragment[3];
    private String[] pageTitles = {"홈", "맛집", "공지사항"};

    public SectionsPagerAdapter(FragmentManager fm)
    {
        super(fm);
        initializeFragments();
    }

    private void initializeFragments()
    {
        fragments[0] = new HomeFragment();
        fragments[1] = new TastePlaceFragment();
        fragments[2] = new NoticeFragment();
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
        return pageTitles[position];
    }

    @Override
    public int getCount()
    {
        // 리턴값은 페이지의 총 개수를 의미함.
        return fragments.length;
    }
}