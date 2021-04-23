package com.example.bitirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.annotation.Nullable;

public class EglencActivity extends Fragment implements View.OnClickListener {

    Button avmmgit,cafegit,restorangit,parkgit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_eglenc, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cafegit = getActivity().findViewById(R.id.cafegit);
        restorangit = getActivity().findViewById(R.id.restorangit);
        parkgit = getActivity().findViewById(R.id.parkgit);
        avmmgit = getActivity().findViewById(R.id.avmgit);

        avmmgit.setOnClickListener(this);
        cafegit.setOnClickListener(this);
        restorangit.setOnClickListener(this);
        parkgit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avmgit:
                Intent intent = new Intent(getActivity(),AvmActivity.class);
                startActivity(intent);
                break;
            case R.id.cafegit:
                Intent intent2 = new Intent(getActivity(),CafeActivity.class);
                startActivity(intent2);
                break;
            case R.id.restorangit:
                Intent intent3 = new Intent(getActivity(),RestorantActivity.class);
                startActivity(intent3);
                break;
            case R.id.parkgit:
                Intent intent4 = new Intent(getActivity(),ParkActivity.class);
                startActivity(intent4);
                break;


        }

    }
}