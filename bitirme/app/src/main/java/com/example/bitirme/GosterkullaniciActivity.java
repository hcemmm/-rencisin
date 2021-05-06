package com.example.bitirme;


import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
 
public class GosterkullaniciActivity extends AppCompatActivity {

    TextView gosteradi,gosteryasi,gosteriletisim;
    ImageView gosterpp;
    ImageFilterView deneme;
    String gosteriletisim_result,gosteryas_result,gosteradi_result;
    String p,url,userid;
    ImageButton g_profilyorum,g_profilgonderi;
    RecyclerView recyclerView_gostergonderi,recyclerView_gosteryorum;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference g_reference,databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firestore;
    DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gosterkullanici);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String kullanici_id = user.getUid();

        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("kullanici").document(kullanici_id);


        Bundle extras = getIntent().getExtras();
        if (extras != null){

            userid = extras.getString("guid");
        }

        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("kullanici").document(userid);
        g_reference = database.getReference("Resimler").child(userid);
        databaseReference =database.getReference("KYorumlar").child(userid);

        gosteradi = findViewById(R.id.goster_kadi);
        gosteryasi = findViewById(R.id.goster_kyasi);
        gosteriletisim = findViewById(R.id.goster_kiletisim);
        gosterpp = findViewById(R.id.goster_kpp);
        g_profilgonderi = findViewById(R.id.goster_post);
        g_profilyorum = findViewById(R.id.goster_yorumlar);

        deneme = findViewById(R.id.goster_kiltili);

        recyclerView_gosteryorum = findViewById(R.id.goster_kyorum);
        recyclerView_gosteryorum.setHasFixedSize(true);
        recyclerView_gosteryorum.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_gostergonderi = findViewById(R.id.goster_gonderiler);
        recyclerView_gostergonderi.setHasFixedSize(true);
        recyclerView_gostergonderi.setLayoutManager(new LinearLayoutManager(this));


        gosteriletisim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linkify.addLinks(gosteriletisim, Linkify.WEB_URLS);
            }
        });
        g_profilgonderi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_gostergonderi.setVisibility(View.VISIBLE);
                recyclerView_gosteryorum.setVisibility(View.GONE);
            }
        });
        g_profilyorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_gostergonderi.setVisibility(View.GONE);
                recyclerView_gosteryorum.setVisibility(View.VISIBLE);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            gosteradi_result = task.getResult().getString("ad");
                            gosteryas_result = task.getResult().getString("yas");
                            gosteriletisim_result = task.getResult().getString("iletisim");
                            url = task.getResult().getString("url");
                            p = task.getResult().getString("privacy");

                            gosteradi.setText(gosteradi_result);
                            Picasso.get().load(url).into(gosterpp);

                            if (p.equals("public")){
                                gosteriletisim.setText(gosteriletisim_result);
                                gosteryasi.setText(gosteryas_result);


                                FirebaseRecyclerOptions<Gonderikullanicilar> options2 =
                                        new FirebaseRecyclerOptions.Builder<Gonderikullanicilar>()
                                                .setQuery(g_reference,Gonderikullanicilar.class)
                                                .build();

                                FirebaseRecyclerAdapter<Gonderikullanicilar,Gonderisahip> firebaseRecyclerAdapter5 =
                                        new FirebaseRecyclerAdapter<Gonderikullanicilar, Gonderisahip>(options2) {
                                            @Override
                                            protected void onBindViewHolder(@NonNull Gonderisahip holder, int position, @NonNull final Gonderikullanicilar model) {

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                final String currentUserid = user.getUid();

                                                final  String postkey = getRef(position).getKey();
                                                holder.SetPost(getApplicationContext(),model.getGkAdsoyad(),model.getGkUrl(),model.getGkPostUri(),model.getGkTime()
                                                        ,model.getGkUid(),model.getGkType(),model.getGkAciklama());


                                            }

                                            @NonNull
                                            @Override
                                            public Gonderisahip onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                                View view = LayoutInflater.from(parent.getContext())
                                                        .inflate(R.layout.activity_gonderisahip,parent,false);

                                                return new Gonderisahip(view);

                                            }
                                        };
                                firebaseRecyclerAdapter5.startListening();

                                recyclerView_gostergonderi.setAdapter(firebaseRecyclerAdapter5);



                                FirebaseRecyclerOptions<Yorumkullanicilar> options =
                                        new FirebaseRecyclerOptions.Builder<Yorumkullanicilar>()
                                                .setQuery(databaseReference, Yorumkullanicilar.class)
                                                .build();


                                FirebaseRecyclerAdapter<Yorumkullanicilar, Yorumsahip> firebaseRecyclerAdapter =
                                        new FirebaseRecyclerAdapter<Yorumkullanicilar, Yorumsahip>(options) {
                                            @Override
                                            protected void onBindViewHolder(@NonNull Yorumsahip holder, int position, @NonNull Yorumkullanicilar model) {
                                                holder.setData(getApplicationContext(), model.getYorum(),model.getAdsoyad(),model.getUrl(),model.getUid(),model.getZaman(),model.getKonum());

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                String kullans_id = user.getUid();

                                                holder.setProfile(getApplicationContext());
                                                String guid = getItem(position).getUid();

                                                holder.setOnClickListener(new Yorumsahip.Clicklistener() {
                                                    @Override
                                                    public void onItemlongClick(View view, int position) {

                                                        Toast.makeText(GosterkullaniciActivity.this, "Yetkiniz Yok!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });



                                            }

                                            @NonNull
                                            @Override
                                            public Yorumsahip onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                                View view = LayoutInflater.from(parent.getContext())
                                                        .inflate(R.layout.yorumcu_items, parent, false);


                                                return new Yorumsahip(view);
                                            }
                                        };
                                firebaseRecyclerAdapter.startListening();
                                recyclerView_gosteryorum.setAdapter(firebaseRecyclerAdapter);



                            }else {
                                g_profilgonderi.setVisibility(View.GONE);
                                g_profilyorum.setVisibility(View.GONE);
                                recyclerView_gostergonderi.setVisibility(View.GONE);
                                recyclerView_gosteryorum.setVisibility(View.GONE);
                                deneme.setVisibility(View.VISIBLE);
                                deneme.setImageResource(R.drawable.ic_baseline_kilitli);

                            }

                        }else {
                            Toast.makeText(GosterkullaniciActivity.this, "Profil bulunamadı!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
