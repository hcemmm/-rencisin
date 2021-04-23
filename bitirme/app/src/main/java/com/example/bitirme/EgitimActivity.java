package com.example.bitirme;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.annotation.Nullable;

public class EgitimActivity extends Fragment implements View.OnClickListener {

    Button unigit,kirtasgit,yurttgit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_egitim, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        unigit = getActivity().findViewById(R.id.uniyegit);
        kirtasgit = getActivity().findViewById(R.id.kirtasiyegit);
        yurttgit = getActivity().findViewById(R.id.yurtlaragit);

        unigit.setOnClickListener(this);
        kirtasgit.setOnClickListener(this);
        yurttgit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uniyegit:
                Intent intent = new Intent(getActivity(), UniversiteActivity.class);
                startActivity(intent);
                break;
            case R.id.yurtlaragit:
                Intent intent2 = new Intent(getActivity(),YurtActivity.class);
                startActivity(intent2);
                break;
            case R.id.kirtasiyegit:
                Intent intent3 = new Intent(getActivity(),KirtasiyeActivity.class);
                startActivity(intent3);
                break;


        }
    }
}