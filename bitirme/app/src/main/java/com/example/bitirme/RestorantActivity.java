package com.example.bitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class RestorantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restorant);

        LinearLayout hacibabaet = (LinearLayout )findViewById(R.id.hacibabaet);
        hacibabaet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(RestorantActivity.this, com.example.bitirme.Hacibabaetsinan.class);
                startActivity(picture_intent );
            }
        });


        LinearLayout yaprakdoner = (LinearLayout )findViewById(R.id.yaprakdoner);
        yaprakdoner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(RestorantActivity.this, com.example.bitirme.Yaprakdoner.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout eymenlahmacun = (LinearLayout )findViewById(R.id.eymen);
        eymenlahmacun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(RestorantActivity.this, com.example.bitirme.Eymenlahmacun.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout kahtali = (LinearLayout )findViewById(R.id.kahtali);
        kahtali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(RestorantActivity.this, com.example.bitirme.KahtaliLahmacun.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout alidayi = (LinearLayout )findViewById(R.id.alidayi);
        alidayi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(RestorantActivity.this, com.example.bitirme.Alidayibalik.class);
                startActivity(picture_intent );
            }
        });
    }
}