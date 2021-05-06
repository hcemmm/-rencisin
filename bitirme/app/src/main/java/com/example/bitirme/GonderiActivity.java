package com.example.bitirme;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GonderiActivity extends AppCompatActivity {

    ImageView g_foto;
    ImageButton g_byukle;
    ProgressBar progressBar;
    private Uri selectedUri;
    private  static final int PICK_FILE = 1;
    UploadTask uploadTask;
    EditText g_bilgi;
    Button g_bsec;
    String url,adsoyad,uid;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db1,db3;

    Gonderikullanicilar gonderikullanicilar;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonderi);

        gonderikullanicilar = new Gonderikullanicilar();

        g_foto = findViewById(R.id.gonderi_fotograf);
        g_bilgi = findViewById(R.id.gonderi_bilgi);
        g_byukle = findViewById(R.id.gonderi_ekle);
        g_bsec = findViewById(R.id.gonderi_sec);
        progressBar = findViewById(R.id.gonderi_progressbar);


        storageReference = FirebaseStorage.getInstance().getReference("Gonderiler");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        db1 = database.getReference("Resimler").child(currentuid);
        db3 = database.getReference("Tum Gonderiler");
        db3.keepSynced(true);


        g_bsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_FILE);

            }
        });
        g_byukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gonderipaylas();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE || resultCode == RESULT_OK ||

                data != null || data.getData() != null) {

            selectedUri = data.getData();
            Picasso.get().load(selectedUri).into(g_foto);
            g_foto.setVisibility(View.VISIBLE);
            type = "iv";

        }
            else {
                Toast.makeText(this, "Dosya Seçilmedi!", Toast.LENGTH_SHORT).show();
            }

        }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));

    }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("kullanici").document(currentuid);

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()) {
                            adsoyad = task.getResult().getString("ad");
                            url = task.getResult().getString("url");
                            uid = task.getResult().getString("uid");

                        } else {
                            Toast.makeText(GonderiActivity.this, "HATA(onStart)", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }


    private void gonderipaylas() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String kulla_id = user.getUid();

        final String g_aciklama = g_bilgi.getText().toString();

        Calendar callfordate = Calendar.getInstance();
        SimpleDateFormat currentdate = new
                SimpleDateFormat("dd-MMMM-yyyy");
        final  String savedate = currentdate.format(callfordate.getTime());

        Calendar callfortime = Calendar.getInstance();
        SimpleDateFormat currenttime = new
                SimpleDateFormat("HH:mm");
        final  String savetime = currenttime.format(callfortime.getTime());

        String time = savedate+":"+savetime;

        if(TextUtils.isEmpty(g_aciklama) || selectedUri != null){
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+ "."+getFileExt(selectedUri));
            uploadTask = reference.putFile(selectedUri);

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

                        if (type.equals("iv")){

                            gonderikullanicilar.setGkAciklama(g_aciklama);
                            gonderikullanicilar.setGkAdsoyad(adsoyad);
                            gonderikullanicilar.setGkPostUri(downloadUri.toString());
                            gonderikullanicilar.setGkTime(time);
                            gonderikullanicilar.setGkUid(kulla_id);
                            gonderikullanicilar.setGkUrl(url);
                            gonderikullanicilar.setGkType("iv");



                            String id = db1.push().getKey();
                            db1.child(id).setValue(gonderikullanicilar);

                            String id1 = db3.push().getKey();
                            db3.child(id1).setValue(gonderikullanicilar);

                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(GonderiActivity.this, "Gönderi Paylaşıldı.", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(GonderiActivity.this, "HATA(gonderiPaylas)", Toast.LENGTH_SHORT).show();
                        }


                    }

                }
            });

        }else {
            Toast.makeText(this, "Tüm Alanları Doldurun!", Toast.LENGTH_SHORT).show();
        }

    }
}
