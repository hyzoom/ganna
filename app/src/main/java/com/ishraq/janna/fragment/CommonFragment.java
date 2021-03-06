package com.ishraq.janna.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ahmed on 2/27/2016.
 */
public class CommonFragment extends Fragment {

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
    }

    //////////////////////////////////////////////////////////////////////////////////

    protected Retrofit getRestAdapter() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://nkamel-001-site1.gtempurl.com/pages/JsonData/")
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