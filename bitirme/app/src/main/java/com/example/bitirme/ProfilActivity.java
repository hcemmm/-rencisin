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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {

    EditText adsoyad,yas,iletisim;
    Button button;
    ImageView ppresim;
    ProgressBar progressbar;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    Tumkullanicilar kullanici;
    String kullanici_id;
    private static final int PICK_IMAGE=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        kullanici=new Tumkullanicilar();
        ppresim=findViewById(R.id.ppfoto);
        adsoyad=findViewById(R.id.profil_kadi);
        yas=findViewById(R.id.profil_yas);
        iletisim=findViewById(R.id.profil_iletisim);
        button=findViewById(R.id.profil_buton);
        progressbar=findViewById(R.id.profil_progress);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        kullanici_id =  user.getUid();


        documentReference = db.collection("kullanici").document(kullanici_id);
        storageReference= FirebaseStorage.getInstance().getReference("Profil Resmi");
        databaseReference=database.getReference("Tüm Kullanıcılar");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });

        ppresim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() !=null){
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(ppresim);
            }
        }
        catch (Exception e){
            Toast.makeText(this, "HATA!"+e, Toast.LENGTH_SHORT).show();
        }

    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void uploadData() {

        String name = adsoyad.getText().toString();
        String age = yas.getText().toString();
        String context = iletisim.getText().toString();

        if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(age) || !TextUtils.isEmpty(context)) {

            progressbar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();

                        Map<String,String> profile = new HashMap<>();
                        profile.put("ad",name);
                        profile.put("yas",age);
                        profile.put("iletisim",context);
                        profile.put("url",downloadUri.toString());
                        profile.put("uid",kullanici_id);
                        profile.put("privacy","public");

                        kullanici.setAdsoyad(name);
                        kullanici.setIletisim(context);
                        kullanici.setYas(age);
                        kullanici.setUid(kullanici_id);
                        kullanici.setUrl(downloadUri.toString());

                        databaseReference.child(kullanici_id).setValue(kullanici);

                        documentReference.set(profile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        progressbar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(ProfilActivity.this, "Profil Oluşturuldu", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }



                }
            });

        }
        else {
            Toast.makeText(this, "Tüm Alanları Doldurun!", Toast.LENGTH_SHORT).show();
        }

    }
}