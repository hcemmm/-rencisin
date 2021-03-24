package com.example.bitirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void girisyap(View view) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();

    }

    public void kayitol(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        finish();
    }
}