package com.example.bitirme;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    EditText mEmail,msifre;
    Button mgirisbtn;
    TextView mkayitbtn,sifreunuttum;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sifreunuttum=findViewById(R.id.kayit_sunut);
        mEmail=findViewById(R.id.kayit_kullaniciposta);
        msifre=findViewById(R.id.kayit_kullanicisifre);
        mgirisbtn=findViewById(R.id.Loginbutton);
        mkayitbtn=findViewById(R.id.kayitagit);
        fAuth=FirebaseAuth.getInstance();



        mgirisbtn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
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

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Başarılı Giriş", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),AnasayfaActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Hata!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        sifreunuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Şifre Sıfırla")
                        .setMessage("Şifrenizi sıfırlamak istiyor musunuz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(LoginActivity.this, "Mail adresinize gönderildi.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "HATA!"+e, Toast.LENGTH_SHORT).show();
                                    }
                                });




                            }
                        })
                        .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create();
                builder.show();

            }
        });


        mkayitbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

}