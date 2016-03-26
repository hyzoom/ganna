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
public class LoginFragment extends LoginCommonFragment implements View.OnClickListener {

    private SettingsService settingsService;
    private UserService userService;

    private UserWebService userWebService;

    private Button loginButton, newUserButton;
    private EditText mobileEditText, passwordEditText;

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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button) view.findViewById(R.id.loginButton);
        newUserButton = (Button) view.findViewById(R.id.newUserButton);

        loginButton.setOnClickListener(this);
        newUserButton.setOnClickListener(this);

        mobileEditText = (EditText) view.findViewById(R.id.mobileEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);

        return view;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    private boolean validateLogin() {
        boolean result = true;

        if (mobileEditText.getText().toString().equals("") || mobileEditText.getText() == null) {
            Toast.makeText(getActivity(), getString(R.string.mobile_empty), Toast.LENGTH_SHORT).show();
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
                    getLoginActivity().startLoadingAnimator();
                    LoginUserRequest request = new LoginUserRequest();
                    request.execute();
                }
                break;

            case R.id.newUserButton:
                ((LoginActivity) getActivity()).addFragment(new RegistrationFragment(), true, null);
                break;
        }
    }

    /////////////////////////////////////////// Connection ////////////////////////////
    private class LoginUserRequest implements CommonRequest {

        @Override
        public void execute() {
            userWebService.loginUser(mobileEditText.getText().toString(),
                    passwordEditText.getText().toString()).enqueue(new RequestCallback<List<User.ExistUser>>(this) {
                @Override
                public void onResponse(Call<List<User.ExistUser>> call, Response<List<User.ExistUser>> response) {
                    User.ExistUser result = response.body().get(0);
                    if (result.getErrorMessage().trim().equals("Exist User")) {
                        // Exist => get user data
                        GetUserRequest request = new GetUserRequest(result.getId());
                        request.execute();
                    } else {
                        // Not exist
                        Toast.makeText(getLoginActivity(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<User.ExistUser>> call, Throwable t) {
                    getLoginActivity().stopLoadingAnimator();
                    Log.w("AhmedLog", t.getMessage() + "\n" + call.request().method() + "\n" + t.getLocalizedMessage());
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
