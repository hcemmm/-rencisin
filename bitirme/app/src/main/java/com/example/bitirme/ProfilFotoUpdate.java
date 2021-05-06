package com.example.bitirme;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfilFotoUpdate extends AppCompatActivity {

    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Uri resimuri;
    String  kullanici_id;
    ImageView new_foto;
    Button new_degis;

    private final static int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_foto_update);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        kullanici_id= user.getUid();


        storageReference= FirebaseStorage.getInstance().getReference("Profil Resmi");

        new_foto = findViewById(R.id.up_fotosec);
        new_degis = findViewById(R.id.up_butonsec);

        new_degis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImage();
            }
        });
    }

    public void fotosec(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {

            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
                    data != null || data.getData() != null) {
                resimuri = data.getData();

                Picasso.get().load(resimuri).into(new_foto);
            }
        }catch (Exception e){
            Toast.makeText(this, "Hata(catch)"+e, Toast.LENGTH_SHORT).show();
        }


    }
    private void updateImage() {
        final StorageReference reference = storageReference.child(System.currentTimeMillis()+ "."+getFileExt(resimuri));
        uploadTask = reference.putFile(resimuri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw  task.getException();
                }

                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();

                    final DocumentReference sDoc = db.collection("kullanici").document(kullanici_id);
                    db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(sDoc);


                            transaction.update(sDoc, "url",downloadUri.toString() );

                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProfilFotoUpdate.this, "Değiştirildi!", Toast.LENGTH_SHORT).show();


                            FirebaseDatabase database = FirebaseDatabase.getInstance();




                            Map<String,Object > profile = new HashMap<>();
                            profile.put("url",downloadUri.toString());
                            Map<String,Object > resimler = new HashMap<>();
                            resimler.put("gkUrl",downloadUri.toString());

                            DatabaseReference   kyorum = database.getReference("KYorumlar").child(kullanici_id);
                            Query querykyorum = kyorum.orderByChild("uid").equalTo(kullanici_id);
                            querykyorum.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            DatabaseReference   kresim = database.getReference("Resimler").child(kullanici_id);
                            Query querykresim = kresim.orderByChild("gkUid").equalTo(kullanici_id);
                            querykresim.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(resimler);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference kgonderi = database.getReference("Tum Gonderiler");
                            Query querykgonderi = kgonderi.orderByChild("gkUid").equalTo(kullanici_id);
                            querykgonderi.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(resimler);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            DatabaseReference db2 = database.getReference("Yorumlar").child("Turgut");
                            Query query2 = db2.orderByChild("uid").equalTo(kullanici_id);
                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference db3 = database.getReference("Yorumlar").child("Alcatraz");
                            Query query3 = db3.orderByChild("uid").equalTo(kullanici_id);
                            query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference  db4 = database.getReference("Yorumlar").child("alidayi");
                            Query query4 = db4.orderByChild("uid").equalTo(kullanici_id);
                            query4.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db5 = database.getReference("Yorumlar").child("Battalgazi");
                            Query query5 = db5.orderByChild("uid").equalTo(kullanici_id);
                            query5.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference  db6 = database.getReference("Yorumlar").child("BeydagiTabiat");
                            Query query6 = db6.orderByChild("uid").equalTo(kullanici_id);
                            query6.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference  db7 = database.getReference("Yorumlar").child("Coffemania");
                            Query query7 = db7.orderByChild("uid").equalTo(kullanici_id);
                            query7.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            DatabaseReference   db10 = database.getReference("Yorumlar").child("Copycenter");
                            Query query10 = db10.orderByChild("uid").equalTo(kullanici_id);
                            query10.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference  db11 = database.getReference("Yorumlar").child("dicle");
                            Query query11 = db11.orderByChild("uid").equalTo(kullanici_id);
                            query11.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference  db12 = database.getReference("Yorumlar").child("dogacadde");
                            Query query12 = db12.orderByChild("uid").equalTo(kullanici_id);
                            query12.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference  db13 = database.getReference("Yorumlar").child("DR");
                            Query query13 = db13.orderByChild("uid").equalTo(kullanici_id);
                            query13.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference  db14 = database.getReference("Yorumlar").child("EsrefBKyk");
                            Query query14 = db14.orderByChild("uid").equalTo(kullanici_id);
                            query14.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db15 = database.getReference("Yorumlar").child("eymen");
                            Query query15 = db15.orderByChild("uid").equalTo(kullanici_id);
                            query15.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db16 = database.getReference("Yorumlar").child("Gloria");
                            Query query16 = db16.orderByChild("uid").equalTo(kullanici_id);
                            query16.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference db17= database.getReference("Yorumlar").child("InonuYorumi");
                            Query query17 = db17.orderByChild("uid").equalTo(kullanici_id);
                            query17.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference db18 = database.getReference("Yorumlar").child("kahtali");
                            Query query18 = db18.orderByChild("uid").equalTo(kullanici_id);
                            query18.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference  db19 = database.getReference("Yorumlar").child("KernekS");
                            Query query19 = db19.orderByChild("uid").equalTo(kullanici_id);
                            query19.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db20 = database.getReference("Yorumlar").child("Hacibaba");
                            Query query20 = db20.orderByChild("uid").equalTo(kullanici_id);
                            query20.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            DatabaseReference  db21 = database.getReference("Yorumlar").child("Hazar");
                            Query query21 = db21.orderByChild("uid").equalTo(kullanici_id);
                            query21.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db23 = database.getReference("Yorumlar").child("Hurriyet");
                            Query query23 = db23.orderByChild("uid").equalTo(kullanici_id);
                            query23.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db24 = database.getReference("Yorumlar").child("Leventvadi");
                            Query query24 = db24.orderByChild("uid").equalTo(kullanici_id);
                            query24.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db25 = database.getReference("Yorumlar").child("sehriisefa");
                            Query query25 = db25.orderByChild("uid").equalTo(kullanici_id);
                            query25.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db26 = database.getReference("Yorumlar").child("MalatyaPark");
                            Query query26 = db26.orderByChild("uid").equalTo(kullanici_id);
                            query26.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db27 = database.getReference("Yorumlar").child("TurgutOzalTabiatParki");
                            Query query27 = db27.orderByChild("uid").equalTo(kullanici_id);
                            query27.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference   db28 = database.getReference("Yorumlar").child("yaprakd");
                            Query query28 = db28.orderByChild("uid").equalTo(kullanici_id);
                            query28.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            DatabaseReference   db29 = database.getReference("Yorumlar").child("Hanzade");
                            Query query29 = db29.orderByChild("uid").equalTo(kullanici_id);
                            query29.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(ProfilFotoUpdate.this, "Başarılı", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });





                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfilFotoUpdate.this, "HATA(onFailure)", Toast.LENGTH_SHORT).show();
                                }
                            });



                }

            }
        });


    }

}