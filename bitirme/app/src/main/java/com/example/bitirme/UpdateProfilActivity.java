package com.example.bitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

public class UpdateProfilActivity extends AppCompatActivity {

    EditText adsoyaddz,yasdz,iletisimdz;
    Button button,ayarlar;
    DocumentReference documentReference ;
    ImageView new_foto;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String  kullanici_id;

    private final static int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        kullanici_id= user.getUid();

        documentReference = db.collection("kullanici").document(kullanici_id);

        new_foto = findViewById(R.id.nw_updatefoto);
        iletisimdz = findViewById(R.id.et_iletisim);
        adsoyaddz = findViewById(R.id.et_name_up);
        yasdz = findViewById(R.id.et_yas);
        button = findViewById(R.id.btn_up);
        ayarlar=findViewById(R.id.gizlilik_ayar);

        ayarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Hesapgizlilik.class));
                finish();
            }
        });

        new_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfilFotoUpdate.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateProfile();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            String adsoyadResult = task.getResult().getString("ad");
                            String iletisimResult = task.getResult().getString("iletisim");
                            String yasResult = task.getResult().getString("yas");
                            String urlresult = task.getResult().getString("url");

                            Picasso.get().load(urlresult).into(new_foto);
                            adsoyaddz.setText(adsoyadResult);
                            iletisimdz.setText(iletisimResult);
                            yasdz.setText(yasResult);
                        }else {
                            Toast.makeText(UpdateProfilActivity.this, "Profil Bulunamadı ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void updateProfile() {

        final String name = adsoyaddz.getText().toString();
        final String contex = iletisimdz.getText().toString();
        final String age = yasdz.getText().toString();

        final  DocumentReference sDoc = db.collection("kullanici").document(kullanici_id);
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sDoc);


                transaction.update(sDoc, "ad",name );
                transaction.update(sDoc,"yas",age);
                transaction.update(sDoc,"iletisim",contex);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateProfilActivity.this, "Düzenlendi", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfilActivity.this, "HATA", Toast.LENGTH_SHORT).show();
                    }
                });




    }

}