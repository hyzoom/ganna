package com.ishraq.janna.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.ishraq.janna.R;
import com.ishraq.janna.activity.LoginActivity;
import com.ishraq.janna.service.SettingsService;
import com.ishraq.janna.service.UserService;
import com.ishraq.janna.webservice.UserWebService;

/**
 * Created by root on 4/24/16.
 */
public class ForgetPassWordFragment extends LoginCommonFragment implements View.OnClickListener {
    EditText mobileEditText, emailEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((LoginActivity) getActivity()).stopLoadingAnimator();
        View view = inflater.inflate(R.layout.fragment_forget_pass, container, false);

        mobileEditText = (EditText) view.findViewById(R.id.mobileEditText);
        emailEditText = (EditText) view.findViewById(R.id.emailEditText);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
