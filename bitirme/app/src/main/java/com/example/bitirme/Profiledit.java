package com.example.bitirme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profiledit extends BottomSheetDialogFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference reference ;
    CardView cv_gizlilik,cv_cik,cv_duzenle;
    FirebaseAuth mAuth;
    DatabaseReference df;
    FirebaseUser mCurrentUser;
    String url,name,currentid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.profiledit_item,null);


        cv_duzenle = view.findViewById(R.id.cv_duzenle);
        cv_cik = view.findViewById(R.id.cv_cikisyap);
        cv_gizlilik = view.findViewById(R.id.cv_gizlilik);
        mAuth = FirebaseAuth.getInstance();





        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentid= user.getUid();

        df = FirebaseDatabase.getInstance().getReference("Tüm Kullanıcılar");
        reference = db.collection("kullanici").document(currentid);
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            url = task.getResult().getString("url");

                        }else {

                        }
                    }
                });


        mCurrentUser = mAuth.getCurrentUser();


        cv_cik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        cv_gizlilik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),Hesapgizlilik.class));
            }
        });
        cv_duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),UpdateProfilActivity.class));
            }
        });

        return view;
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ÇIKIŞ")
                .setMessage("Çıkmak istediğinizden emin misiniz?")
                .setPositiveButton("Çıkış Yap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mAuth.signOut();
                        startActivity(new Intent(getActivity(),MainActivity.class));
                    }
                })
                .setNegativeButton("Kal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.create();
        builder.show();
    }
}
