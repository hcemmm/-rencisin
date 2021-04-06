package com.example.bitirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

public class UpdateProfilActivity extends AppCompatActivity {

    EditText adsoyaddz,yasdz,iletisimdz;
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DocumentReference documentReference ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String  kullanici_id= user.getUid();

        documentReference = db.collection("kullanici").document(kullanici_id);

        iletisimdz = findViewById(R.id.et_iletisim);
        adsoyaddz = findViewById(R.id.et_name_up);
        yasdz = findViewById(R.id.et_yas);
        button = findViewById(R.id.btn_up);


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
                            String url = task.getResult().getString("url");
                            String yasResult = task.getResult().getString("yas");


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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String kullanici_id= user.getUid();
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