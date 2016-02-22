package com.ishraq.janna.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.ishraq.janna.R;
import com.ishraq.janna.fragment.login.LoginFragment;

/**
 * Created by Ahmed on 2/18/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private ContentLoadingProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();

        addFragment(new LoginFragment(), false, null);
    }

    private void initializeViews() {
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.login_activity));
        setSupportActionBar(mToolbar);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);
//        swipeRefreshLayout.setColorSchemeResources(
//                R.color.refresh_progress_1,
//                R.color.refresh_progress_2,
//                R.color.refresh_progress_3);
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

}
