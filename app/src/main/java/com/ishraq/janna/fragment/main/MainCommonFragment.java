package com.ishraq.janna.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;

import com.ishraq.janna.activity.MainActivity;
import com.ishraq.janna.fragment.CommonFragment;
import com.ishraq.janna.webservice.CommonRequest;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Ahmed on 2/27/2016.
 */
public class MainCommonFragment extends CommonFragment {

    protected Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMainActivity().startLoadingAnimator();

        mToolbar = getMainActivity().getToolbar();
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    protected abstract class RequestCallback<T> implements Callback<T> {
        private CommonRequest request;

        public RequestCallback(CommonRequest request) {
            this.request = request;
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            ((MainActivity)getActivity()).showConnectionError(MainCommonFragment.this, request, t, getView());
        }
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////
    protected void hideToolbar() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    protected void showToolbar() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

}
