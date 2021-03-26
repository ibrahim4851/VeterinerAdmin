package com.example.veterineradmin.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.veterineradmin.R;
import com.example.veterineradmin.Utils.ChangeFragments;


public class HomeFragment extends Fragment {

    private View view;
    private LinearLayout kampanyaLayout, asiTakipLayout, soruLayout, kullanicilarLayout;
    private ChangeFragments changeFragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        tanimla();
        clickToLayout();
        return view;
    }

    public void tanimla()
    {
        kullanicilarLayout = view.findViewById(R.id.kullanicilarLayout);
        asiTakipLayout = view.findViewById(R.id.asiTakipLayout);
        kampanyaLayout = view.findViewById(R.id.kampanyaLayout);
        changeFragments = new ChangeFragments(getContext());
        soruLayout = view.findViewById(R.id.soruLayout);
    }

    public void clickToLayout()
    {
        kampanyaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragments.change(new KampanyaFragment());
            }
        });
        asiTakipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments.change(new AsiTakipFragment());
            }
        });
        soruLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments.change(new SorularFragment());
            }
        });
        kullanicilarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments.change(new KullanicilarFragment());
            }
        });
    }

}