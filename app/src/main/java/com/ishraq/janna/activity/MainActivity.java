package com.ishraq.janna.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.ishraq.janna.R;
import com.ishraq.janna.fragment.main.AboutUs;
import com.ishraq.janna.fragment.main.Events;
import com.ishraq.janna.fragment.main.Reservation;
import com.ishraq.janna.model.Settings;
import com.ishraq.janna.service.SettingsService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SettingsService settingsService;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsService = new SettingsService(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        Settings settings = settingsService.getSettings();

        TextView aboutUsTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        aboutUsTab.setText("عن مستشفى جنّة");
        tabLayout.getTabAt(0).setCustomView(aboutUsTab);


        if (settings.getLoggedInUser().getUserType().equals(getString(R.string.radio_doctor))) {
            TextView eventsTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            eventsTab.setText("الأحداث");
            tabLayout.getTabAt(1).setCustomView(eventsTab);
        } else {
            TextView reservationTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            reservationTab.setText("الحجوزات");
            tabLayout.getTabAt(1).setCustomView(reservationTab);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AboutUs());

        Settings settings = settingsService.getSettings();
        if (settings.getLoggedInUser().getUserType().equals(getString(R.string.radio_doctor))) {
            adapter.addFrag(new Events());
        } else {
            adapter.addFrag(new Reservation());
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }

}
