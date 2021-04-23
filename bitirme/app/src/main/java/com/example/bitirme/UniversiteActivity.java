package com.example.bitirme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class UniversiteActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universite);

        LinearLayout inonu = (LinearLayout )findViewById(R.id.inonu);
        inonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(UniversiteActivity.this,Inonuuniversite.class);
                startActivity(picture_intent );
            }
        });

        LinearLayout turguozal = (LinearLayout )findViewById(R.id.turgutozal);
        turguozal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture_intent = new Intent(UniversiteActivity.this,Turgutozaluniversite.class);
                startActivity(picture_intent );
            }
        });

    }
}