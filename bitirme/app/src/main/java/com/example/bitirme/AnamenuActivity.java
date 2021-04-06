package com.example.bitirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import javax.annotation.Nullable;

public class AnamenuActivity extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_anamenu, container, false);
        WebView ogrenweb = (WebView)view.findViewById(R.id.ogrencigit);
        ogrenweb.getSettings().setJavaScriptEnabled(true);
        ogrenweb.setWebViewClient(new WebViewClient());
        ogrenweb.loadUrl("https://ogrencisin.com/indirimler/");
        return view;

    }

}