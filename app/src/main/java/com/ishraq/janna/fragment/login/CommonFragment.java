package com.ishraq.janna.fragment.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;

import com.ishraq.janna.activity.LoginActivity;
import com.ishraq.janna.webservice.CommonRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ahmed on 2/20/2016.
 */
public class CommonFragment extends Fragment {

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected Toolbar mToolbar;

    private static List<Object> webServiceList;
    private static Retrofit retrofit;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar = getLoginActivity().getToolbar();
    }

    //////////////////////////////////////////////////////////////////////////////////

    protected LoginActivity getLoginActivity() {
        return (LoginActivity) getActivity();
    }

    //////////////////////////////////////////////////////////////////////////////////

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


    protected Retrofit getRestAdapter() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://ganah.zagel1.com/pages/JsonData/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }


    public <T> T getWebService(Class<T> clazz) {
        if (webServiceList == null) {
            webServiceList = new ArrayList<>();
        }

        for (Object webService : webServiceList) {
            if (webService.getClass().equals(clazz)) {
                return (T) webService;
            }
        }

        T webService = getRestAdapter().create(clazz);
        webServiceList.add(webService);
        return webService;
    }

}
