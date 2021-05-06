package com.example.bitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AvmActivity extends AppCompatActivity {

    WebView malatyaavm_fotosu,dogaavm_fotosu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avm);


        LinearLayout malatyapark = (LinearLayout )findViewById(R.id.malatyapark);
        malatyapark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(AvmActivity.this, com.example.bitirme.Malatyaparkavm.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout dogacadde = (LinearLayout )findViewById(R.id.dogacadde);
        dogacadde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(AvmActivity.this, com.example.bitirme.Dogacaddeavm.class);
                startActivity(picture_intent );
            }
        });

    }

}