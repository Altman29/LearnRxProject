package com.csy.rx_retrofit;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.csy.rx_retrofit.module.Zip_3.ZipFragment;
import com.csy.rx_retrofit.module.cache_6.CacheFragment;
import com.csy.rx_retrofit.module.elementary_1.ElementaryFragment;
import com.csy.rx_retrofit.module.map_2.MapFragment;
import com.csy.rx_retrofit.module.token_flatmap_4.TokenFragment;
import com.csy.rx_retrofit.module.token_retryWhen_5.TokenAdvancedFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;
//    @Bind(R.id.toolBar) Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);//butterknife绑定

        /** 绑定toolbar*/
//        setSupportActionBar(toolBar);

        /** viewPager设置Adapter*/
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ElementaryFragment();//基本
                    case 1:
                        return new MapFragment();//map
                    case 2:
                        return new ZipFragment();//zip
                    case 3:
                        return new TokenFragment();
                    case 4:
                        return new TokenAdvancedFragment();
                    case 5:
                        return new CacheFragment();
                    default:
                        return new MapFragment();
                }
            }

            /** 返回标题*/
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.title_elementary);
                    case 1:
                        return getString(R.string.title_map);
                    case 2:
                        return getString(R.string.title_zip);
                    case 3:
                        return getString(R.string.title_token);
                    case 4:
                        return getString(R.string.title_token_advanced);
                    case 5:
                        return getString(R.string.title_cache);
                    default:
                        return getString(R.string.title_elementary);
                }
            }
        });
        /** 绑定ViewPager*/
        tabLayout.setupWithViewPager(viewPager);
    }
}
