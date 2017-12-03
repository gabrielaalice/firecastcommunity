package com.example.gabriela.firecastcommunity.drawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Gabriela on 12/3/17.
 */

public class ShareAppActivity  extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        // context = itemView.getContext();
        intent.setAction(Intent.ACTION_SEND);
        String shareBody = "Ocorrência Firecast";
        String shareSub = "Ocorrência ocorrendo no endereço:  " +
                "\n"+ "Mais informações em: Firecast Comunidade";
        intent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
        intent.putExtra(Intent.EXTRA_TEXT,shareSub);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, "share using:"));
    }
}
