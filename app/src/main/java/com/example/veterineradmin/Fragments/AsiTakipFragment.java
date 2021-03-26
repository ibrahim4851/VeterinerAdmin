package com.example.veterineradmin.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.veterineradmin.Adapters.PetAsiTakipAdapter;
import com.example.veterineradmin.Models.PetAsiTakipModel;
import com.example.veterineradmin.R;
import com.example.veterineradmin.RestApi.ManagerAll;
import com.example.veterineradmin.Utils.ChangeFragments;
import com.example.veterineradmin.Utils.Warnings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AsiTakipFragment extends Fragment {

    private View view;
    private DateFormat format;
    private Date date;
    private String today;
    private ChangeFragments changeFragments;
    private RecyclerView asiTakipRecyclerView;
    private List<PetAsiTakipModel> list;
    private PetAsiTakipAdapter petAsiTakipAdapter;
    private ImageView asiBackImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_asi_takip, container, false);
        tanimla();
        click();
        istekAt(today);
        return view;
    }

    public void tanimla()
    {
        format = new SimpleDateFormat("dd/MM/yyyy");
        date = Calendar.getInstance().getTime();
        today = format.format(date);
        Log.i("bugununtarihi ",today);
        asiTakipRecyclerView = view.findViewById(R.id.asiTakipRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        asiTakipRecyclerView.setLayoutManager(layoutManager);
        changeFragments = new ChangeFragments(getContext());
        list = new ArrayList<>();
        asiBackImage = view.findViewById(R.id.asiBackImage);
    }

    public void click()
    {
        asiBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments.change(new HomeFragment());
            }
        });
    }

    public void istekAt(String tarih)
    {
        Call<List<PetAsiTakipModel>> req = ManagerAll.getInstance().getPetAsiTakip(tarih);
        req.enqueue(new Callback<List<PetAsiTakipModel>>() {
            @Override
            public void onResponse(Call<List<PetAsiTakipModel>> call, Response<List<PetAsiTakipModel>> response) {
                if (response.body().get(0).isTf())
                {
                    Toast.makeText(getContext(), "Bugün "+response.body().size()+" pete aşı yapılacaktır.", Toast.LENGTH_SHORT).show();
                    list = response.body();
                    petAsiTakipAdapter = new PetAsiTakipAdapter(list, getContext(), getActivity());
                    asiTakipRecyclerView.setAdapter(petAsiTakipAdapter);
                }
                else
                {
                    Toast.makeText(getContext(), "Bugün aşısı yapılacak pet yoktur.", Toast.LENGTH_SHORT).show();
                    changeFragments.change(new HomeFragment());
                }
            }

            @Override
            public void onFailure(Call<List<PetAsiTakipModel>> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_SHORT).show();
            }
        });
    }

}