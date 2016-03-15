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

        getMainActivity().getToolbar().setTitle(getResources().getString(R.string.app_name));
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
        aboutUsTab.setText(getResources().getString(R.string.tab_about));
        tabLayout.getTabAt(0).setCustomView(aboutUsTab);

        if (settings.getLoggedInUser().getUserType() == 1) {
            TextView eventsTab = (TextView) LayoutInflater.from(JannaApp.getContext()).inflate(R.layout.custom_tab, null);
            eventsTab.setText(getResources().getString(R.string.tab_session));
            tabLayout.getTabAt(1).setCustomView(eventsTab);
            if (settings.getLoggedInUser().getMobile().equals("01112220298")) {
                TextView quesTab = (TextView) LayoutInflater.from(JannaApp.getContext()).inflate(R.layout.custom_tab, null);
                quesTab.setText(getResources().getString(R.string.tab_question));
                tabLayout.getTabAt(2).setCustomView(quesTab);
            }
        } else {
            TextView reservationTab = (TextView) LayoutInflater.from(JannaApp.getContext()).inflate(R.layout.custom_tab, null);
            reservationTab.setText(getResources().getString(R.string.tab_booking));
            tabLayout.getTabAt(1).setCustomView(reservationTab);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new AboutFragment());

        Settings settings = settingsService.getSettings();
        if (settings.getLoggedInUser().getUserType() == 1) {
            adapter.addFrag(new EventFragment());
            if (settings.getLoggedInUser().getMobile().equals("01112220298")) {
                adapter.addFrag(new QuestionFragment());
            }
        } else {
            adapter.addFrag(new ReservationFragment());
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
