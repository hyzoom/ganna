package com.ishraq.janna.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ishraq.janna.R;
import com.ishraq.janna.activity.LoginActivity;
import com.ishraq.janna.activity.MainActivity;
import com.ishraq.janna.model.Settings;
import com.ishraq.janna.model.User;
import com.ishraq.janna.service.SettingsService;
import com.ishraq.janna.service.UserService;
import com.ishraq.janna.webservice.CommonRequest;
import com.ishraq.janna.webservice.UserWebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ahmed on 2/18/2016.
 */
public class RegistrationFragment extends CommonFragment implements View.OnClickListener{

    private SettingsService settingsService;
    private UserService userService;
    private UserWebService userWebService;

    private Button registerButton;
    private EditText nameEditText, firstNameEditText, secondNameEditText, addressEditText,
            phoneEditText, mobileEditText, emailEditText, passwordEditText, rePasswordEditText;

    private RadioButton radioDoctor, radioPatient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsService = new SettingsService(getActivity());
        userService = new UserService(getActivity());
        userWebService = getWebService(UserWebService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((LoginActivity) getActivity()).stopLoadingAnimator();
        View view = inflater.inflate(R.layout.fragment_regestration, container, false);

        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        firstNameEditText = (EditText) view.findViewById(R.id.firstNameEditText);
        secondNameEditText = (EditText) view.findViewById(R.id.secondNameEditText);
        addressEditText = (EditText) view.findViewById(R.id.addressEditText);
        phoneEditText = (EditText) view.findViewById(R.id.phoneEditText);
        mobileEditText = (EditText) view.findViewById(R.id.mobileEditText);
        emailEditText = (EditText) view.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        rePasswordEditText = (EditText) view.findViewById(R.id.rePasswordEditText);

        radioDoctor = (RadioButton) view.findViewById(R.id.radioDoctor);
        radioPatient = (RadioButton) view.findViewById(R.id.radioPatient);

        registerButton = (Button) view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerButton) {
            if (validateUser()) {
                getLoginActivity().startLoadingAnimator();
                RegisterUserRequest request = new RegisterUserRequest();
                request.execute();
            }
        }
    }

    private boolean validateUser() {
        boolean result = true;
        if (nameEditText.getText() == null || nameEditText.getText().equals("")) {
            Toast.makeText(getActivity(), "name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordEditText.getText() == null || passwordEditText.getText().equals("")
                || rePasswordEditText.getText() == null || rePasswordEditText.getText().equals("")) {
            Toast.makeText(getActivity(), "pass", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!passwordEditText.getText().toString().equals(passwordEditText.getText().toString())){
                Toast.makeText(getActivity(), "pass not", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (mobileEditText.getText() == null || mobileEditText.getText().equals("")) {
            Toast.makeText(getActivity(), "mobile", Toast.LENGTH_SHORT).show();
            return false;
        }


        ////////////////////////////////////////
        if (firstNameEditText.getText() == null || firstNameEditText.getText().equals("")) {
            firstNameEditText.setText("");
        }
        if (secondNameEditText.getText() == null || secondNameEditText.getText().equals("")) {
            secondNameEditText.setText("");
        }
        if (addressEditText.getText() == null || addressEditText.getText().equals("")) {
            addressEditText.setText("");
        }
        if (phoneEditText.getText() == null || phoneEditText.getText().equals("")) {
            phoneEditText.setText("");
        }
        if (emailEditText.getText() == null || emailEditText.getText().equals("")) {
            emailEditText.setText("");
        }

        return result;
    }

    private class RegisterUserRequest implements CommonRequest {

        @Override
        public void execute() {
            int type = 1;
            if (radioDoctor.isChecked()) {
                type = 2;
            }
            userWebService.registerUser(nameEditText.getText().toString(),
                    passwordEditText.getText().toString(), type,
                    mobileEditText.getText().toString(),
                    emailEditText.getText().toString()).enqueue(new RequestCallback<List<User>>(this) {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    // Save user
                    User usr = response.body().get(0);
                    userService.saveUser(usr);

                    // Set user login
                    Settings settings = settingsService.getSettings();
                    settings.setLoggedInUser(usr);
                    settingsService.updateSettings(settings);

                    // Go to main activity
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    super.onFailure(call, t);
                    getLoginActivity().stopLoadingAnimator();
                    Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
