package com.example.gabriela.firecastcommunity.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.domain.User;
import com.example.gabriela.firecastcommunity.helper.Constantes;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.Header;


public class RegisterUserActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtEmail, edtPassword, edtConfirmPassword, edtDistance;

    private TextInputLayout lytTxtName, lytTxtEmail, lytTxtPassword, lytTxtConfirmPassword;

    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        final Gson gson = new Gson();

        edtName = (EditText) findViewById(R.id.edt_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
        lytTxtName = (TextInputLayout) findViewById(R.id.lyt_name);
        lytTxtEmail = (TextInputLayout) findViewById(R.id.lyt_email);
        lytTxtPassword = (TextInputLayout) findViewById(R.id.lyt_password);
        lytTxtConfirmPassword = (TextInputLayout) findViewById(R.id.lyt_confirm_password);
        edtDistance = (EditText) findViewById(R.id.edt_max_distance);

        btnRegister = (Button) findViewById(R.id.btn_save);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkNameValid()) {
                    return;
                }
                String json = gson.toJson(dataUser());

                try {
                    StringEntity stringEntity = new StringEntity(json);
                    new AsyncHttpClient().post(null, Constantes.URL_WS_BASE + "user/add", stringEntity, "application/json", new JsonHttpResponseHandler() {

                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("response", response.toString());
                            gson.fromJson(response.toString(), User.class);
                        }
                    });
                    Intent intent = new Intent();
                    intent.setClass(RegisterUserActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean checkNameValid() {
        if (edtName.getText().toString().trim().isEmpty()) {
            lytTxtName.setError("Campo obrigatório!");
            return false;
        }
        if (edtEmail.getText().toString().trim().isEmpty()) {
            lytTxtEmail.setError("Campo obrigatório!");
            return false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()){
            lytTxtEmail.setError("e-mail inválido");
            return false;
        }
        if (edtPassword.getText().toString().trim().isEmpty()) {
            lytTxtPassword.setError("Campo obrigatório!");
            return false;
        }
        if (edtConfirmPassword.getText().toString().trim().isEmpty()) {
            lytTxtConfirmPassword.setError("Campo obrigatório!");
            return false;
        }
        return true;
    }

    private User dataUser() {
        User user = new User();

        user.setName(edtName.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setPassword(edtPassword.getText().toString());
        user.setConfirmPassword(edtConfirmPassword.getText().toString());

        return user;
    }

}
