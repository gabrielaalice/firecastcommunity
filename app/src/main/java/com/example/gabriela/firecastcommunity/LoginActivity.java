package com.example.gabriela.firecastcommunity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriela.firecastcommunity.domain.LoginValidation;
import com.example.gabriela.firecastcommunity.drawer.RegisterUserActivity;
import com.example.gabriela.firecastcommunity.helper.LoginService;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Timer;
import java.util.TimerTask;


public class LoginActivity extends AppCompatActivity {

    private EditText edtLogin;
    private EditText edtSenha;

    private TextView txtSkip;
    private TextView txtRegisterUser;
    private Button btnLogar;
    private SharedPreferences preferences;
    private LoginService loginService;
    private LoginButton loginButton;
    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        loginService = new LoginService();

        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        String login = preferences.getString("login", null);
        String senha = preferences.getString("senha", null);
        if (login != null && senha != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        edtLogin = (EditText) findViewById(R.id.edt_login);
        edtSenha = (EditText) findViewById(R.id.edt_senha);
        txtRegisterUser = (TextView) findViewById(R.id.text_register);
        txtRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterUserActivity.class);
                startActivity(i);
                finish();
            }
        });
        txtSkip = (TextView) findViewById(R.id.text_skip);
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnLogar = (Button) findViewById(R.id.btn_login);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = edtLogin.getText().toString();
                String senha = edtSenha.getText().toString();

                LoginValidation validation = new LoginValidation();
                validation.setActivity(LoginActivity.this);
                validation.setEdtLogin(edtLogin);
                validation.setEdtSenha(edtSenha);
                validation.setLogin(login);
                validation.setSenha(senha);

                loginService.validarCamposLogin(validation);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        loginButton = (LoginButton) findViewById(R.id.facebook_login);
        loginButton.setReadPermissions("email", "public_profile");

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        LoginButton loginButton = (LoginButton)findViewById(R.id.facebook_login);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                //  logginValidated();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        nextActivity(profile);
                    }
                }, 1000);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);

    }
    private void nextActivity(Profile profile) {
        if (profile != null) {
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            main.putExtra("name", profile.getFirstName());
            main.putExtra("surname", profile.getLastName());
            main.putExtra("imageUrl", profile.getProfilePictureUri(200, 200).toString());
            startActivity(main);
            finish();
        }
    }
    private void logginValidated(){

        Intent main = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(main);
        finish();
    }
}
