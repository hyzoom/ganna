package com.ishraq.janna.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ishraq.janna.R;
import com.ishraq.janna.activity.LoginActivity;

/**
 * Created by Ahmed on 2/18/2016.
 */
public class LoginFragment extends CommonFragment implements View.OnClickListener {

    private Button loginButton, newUserButton;
    private EditText nameEditText, passwordEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((LoginActivity) getActivity()).stopLoadingAnimator();
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button) view.findViewById(R.id.loginButton);
        newUserButton = (Button) view.findViewById(R.id.newUserButton);

        loginButton.setOnClickListener(this);
        newUserButton.setOnClickListener(this);

        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);

        return view;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    private boolean validateLogin() {
        boolean result = true;

        if (nameEditText.getText().toString().equals("") || nameEditText.getText() == null) {
            Toast.makeText(getActivity(), getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
            result = false;
            return result;
        }
        if (passwordEditText.getText().toString().equals("") || passwordEditText.getText() == null) {
            Toast.makeText(getActivity(), getString(R.string.password_empty), Toast.LENGTH_SHORT).show();
            result = false;
            return result;
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                if (validateLogin()) {

                }
                break;

            case R.id.newUserButton:
                ((LoginActivity) getActivity()).addFragment(new RegistrationFragment(), true, null);
                break;
        }
    }

    /////////////////////////////////////////// Connection ////////////////////////////
}
