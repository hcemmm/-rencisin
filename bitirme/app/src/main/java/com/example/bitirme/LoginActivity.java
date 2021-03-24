package com.example.bitirme;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mloginButtn;
    TextView mcreatebutton;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail=findViewById(R.id.kayit_kullaniciposta);
        mPassword=findViewById(R.id.kayit_kullanicisifre);
        mloginButtn=findViewById(R.id.Loginbutton);
        mcreatebutton=findViewById(R.id.kayitagit);
        fAuth=FirebaseAuth.getInstance();



        mloginButtn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                String email= mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Eposta Giriniz");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Şifrenizi Giriniz");
                    return;
                }

                if(password.length()<6){
                    mPassword.setError("Şifreniz Hata Uzun Olmalı");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Giriş oluşturuldu", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),AnasayfaActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Hata!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        mcreatebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

}