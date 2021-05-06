package com.example.bitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class CafeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe);

        LinearLayout sehrisefa = (LinearLayout )findViewById(R.id.sehrisefababaet);
        sehrisefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(CafeActivity.this, Sehrisefa.class);
                startActivity(picture_intent );
            }
        });


        LinearLayout gloria = (LinearLayout )findViewById(R.id.gloriajeans);
        gloria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(CafeActivity.this, Gloriajeans.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout alcatraz = (LinearLayout )findViewById(R.id.alcatraz);
        alcatraz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(CafeActivity.this, Alcatrazw.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout coffe = (LinearLayout )findViewById(R.id.coffeemania);
        coffe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(CafeActivity.this, Coffemania.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout hanzade = (LinearLayout )findViewById(R.id.hanzade);
        hanzade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(CafeActivity.this, Hanzadekonagi.class);
                startActivity(picture_intent );
            }
        });

    }
}