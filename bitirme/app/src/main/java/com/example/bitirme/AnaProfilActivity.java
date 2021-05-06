package com.example.bitirme;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;


public class AnaProfilActivity extends Fragment implements View.OnClickListener {



    TextView adsoyad,yas,iletisim;
    ImageButton imageButtonEdit,eklepost,profilyorum,profilgonderi;
    RecyclerView recyclerView_gonderi,recyclerView_yorum;
    ImageView ppresim;
    String url,post,yorum;
    DocumentReference reference;
    DatabaseReference databaseReference;
    DatabaseReference g_reference;
    FirebaseFirestore firestore;
    FirebaseDatabase firebaseDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    Uri imageUri;
    private  static  final  int PICK_IMAGE = 1;


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

        g_reference = database.getReference("Resimler").child(kullanici_id);
        databaseReference =database.getReference("KYorumlar").child(kullanici_id);


        ppresim = getActivity().findViewById(R.id.ppfoto);
        adsoyad = getActivity().findViewById(R.id.profil_adi);
        yas = getActivity().findViewById(R.id.profil_yasi);
        iletisim = getActivity().findViewById(R.id.profil_iletsim);


        eklepost = getActivity().findViewById(R.id.ib_eklepost);
        imageButtonEdit = getActivity().findViewById(R.id.ib_editmenu);
        profilyorum= getActivity().findViewById(R.id.profil_yorumlar);
        profilgonderi= getActivity().findViewById(R.id.profil_fotolar);

        iletisim.setOnClickListener(this);
        ppresim.setOnClickListener(this);
        imageButtonEdit.setOnClickListener(this);
        eklepost.setOnClickListener(this);
        profilgonderi.setOnClickListener(this);
        profilyorum.setOnClickListener(this);

        recyclerView_yorum = getActivity().findViewById(R.id.profil_kyorum);
        recyclerView_yorum.setHasFixedSize(true);
        recyclerView_yorum.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView_gonderi = getActivity().findViewById(R.id.profil_gonderiler);
        recyclerView_gonderi.setHasFixedSize(true);
        recyclerView_gonderi.setLayoutManager(new LinearLayoutManager(getActivity()));

        profilgonderi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_gonderi.setVisibility(View.VISIBLE);
                recyclerView_yorum.setVisibility(View.GONE);
            }
        });
        profilyorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_gonderi.setVisibility(View.GONE);
                recyclerView_yorum.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ib_editmenu:
                Profiledit bottomSheetMenu = new Profiledit();
                bottomSheetMenu.show(getFragmentManager(),"bottomsheet");
                break;
            case R.id.ib_eklepost:
                Intent intent2 = new Intent(getActivity(),GonderiActivity.class);
                startActivity(intent2);
                break;
            case R.id.profil_iletsim:
                Linkify.addLinks(iletisim, Linkify.WEB_URLS);
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

        FirebaseRecyclerOptions<Yorumkullanicilar> options =
                new FirebaseRecyclerOptions.Builder<Yorumkullanicilar>()
                        .setQuery(databaseReference, Yorumkullanicilar.class)
                        .build();


        FirebaseRecyclerAdapter<Yorumkullanicilar, Yorumsahip> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Yorumkullanicilar, Yorumsahip>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Yorumsahip holder, int position, @NonNull Yorumkullanicilar model) {
                        holder.setData(getActivity(), model.getYorum(),model.getAdsoyad(),model.getUrl(),model.getUid(),model.getZaman(),model.getKonum());

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String kullans_id = user.getUid();

                        String guid = getItem(position).getUid();

                        yorum = getItem(position).getYorum();

                        holder.setOnClickListener(new Yorumsahip.Clicklistener() {
                            @Override
                            public void onItemlongClick(View view, int position) {
                                if (guid.equals(kullans_id)){
                                    holder.setOnClickListener(new Yorumsahip.Clicklistener() {
                                        @Override
                                        public void onItemlongClick(View view, int position) {

                                            yorum = getItem(position).getYorum();

                                            Toast.makeText(getContext(), "Yorumu Konumdan Silmeniz Gerekiyor!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    holder.setOnClickListener(null);
                                    return;
                                }
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
        recyclerView_yorum.setAdapter(firebaseRecyclerAdapter);

        FirebaseRecyclerOptions<Gonderikullanicilar> options2 =
                new FirebaseRecyclerOptions.Builder<Gonderikullanicilar>()
                        .setQuery(g_reference,Gonderikullanicilar.class)
                        .build();

        FirebaseRecyclerAdapter<Gonderikullanicilar,Gonderisahip> firebaseRecyclerAdapter3 =
                new FirebaseRecyclerAdapter<Gonderikullanicilar, Gonderisahip>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull Gonderisahip holder, int position, @NonNull final Gonderikullanicilar model) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String kullans_id = user.getUid();

                        holder.SetPost(getActivity(),model.getGkAdsoyad(),model.getGkUrl(),model.getGkPostUri(),model.getGkTime()
                                ,model.getGkUid(),model.getGkType(),model.getGkAciklama());

                       String postuid = getItem(position).getGkPostUri();
                        String guid = getItem(position).getGkUid();

                        holder.gonderi_sil.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (guid.equals(kullans_id)){
                                    holder.gonderi_sil.setVisibility(View.VISIBLE);
                                    gonderisil(postuid);
                                }else {
                                    holder.gonderi_sil.setVisibility(View.INVISIBLE);
                                }

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public Gonderisahip onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.activity_gonderisahip,parent,false);

                        return new Gonderisahip(view);

                    }
                };
        firebaseRecyclerAdapter3.startListening();

        recyclerView_gonderi.setAdapter(firebaseRecyclerAdapter3);



    }



    private void gonderisil(String postuid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sil");
        builder.setMessage("Gönderi Silinsin mi?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Query query = g_reference.orderByChild("gk_postUri").equalTo(postuid);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(getActivity(), "Gönderi Silindi", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}