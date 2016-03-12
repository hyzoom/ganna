package com.ishraq.janna.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishraq.janna.R;

/**
 * Created by Ahmed on 3/11/2016.
 */
public class QuestionFragment extends MainCommonFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getMainActivity().stopLoadingAnimator();
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        return view;
    }
}
