package com.ishraq.janna.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ishraq.janna.fragment.CommonFragment;
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
public class RegistrationFragment extends LoginCommonFragment implements View.OnClickListener {

    private SettingsService settingsService;
    private UserService userService;
    private UserWebService userWebService;

    private Button registerButton;
    private EditText nameEditText, addressEditText, mobileEditText, passwordEditText, rePasswordEditText;

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
        addressEditText = (EditText) view.findViewById(R.id.addressEditText);
        mobileEditText = (EditText) view.findViewById(R.id.mobileEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        rePasswordEditText = (EditText) view.findViewById(R.id.rePasswordEditText);

        radioDoctor = (RadioButton) view.findViewById(R.id.radioDoctor);
        radioPatient = (RadioButton) view.findViewById(R.id.radioPatient);

        registerButton = (Button) view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (passwordEditText.getText().toString().equals("")) {
                    rePasswordEditText.setEnabled(false);
                } else {
                    rePasswordEditText.setEnabled(true);
                }
            }
        });
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
        if (nameEditText.getText().toString() == null || nameEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mobileEditText.getText().toString() == null || mobileEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.mobile_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordEditText.getText().toString() == null || passwordEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.password_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rePasswordEditText.getText().toString() == null || rePasswordEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.re_password_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwordEditText.getText().toString().toString().equals(rePasswordEditText.getText().toString().toString())) {
            Toast.makeText(getActivity(), getString(R.string.repeating_not_right), Toast.LENGTH_SHORT).show();
            return false;
        }

        ////////////////////////////////////////
        if (addressEditText.getText().toString() == null || addressEditText.getText().toString().equals("")) {
            addressEditText.setText("");
        }

        return result;
    }

    private class RegisterUserRequest implements CommonRequest {

        @Override
        public void execute() {
            int type = 0;
            if (radioDoctor.isChecked()) {
                type = 1;
            }
            userWebService.registerUser(nameEditText.getText().toString(),
                    passwordEditText.getText().toString(), type,
                    mobileEditText.getText().toString(),
                    addressEditText.getText().toString()).enqueue(new RequestCallback<List<User.ExistUser>>(this) {
                @Override
                public void onResponse(Call<List<User.ExistUser>> call, Response<List<User.ExistUser>> response) {
                    // Save user
                    User.ExistUser result = response.body().get(0);

                    if (result.getUserExist() == 1) {
                        // Saved successfully
                        GetUserRequest request = new GetUserRequest(result.getId());
                        request.execute();
                    } else {
                        getLoginActivity().stopLoadingAnimator();
                        Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<User.ExistUser>> call, Throwable t) {
                    super.onFailure(call, t);
                    getLoginActivity().stopLoadingAnimator();
                    Toast.makeText(getActivity(), "An error occurred, Retry again. ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private class GetUserRequest implements CommonRequest {
        private Integer userId;

        public GetUserRequest(Integer userId) {
            this.userId = userId;
        }

        @Override
        public void execute() {
            userWebService.getUser(userId).enqueue(new RequestCallback<List<User>>(this) {
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
                    getActivity().finish();
                }
            });
        }
    }

}
