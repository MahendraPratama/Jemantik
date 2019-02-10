package com.jentiknyamuk.mpdev.jentiknyamuk;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuUserActivity extends AppCompatActivity {
    private ViewPager mPager;
    private MenuAdminActivity.SectionPageAdapter mSectPageAdapter;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);

        mPager = (ViewPager) findViewById(R.id.tab_pager);
        SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(pagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mPager);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }
    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "Home","Map", "Pemantauan", "Setting" };
        private int tabImage[] = new int[] {R.drawable.ic_home, R.drawable.ic_mapjentik
                , R.drawable.ic_datapantau, R.drawable.ic_setting};

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position){
            final View v = LayoutInflater.from(getApplication()).inflate(R.layout.tab_header_custom_layout, null);
            TextView tv = (TextView) v.findViewById(R.id.tab_title_customLayout);
            ImageView iv = (ImageView) v.findViewById(R.id.tab_image_customLayout);
            tv.setText(tabTitles[position]);
            iv.setImageResource(tabImage[position]);

            return v;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    HomeFragment homeFragment = new HomeFragment();
                    return homeFragment;
                case 1 :
                    MapsActivity mapsActivity = new MapsActivity();
                    return mapsActivity;
                case 2 :
                    DataKKFragment dataKKFragment = new DataKKFragment();
                    return dataKKFragment;
                case 3 :
                    AccountFragment accountFragment = new AccountFragment();
                    return accountFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    class SectionPageAdapter extends FragmentPagerAdapter{
        public SectionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    HomeFragment homeFragment = new HomeFragment();
                    return homeFragment;
                case 1 :
                    MapsActivity mapsActivity = new MapsActivity();
                    return mapsActivity;
                case 2 :
                    KaderFragment kaderFragment = new KaderFragment();
                    return kaderFragment;
                case 3 :
                    DataKKFragment dataKKFragment = new DataKKFragment();
                    return dataKKFragment;
                case 4 :
                    AccountFragment accountFragment = new AccountFragment();
                    return accountFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
