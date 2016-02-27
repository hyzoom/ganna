package com.ishraq.janna.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.model.Settings;
import com.ishraq.janna.service.SettingsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 2/27/2016.
 */
public class HomeFragment extends MainCommonFragment {
    private SettingsService settingsService;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsService = new SettingsService(getMainActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        return view;
    }

    private void setupTabIcons() {
        Settings settings = settingsService.getSettings();

        TextView aboutUsTab = (TextView) LayoutInflater.from(JannaApp.getContext()).inflate(R.layout.custom_tab, null);
        aboutUsTab.setText("عن مستشفى جنّة");
        tabLayout.getTabAt(0).setCustomView(aboutUsTab);


        if (settings.getLoggedInUser().getUserType().equals(getString(R.string.radio_doctor))) {
            TextView eventsTab = (TextView) LayoutInflater.from(JannaApp.getContext()).inflate(R.layout.custom_tab, null);
            eventsTab.setText("الأحداث");
            tabLayout.getTabAt(1).setCustomView(eventsTab);
        } else {
            TextView reservationTab = (TextView) LayoutInflater.from(JannaApp.getContext()).inflate(R.layout.custom_tab, null);
            reservationTab.setText("الحجوزات");
            tabLayout.getTabAt(1).setCustomView(reservationTab);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getMainActivity().getSupportFragmentManager());
        adapter.addFrag(new AboutFragment());

        Settings settings = settingsService.getSettings();
        if (settings.getLoggedInUser().getUserType().equals(getString(R.string.radio_doctor))) {
            adapter.addFrag(new EventFragment());
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



    ///////////////////////////////////////////////////////////////////////////////////////////////
    protected void hideTabBar() {
        tabLayout.animate().translationY(-tabLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    protected void showTabBar() {
        tabLayout.animate().translationY(tabLayout.getHeight()).setInterpolator(new DecelerateInterpolator(2));
    }

}
