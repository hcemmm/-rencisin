package com.example.bitirme;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Inonuuniversite extends AppCompatActivity {

    TextView yorumdaki_ad;
    ImageView yorumyap_profilresmi;
    EditText yorum_alani;
    Button yorum_gonder,adresegit,webegit;
    String yorum,url,name_result,uid;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DocumentReference reference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore firestore;
    DatabaseReference databaseReference;
    Yorumkullanicilar yorumkullanicilar;
    Tumkullanicilar tumkullanicilar;
    DatabaseReference db1,db2,db3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inonuuniversite);

        yorumdaki_ad = findViewById(R.id.yorumdaki_adsoyad);
        yorum_alani = findViewById(R.id.yorum_alani);
        yorum_gonder = findViewById(R.id.yorum_yap);
        yorumyap_profilresmi = findViewById(R.id.kullanici_yorumpp);
        yorumkullanicilar = new Yorumkullanicilar();
        tumkullanicilar = new Tumkullanicilar();
        recyclerView = findViewById(R.id.recycler_yorumgor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adresegit = findViewById(R.id.haritagit);
        webegit = findViewById(R.id.webgit);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String kullan_id = user.getUid();

        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("kullanici").document(kullan_id);

        db1 = database.getReference("Tumyorumlar").child("InonuYorum").child(kullan_id);
        db2 = database.getReference("KYorumlar").child(kullan_id);
        db3 = database.getReference("Yorumlar").child("InonuYorumi");
        db3.keepSynced(true);


        databaseReference =database.getReference("Yorumlar").child("InonuYorumi");


        webegit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webintent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.inonu.edu.tr/"));
                startActivity(webintent);

            }
        });

        adresegit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent haritaintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/place/%C4%B0n%C3%B6n%C3%BC+%C3%9Cniversitesi/@38.3302973,38.4377768,17z/data=!4m12!1m6!3m5!1s0x4076384f417eed7b:0xf0135f4a7646a348!2zxLBuw7Zuw7wgw5xuaXZlcnNpdGVzaQ!8m2!3d38.3302973!4d38.4399655!3m4!1s0x4076384f417eed7b:0xf0135f4a7646a348!8m2!3d38.3302973!4d38.4399655"));
                startActivity(haritaintent);
            }
        });
        yorum_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yorum_yap();

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
                        if (task.getResult().exists()) {
                            name_result = task.getResult().getString("ad");
                            uid = task.getResult().getString("uid");

                            url = task.getResult().getString("url");
                            Picasso.get().load(url).into(yorumyap_profilresmi);

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
                        holder.setData(getApplicationContext(), model.getYorum(),model.getAdsoyad(),model.getUrl(),model.getUid(),model.getZaman(),model.getKonum());
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String kullans_id = user.getUid();

                        holder.setProfile(getApplicationContext());
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

                                            yorumsil(yorum);
                                        }
                                    });
                                }else {
                                    holder.setOnClickListener(null);
                                    return;
                                }
                            }
                        });

                        holder.yorumprofilbak.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getApplicationContext(),GosterkullaniciActivity.class);
                                intent.putExtra("guid",guid);
                                startActivity(intent);
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
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void yorumsil(String yorum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Inonuuniversite.this);
        builder.setTitle("Sil");
        builder.setMessage("Yorumunuz Silinsin mi?");
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Query query = databaseReference.orderByChild("yorum").equalTo(yorum);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

                Query query2 = db2.orderByChild("yorum").equalTo(yorum);
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Inonuuniversite.this, "Yorum Silindi", Toast.LENGTH_SHORT).show();
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


    private void yorum_yap() {
        Calendar callfordate = Calendar.getInstance();
        SimpleDateFormat currentdate = new
                SimpleDateFormat("dd-MMMM-yyyy");
        final  String savedate = currentdate.format(callfordate.getTime());

        Calendar callfortime = Calendar.getInstance();
        SimpleDateFormat currenttime = new
                SimpleDateFormat("HH:mm");
        final  String savetime = currenttime.format(callfortime.getTime());

        String time = savedate+":"+savetime;
        String comment = yorum_alani.getText().toString();
        if (comment != null){

            yorumkullanicilar.setYorum(comment);
            yorumkullanicilar.setAdsoyad(name_result);
            yorumkullanicilar.setUid(uid);
            yorumkullanicilar.setUrl(url);
            yorumkullanicilar.setZaman(time);
            yorumkullanicilar.setKonum("İnönü Üniversitesi");



            String id = db1.push().getKey();
            db1.child(id).setValue(yorumkullanicilar);

            String id2 = db2.push().getKey();
            db2.child(id2).setValue(yorumkullanicilar);

            String id1 = db3.push().getKey();
            db3.child(id1).setValue(yorumkullanicilar);


            yorum_alani.setText("");

            Toast.makeText(this, "Yorum Yapıldı.", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Lütfen Yorumunuzu Yazınız...", Toast.LENGTH_SHORT).show();
        }
    }

}