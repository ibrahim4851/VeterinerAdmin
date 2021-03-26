package com.example.veterineradmin.Fragments;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.veterineradmin.Adapters.VeterinerSoruAdapter;
import com.example.veterineradmin.Models.SoruModel;
import com.example.veterineradmin.R;
import com.example.veterineradmin.RestApi.ManagerAll;
import com.example.veterineradmin.Utils.ChangeFragments;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class SorularFragment extends Fragment {

    private View view;
    private RecyclerView soruRecyView;
    private List<SoruModel> list;
    private VeterinerSoruAdapter veterinerSoruAdapter;
    private ChangeFragments changeFragments;
    private ImageView soruBackImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_sorular, container, false);
        tanimla();
        click();
        istekAt();
        return view;
    }

    public void tanimla()
    {
        soruRecyView = view.findViewById(R.id.soruRecyView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        soruRecyView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        changeFragments = new ChangeFragments(getContext());
        soruBackImage = view.findViewById(R.id.soruBackImage);
    }

    public void click()
    {
        soruBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments.change(new HomeFragment());
            }
        });
    }

    public void istekAt()
    {
        Call<List<SoruModel>> req = ManagerAll.getInstance().getSoru();
        req.enqueue(new Callback<List<SoruModel>>() {
            @Override
            public void onResponse(Call<List<SoruModel>> call, Response<List<SoruModel>> response) {
                if (response.body().get(0).isTf())
                {
                    list = response.body();
                    veterinerSoruAdapter = new VeterinerSoruAdapter(list,getContext(),getActivity());
                    soruRecyView.setAdapter(veterinerSoruAdapter);
                }
                else
                {
                    Toast.makeText(getContext(), "Veteriner hekime sorulan soru yoktur", Toast.LENGTH_SHORT).show();
                    changeFragments.change(new HomeFragment());
                }
            }

            @Override
            public void onFailure(Call<List<SoruModel>> call, Throwable t) {

            }
        });
    }

}