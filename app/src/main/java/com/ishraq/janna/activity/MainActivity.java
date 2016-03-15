package com.ishraq.janna.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.fragment.NavigationDrawerFragment;
import com.ishraq.janna.fragment.main.BookingFragment;
import com.ishraq.janna.fragment.main.EventDetailsFragment;
import com.ishraq.janna.fragment.main.HomeFragment;
import com.ishraq.janna.fragment.main.MainCommonFragment;
import com.ishraq.janna.model.Settings;
import com.ishraq.janna.service.SettingsService;
import com.ishraq.janna.webservice.CommonRequest;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private SettingsService settingsService;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    private ContentLoadingProgressBar progressBar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsService = new SettingsService(this);

        initializeViews();

//        addFragment(new HomeFragment(), false, null);

        Fragment eventDetailsFragment = new EventDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("eventId", 1);

        eventDetailsFragment.setArguments(bundle);
        addFragment(eventDetailsFragment, true, null);

        addFragment(new EventDetailsFragment(), false, null);
    }

    private void initializeViews() {
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.hospital_name));
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        }
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ((ViewGroup) findViewById(R.id.wrapper)).removeAllViews();
        transaction.replace(R.id.wrapper, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void showConnectionError(MainCommonFragment fragment, final CommonRequest request, Throwable error, final View mainView) {
        Log.e(JannaApp.LOG_TAG, "error " + error.getLocalizedMessage());
        stopLoadingAnimator();

        final View errorView = LayoutInflater.from(JannaApp.getContext()).inflate(R.layout.fragment_connection_error, null, false);
        final ViewGroup wrapperView  = (ViewGroup) findViewById(R.id.wrapper);

        wrapperView.removeAllViews();
        wrapperView.addView(errorView);
        errorView.findViewById(R.id.retryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadingAnimator();
                wrapperView.removeAllViews();
                wrapperView.addView(mainView);
                request.execute();
            }
        });

        TextView errorMessageTextView = (TextView)errorView.findViewById(R.id.errorMessageTextView);
        errorMessageTextView.setText(R.string.common_error_message);
    }

    public void stopLoadingAnimator() {
        progressBar.setVisibility(View.GONE);
    }

    public void startLoadingAnimator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                addFragment(new BookingFragment(), true, null);
                break;
            case 1:
                Settings settings = settingsService.getSettings();
                settings.setLoggedInUser(null);

                settingsService.updateSettings(settings);

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

                finish();
                break;
        }
    }
}
