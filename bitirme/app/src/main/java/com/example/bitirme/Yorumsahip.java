package com.example.bitirme;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


public class Yorumsahip extends RecyclerView.ViewHolder {

    TextView yorumprofilbak,yorumdayorum,yorumdaadi,yorumdazaman,yorumdakonum;
    ImageView yorumunresmi;

    public Yorumsahip(@NonNull View itemView) {
        super(itemView);


        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClicklistener.onItemlongClick(view,getAdapterPosition());
                return false;
            }
        });

    }

    public void setProfile(Context viewHolder){

        yorumprofilbak = itemView.findViewById(R.id.yorumdaki_adsoyad);
    }

    public void setData(Context context, String yorum,String adisoyadi,String url,String uid,String zaman,String konum){

         yorumdayorum = itemView.findViewById(R.id.yorumdaki_yorum);
         yorumunresmi = itemView.findViewById(R.id.yorumdaki_resim);
         yorumdaadi = itemView.findViewById(R.id.yorumdaki_adsoyad);
         yorumdazaman = itemView.findViewById(R.id.yorumdaki_zaman);
         yorumdakonum = itemView.findViewById(R.id.yorumdaki_yorumlanan);


        yorumdakonum.setText(konum);
        yorumdazaman.setText(zaman);
        yorumdaadi.setText(adisoyadi);
        yorumdayorum.setText(yorum);
        Picasso.get().load(url).into(yorumunresmi);
    }
    public void setData(FragmentActivity activity, String yorum, String adisoyadi, String url, String uid, String zaman,String konum){

        yorumdayorum = itemView.findViewById(R.id.yorumdaki_yorum);
        yorumunresmi = itemView.findViewById(R.id.yorumdaki_resim);
        yorumdaadi = itemView.findViewById(R.id.yorumdaki_adsoyad);
        yorumdazaman = itemView.findViewById(R.id.yorumdaki_zaman);
        yorumdakonum = itemView.findViewById(R.id.yorumdaki_yorumlanan);


        yorumdakonum.setText(konum);
        yorumdazaman.setText(zaman);
        yorumdaadi.setText(adisoyadi);
        yorumdayorum.setText(yorum);
        Picasso.get().load(url).into(yorumunresmi);
    }

    private Yorumsahip.Clicklistener mClicklistener;


    public interface Clicklistener{
        void onItemlongClick(View view , int position);

    }
    public void setOnClickListener(Yorumsahip.Clicklistener clickListener){
        mClicklistener = clickListener;
    }
}

