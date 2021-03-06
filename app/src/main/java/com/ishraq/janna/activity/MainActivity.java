package com.ishraq.janna.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.ishraq.janna.JannaApp;
import com.ishraq.janna.R;
import com.ishraq.janna.fragment.NavigationDrawerFragment;
import com.ishraq.janna.fragment.main.AboutFragment;
import com.ishraq.janna.fragment.main.BookingFragment;
import com.ishraq.janna.fragment.main.EventDetailsFragment;
import com.ishraq.janna.fragment.main.HomeFragment;
import com.ishraq.janna.fragment.main.MainCommonFragment;
import com.ishraq.janna.fragment.main.QuestionFragment;
import com.ishraq.janna.model.Settings;
import com.ishraq.janna.service.SettingsService;
import com.ishraq.janna.webservice.CommonRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private SettingsService settingsService;

    private SwipeRefreshLayout swipeRefreshLayout;
    MenuItem play_music;
    MenuItem stop_music;
    VideoView videoHolder;
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
        videoHolder = (VideoView) findViewById(R.id.ganna_song1);
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

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 300);
        swipeRefreshLayout.setEnabled(false);
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
        swipeRefreshLayout.setRefreshing(false);

        final View errorView = LayoutInflater.from(JannaApp.getContext()).inflate(R.layout.fragment_connection_error, null, false);
        final ViewGroup wrapperView = (ViewGroup) findViewById(R.id.wrapper);

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

        TextView errorMessageTextView = (TextView) errorView.findViewById(R.id.errorMessageTextView);
        errorMessageTextView.setText(R.string.common_error_message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        play_music = menu.findItem(R.id.action_play);
        stop_music = menu.findItem(R.id.action_stop);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            share();
        } else if (id == R.id.action_call) {
            makeCall();
        } else if (id == R.id.action_play) {
            play_music.setVisible(false);
            stop_music.setVisible(true);
            play_music();
        } else if (id == R.id.action_stop) {
            play_music.setVisible(true);
            stop_music.setVisible(false);
            stop_music();
        }

        return super.onOptionsItemSelected(item);
    }

    public void play_music() {

//        setContentView(videoHolder);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.ganna_song);
        videoHolder.setVideoURI(video);
        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                videoHolder.start(); //jump to the next Activity
            }
        });
        videoHolder.start();
    }

    public void stop_music() {
        if (videoHolder.isPlaying()) {
            videoHolder.pause();
        }

    }

    public void shareText(String subject, String body) {
        Intent txtIntent = new Intent(Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        txtIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent, "Share"));
    }

    public void share() {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "تطبيق جنة للاندرويد حمله من هنا \n https://play.google.com/store/apps/details?id=com.ishraq.janna";

        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(
                sharingIntent, 0);
        for (final ResolveInfo app : activityList) {
            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(
                    android.content.Intent.ACTION_SEND);
            targetedShareIntent.setType("text/plain");
            targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "share");
            if (TextUtils.equals(packageName, "com.facebook.katana")) {
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "https://play.google.com/store/apps/details?id=com.ishraq.janna");
            } else {
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        shareBody);
            }
            targetedShareIntent.setPackage(packageName);
            targetedShareIntents.add(targetedShareIntent);
        }
        Intent chooserIntent = Intent.createChooser(
                targetedShareIntents.remove(0), "Share Idea");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                targetedShareIntents.toArray(new Parcelable[]{}));
        startActivity(chooserIntent);

    }

    public void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:01027287777"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getApplicationContext().startActivity(intent);
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

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Settings settings = settingsService.getSettings();

        switch (position) {
            case 0:
                addFragment(new AboutFragment(), true, null);
                break;
            case 1:
                addFragment(new BookingFragment(), true, null);
                break;
            case 2:
                if (settings.getLoggedInUser().isManager()) {
                    addFragment(new QuestionFragment(), true, null);
                } else {
                    settings.setLoggedInUser(null);

                    settingsService.updateSettings(settings);

                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);

                    finish();
                }
                break;

            case 3:
                settings = settingsService.getSettings();
                settings.setLoggedInUser(null);

                settingsService.updateSettings(settings);

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

                finish();
                break;
        }
    }
}
