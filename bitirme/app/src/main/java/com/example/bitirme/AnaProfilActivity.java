package com.example.bitirme;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import javax.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;

import java.util.Objects;


public class AnaProfilActivity extends Fragment implements View.OnClickListener {

    TextView adsoyad,yas,iletisim;
    ImageButton imageButtonEdit,exitbutton;
    ImageView ppresim;
    String url;
    DocumentReference reference;
    FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_ana_profil, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String kullanici_id = user.getUid();

        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("kullanici").document(kullanici_id);

        ppresim = getActivity().findViewById(R.id.ppfoto);
        adsoyad = getActivity().findViewById(R.id.profil_adi);
        yas = getActivity().findViewById(R.id.profil_yasi);
        iletisim = getActivity().findViewById(R.id.profil_iletsim);

        exitbutton = getActivity().findViewById(R.id.ib_cikis);
        imageButtonEdit = getActivity().findViewById(R.id.ib_editmenu);

        ppresim.setOnClickListener(this);
        imageButtonEdit.setOnClickListener(this);
        exitbutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ib_editmenu:
                Intent intent = new Intent(getActivity(),UpdateProfilActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_cikis:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(getActivity(),MainActivity.class);
                startActivity(intent2);
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {

                            String adsoyadResult = task.getResult().getString("ad");
                            String yasResult = task.getResult().getString("yas");
                            String iletisimResult = task.getResult().getString("iletisim");
                            url = task.getResult().getString("url");

                            Picasso.get().load(url).into(ppresim);
                            adsoyad.setText(adsoyadResult);
                            yas.setText(yasResult);
                            iletisim.setText(iletisimResult);


                        }else {
                            Intent intent = new Intent(getActivity(),ProfilActivity.class);
                            startActivity(intent);
                        }
                    }
                });

    }
}