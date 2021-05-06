package com.example.bitirme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView ogrencisinweb;
    TextView ogrencisininsta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ogrencisinweb = findViewById(R.id.git_ogrencisin);
        ogrencisininsta = findViewById(R.id.instagit);

        ogrencisininsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insta = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ogrencisin/"));
                startActivity(insta);

            }
        });
        ogrencisinweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ogrencisin.com/"));
                startActivity(webintent);
            }
        });
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