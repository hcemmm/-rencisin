package com.example.bitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class YurtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yurt);

        LinearLayout esrefbitlis = (LinearLayout )findViewById(R.id.erkekkyk);
        esrefbitlis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(YurtActivity.this, com.example.bitirme.Esrefbitliskyk.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout hazar = (LinearLayout )findViewById(R.id.hazar);
        hazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(YurtActivity.this, com.example.bitirme.Hazaryurt.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout battalgazi = (LinearLayout )findViewById(R.id.battalgazi);
        battalgazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(YurtActivity.this, com.example.bitirme.Battalgazikyk.class);
                startActivity(picture_intent );
            }
        });
    }
}
