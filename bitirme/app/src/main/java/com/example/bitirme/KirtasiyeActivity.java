package com.example.bitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class KirtasiyeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirtasiye);

        LinearLayout dr = (LinearLayout )findViewById(R.id.dr);
        dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(KirtasiyeActivity.this, com.example.bitirme.Dr.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout copycenter = (LinearLayout )findViewById(R.id.copycenter);
        copycenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(KirtasiyeActivity.this, com.example.bitirme.Copycenter.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout dicle = (LinearLayout )findViewById(R.id.diclekirtasiye);
        dicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(KirtasiyeActivity.this, com.example.bitirme.Diclekirtasiye.class);
                startActivity(picture_intent );
            }
        });
    }
}