package com.example.kuleumbridge.Activity;
import android.graphics.Color;
import android.os.Bundle;
import com.example.kuleumbridge.databinding.TabViewpagerBinding;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.kuleumbridge.Activity.SectionsPagerAdapter;

/* 다른 프로젝트에서 테스트했던 메인 액티비티 코드 놔둘때가 없어서 임시로 만들어둠
   최악의 경우(레이아웃 제외 싹다 뜯어고치는 상황)에는 얘가 MainActivity가 될 수도 있음 */


public class SwipeActivity extends AppCompatActivity {
    // 여기서 TabViewPagerBinding은 layout의 tab_viewpager.xml이 바인딩 된 것임을 의미함.
    private TabViewpagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TabViewpagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //섹션페이저어댑터 객체 생성. 생성자 안에 들어가있는건 나도 잘 모르겠다. 별 중요한건 아닌거 같음.
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // tab_viewpager.xml에는 view_pager라는 id를 가지고있는 뷰페이저 객체가 존재하는데,
        // binding 변수는 tab_viewpager.xml가 바인딩된 것이므로 이렇게 xml파일 내부의 객체를 직접 가져올수 있음.
        // binding을 안 썼으면 findViewbyId를 이용했겠지만 방법은 잘 모르겠음. 구글링해서 나온 방법이 이거밖에 없었음.
        ViewPager viewPager = binding.viewPager;

        // 뷰페이저와 페이저어댑터 연결
        viewPager.setAdapter(sectionsPagerAdapter);

        // 상단의 뷰페이저와 동일. tab_viewpager.xml에 존재하는 mainTab id를 가진 TabLayout 객체
        // 바인딩했기때문에 findViewById 없이 이런 식으로 직접 가져올 수 있다.
        TabLayout tabs = binding.mainTab;

        // 탭과 뷰페이저 연결
        tabs.setupWithViewPager(viewPager);

        // 요건 그냥 탭 색깔 바꾸기
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabs.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#000000"));

    }

}
