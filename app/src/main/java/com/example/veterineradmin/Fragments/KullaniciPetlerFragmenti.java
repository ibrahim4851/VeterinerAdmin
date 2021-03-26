package com.example.veterineradmin.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.veterineradmin.Adapters.PetAdapter;
import com.example.veterineradmin.Models.PetEkle;
import com.example.veterineradmin.Models.PetModel;
import com.example.veterineradmin.R;
import com.example.veterineradmin.RestApi.ManagerAll;
import com.example.veterineradmin.Utils.ChangeFragments;
import com.example.veterineradmin.Utils.Warnings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class KullaniciPetlerFragmenti extends Fragment {

    private View view;
    private String musid, imageString="";
    private ChangeFragments changeFragments;
    RecyclerView userPetListRecView;
    ImageView petEkleResimYok, kullaniciBackImage;
    TextView petEkleUyariText;
    Button userPetEkle;
    private List<PetModel> list;
    private PetAdapter petAdapter;
    private ImageView petEkleImageView;
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kullanici_fragmenti, container, false);
        tanimla();
        getPets(musid);
        click();
        return view;
    }

    public void tanimla()
    {
        musid = getArguments().get("userid").toString();
        Log.i("gelenmusid",musid);
        changeFragments = new ChangeFragments(getContext());

        userPetListRecView = view.findViewById(R.id.userPetListRecView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        userPetListRecView.setLayoutManager(layoutManager);
        petEkleResimYok = view.findViewById(R.id.petEkleResimYok);
        petEkleUyariText = view.findViewById(R.id.petEkleUyariText);
        userPetEkle = view.findViewById(R.id.userPetEkle);
        kullaniciBackImage = view.findViewById(R.id.kullaniciBackImage);
        list = new ArrayList<>();
        bitmap = null;
    }

    public void click()
    {
        userPetEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petEkleAlert();
            }
        });
        kullaniciBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments.change(new HomeFragment());
            }
        });
    }

    public void getPets(String id)
    {
        Call<List<PetModel>> req = ManagerAll.getInstance().getPets(id);
        req.enqueue(new Callback<List<PetModel>>() {
            @Override
            public void onResponse(Call<List<PetModel>> call, Response<List<PetModel>> response) {
                if (response.body().get(0).isTf())
                {
                    userPetListRecView.setVisibility(View.VISIBLE);
                    petEkleResimYok.setVisibility(View.GONE);
                    petEkleUyariText.setVisibility(View.GONE);
                    list = response.body();
                    petAdapter = new PetAdapter(list, getContext(), getActivity(), musid);
                    userPetListRecView.setAdapter(petAdapter);
                    Toast.makeText(getContext(), "Kullanıcıya ait "+response.body().size()+" adet pet bulunmuştur.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Kullanıcıya ait pet bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                    petEkleResimYok.setVisibility(View.VISIBLE);
                    petEkleUyariText.setVisibility(View.VISIBLE);
                    userPetListRecView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<PetModel>> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void petEkleAlert() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.peteklelayout, null);

        final EditText petEkleNameEditText = view.findViewById(R.id.petEkleNameEditText);
        final EditText petEkleTurEditText = view.findViewById(R.id.petEkleTurEditText);
        final EditText petEkleCinsEditText = view.findViewById(R.id.petEkleCinsEditText);
        Button petEkleEkleButon = view.findViewById(R.id.petEkleEkleButon);
        Button petEkleResimSecButon = view.findViewById(R.id.petEkleResimSecButon);
        petEkleImageView = view.findViewById(R.id.petEkleImageView);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog alertDialog = alert.create();
        petEkleResimSecButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }
        });
        petEkleEkleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageToString().equals("") && !petEkleNameEditText.getText().toString().equals("") && !petEkleTurEditText.getText().toString().equals("") && !petEkleCinsEditText.getText().toString().equals("")) {

                    petEkle(musid, petEkleNameEditText.getText().toString(),
                            petEkleTurEditText.getText().toString(),
                            petEkleCinsEditText.getText().toString(),
                            imageToString(),
                            alertDialog);
                    petEkleNameEditText.setText("");
                    petEkleTurEditText.setText("");
                    petEkleCinsEditText.setText("");
                } else {
                    Toast.makeText(getContext(), "Tüm alanların doldurulamsı ve resmin seçilmesi zorunludur.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

    void galeriAc() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 777);
    }

    public String imageToString() {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byt = byteArrayOutputStream.toByteArray();
            String imageString = Base64.getEncoder().encodeToString(byt);
            return imageString;
        }
        else
        {
            return imageString;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777 && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                petEkleImageView.setImageBitmap(bitmap);
                petEkleImageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void petEkle(String musid, String petismi, String pettur, String petcins, String imageString, AlertDialog alertDialog)
    {
        Call<PetEkle> req = ManagerAll.getInstance().petEkle(musid, petismi, pettur, petcins, imageString);
        req.enqueue(new Callback<PetEkle>() {
            @Override
            public void onResponse(Call<PetEkle> call, Response<PetEkle> response) {
                if (response.body().isTf())
                {
                    getPets(musid);
                    Toast.makeText(getContext(), response.body().getText(), Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
                else
                {
                    Toast.makeText(getContext(), response.body().getText(), Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<PetEkle> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_SHORT).show();
            }
        });
    }

}