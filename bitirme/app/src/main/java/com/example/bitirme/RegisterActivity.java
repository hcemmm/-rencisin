package com.example.bitirme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    EditText madsoyad,mEmail,msifre,mtelefon;
    Button mkayitol;
    TextView mgirisyap;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        madsoyad=findViewById(R.id.kayit_kullaniciadi);
        mEmail=findViewById(R.id.kayit_kullaniciposta);
        msifre=findViewById(R.id.kayit_kullanicisifre);
        mtelefon=findViewById(R.id.kayit_kullaniciiletisim);
        mkayitol=findViewById(R.id.kayitbuton);
        mgirisyap=findViewById(R.id.createtext);
        fAuth=FirebaseAuth.getInstance();



        mkayitol.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email= mEmail.getText().toString().trim();
                String password = msifre.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Eposta Giriniz");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    msifre.setError("Şifrenizi Giriniz");
                    return;
                }

                if(password.length()<6){
                    msifre.setError("Şifreniz Hata Uzun Olmalı");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Kullanıcı oluşturuldu", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Hata!" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mgirisyap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));

            }
        });

    }

}