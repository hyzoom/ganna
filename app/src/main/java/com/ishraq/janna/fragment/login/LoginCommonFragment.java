package com.ishraq.janna.fragment.login;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ishraq.janna.activity.LoginActivity;
import com.ishraq.janna.fragment.CommonFragment;
import com.ishraq.janna.webservice.CommonRequest;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Ahmed on 2/27/2016.
 */
public class LoginCommonFragment extends CommonFragment {
    protected Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar = getLoginActivity().getToolbar();
    }

    protected LoginActivity getLoginActivity() {
        return (LoginActivity) getActivity();
    }


    protected abstract class RequestCallback<T> implements Callback<T> {
        private CommonRequest request;

        public RequestCallback(CommonRequest request) {
            this.request = request;
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
//            ((MainActivity)getActivity()).showConnectionError(CommonFragment.this, request, t, getView());
        }
    }

}
