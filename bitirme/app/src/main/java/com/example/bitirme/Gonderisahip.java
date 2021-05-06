package com.example.bitirme;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Gonderisahip extends RecyclerView.ViewHolder {

    ImageView kullanicifoto_paylasim,image_paylasim;
    TextView aciklma_paylasim,zaman_paylasim,adsoyad_paylasim,gonderi_sil;
    FirebaseDatabase database = FirebaseDatabase.getInstance();



    public Gonderisahip(@NonNull View itemView) {
        super(itemView);
    }


    public void SetPost(FragmentActivity activity, String name, String url, String postUri, String time,
                        String uid, String type, String desc){

        SimpleExoPlayer exoPlayer;
        kullanicifoto_paylasim = itemView.findViewById(R.id.paylasim_kullanicifoto);
        image_paylasim = itemView.findViewById(R.id.paylasim_image); 
        aciklma_paylasim = itemView.findViewById(R.id.paylasim_aciklama);
        zaman_paylasim = itemView.findViewById(R.id.paylasim_zaman);
        adsoyad_paylasim = itemView.findViewById(R.id.paylasim_adsoyad);
        gonderi_sil = itemView.findViewById(R.id.paylasim_sil);


        PlayerView playerView = itemView.findViewById(R.id.paylasim_resim);

        if (type.equals("iv")){

            Picasso.get().load(url).into(kullanicifoto_paylasim);
            Picasso.get().load(postUri).into(image_paylasim);
            aciklma_paylasim.setText(desc);
            zaman_paylasim.setText(time);
            adsoyad_paylasim.setText(name);
            playerView.setVisibility(View.INVISIBLE);
        }
    }
    public void SetPost(Context context, String name, String url, String postUri, String time,
                        String uid, String type, String desc){

        SimpleExoPlayer exoPlayer;
        kullanicifoto_paylasim = itemView.findViewById(R.id.paylasim_kullanicifoto);
        image_paylasim = itemView.findViewById(R.id.paylasim_image);
        aciklma_paylasim = itemView.findViewById(R.id.paylasim_aciklama);
        zaman_paylasim = itemView.findViewById(R.id.paylasim_zaman);
        adsoyad_paylasim = itemView.findViewById(R.id.paylasim_adsoyad);
        gonderi_sil = itemView.findViewById(R.id.paylasim_sil);


        PlayerView playerView = itemView.findViewById(R.id.paylasim_resim);

        if (type.equals("iv")){

            Picasso.get().load(url).into(kullanicifoto_paylasim);
            Picasso.get().load(postUri).into(image_paylasim);
            aciklma_paylasim.setText(desc);
            zaman_paylasim.setText(time);
            adsoyad_paylasim.setText(name);
            playerView.setVisibility(View.INVISIBLE);
        }
    }
    }
