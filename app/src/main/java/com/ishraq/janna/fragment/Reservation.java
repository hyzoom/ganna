package com.ishraq.janna.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ishraq.janna.R;
import com.ishraq.janna.service.CommonService;

/**
 * Created by hp on 20/02/2016.
 */
public class Reservation extends Fragment {

    private CommonService commonService = new CommonService(getActivity());


    private ListView listView;


    public Reservation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.about_us, container, false);


        return view;
    }

}