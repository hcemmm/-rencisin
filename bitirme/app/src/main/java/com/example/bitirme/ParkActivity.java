package com.example.bitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ParkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        LinearLayout kernek = (LinearLayout )findViewById(R.id.kernekselale);
        kernek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(ParkActivity.this, Kernekselale.class);
                startActivity(picture_intent );
            }
        });


        LinearLayout turgut = (LinearLayout )findViewById(R.id.tozaltabiat);
        turgut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(ParkActivity.this, Turgutozaltabiat.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout beydagi = (LinearLayout )findViewById(R.id.beydagitabiat);
        beydagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(ParkActivity.this, Beydagitabiat.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout hurriyet = (LinearLayout )findViewById(R.id.hurriyetpark);
        hurriyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(ParkActivity.this, Hurriyetparki.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout levent = (LinearLayout )findViewById(R.id.leventvadi);
        levent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(ParkActivity.this, Leventvadisi.class);
                startActivity(picture_intent );
            }
        });
    }
}