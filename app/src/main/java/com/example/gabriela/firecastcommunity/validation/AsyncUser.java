package com.example.gabriela.firecastcommunity.validation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriela.firecastcommunity.MainActivity;
import com.example.gabriela.firecastcommunity.R;
import com.example.gabriela.firecastcommunity.domain.LoginValidation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Gabriela on 10/11/17.
 */

public class AsyncUser extends AsyncTask<String, String, String> {


    private LoginValidation loginValidation;

    private Activity activity;

    public AsyncUser(LoginValidation loginValidation) {
        this.loginValidation = loginValidation;
        this.activity = loginValidation.getActivity();
    }

    @Override
    protected String doInBackground(String... url) {
        StringBuilder resultado = new StringBuilder("");
        try {
            String path = url[0];
            path += "?email=" + loginValidation.getLogin() + "&senha=" + loginValidation.getSenha();

            URL urlNet = new URL(path);
            HttpURLConnection con = (HttpURLConnection) urlNet.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.connect();

            InputStream inp = con.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inp));

            String linha = "";
            while ((linha = bufferedReader.readLine()) != null) {
                resultado.append(linha);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado.toString();
    }

    @Override
    protected void onPostExecute(String resultado) {
        if (Boolean.valueOf(resultado)) {
            SharedPreferences.Editor editor = activity.getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
            editor.putString("login", loginValidation.getLogin());
            editor.putString("senha", loginValidation.getSenha());
            editor.commit();

            Intent i = new Intent(activity, MainActivity.class);
            activity.startActivity(i);
            activity.finish();
        } else {
          showMsgToast(activity, "Login/Senha inválidos!");
        }
    }
    public static void showMsgToast(Activity activity, String txt) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View lytToast = inflater.inflate(R.layout.toast_template, (ViewGroup) activity.findViewById(R.id.lytToast));

        TextView txtToast = (TextView) lytToast.findViewById(R.id.txtToast);
        txtToast.setText(txt);

        Toast toast = new Toast(activity);
        toast.setView(lytToast);
        toast.show();
    }

}
