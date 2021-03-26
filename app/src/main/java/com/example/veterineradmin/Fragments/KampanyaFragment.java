package com.example.veterineradmin.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.veterineradmin.Adapters.KampanyaAdapter;
import com.example.veterineradmin.Models.KampanyaEkleModel;
import com.example.veterineradmin.Models.KampanyaModel;
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

import static android.app.Activity.RESULT_OK;


public class KampanyaFragment extends Fragment {

    private View view;
    private RecyclerView kampanyRecView;
    private List<KampanyaModel> kampanyaList;
    private KampanyaAdapter kampanyaAdapter;
    private ChangeFragments changeFragments;
    private Button kampanyaEkle;
    private ImageView kampanyaEkleImageView, kampanyaBackImage;
    private Bitmap bitmap;
    private String imageString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kampanya, container, false);
        tanimla();
        getKampanya();
        click();
        return view;
    }

    public void tanimla() {
        kampanyRecView = view.findViewById(R.id.kampanyRecView);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        kampanyRecView.setLayoutManager(mng);
        kampanyaEkle = view.findViewById(R.id.kampanyaEkle);
        kampanyaList = new ArrayList<>();
        kampanyaBackImage = view.findViewById(R.id.kampanyaBackImage);
        changeFragments = new ChangeFragments(getContext());
        bitmap = null;
        imageString = "";
    }

    public void click() {
        kampanyaEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addKampanya();
            }
        });
        kampanyaBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments.change(new HomeFragment());
            }
        });
    }

    public void getKampanya() {
        Call<List<KampanyaModel>> req = ManagerAll.getInstance().getKampanya();
        req.enqueue(new Callback<List<KampanyaModel>>() {
            @Override
            public void onResponse(Call<List<KampanyaModel>> call, Response<List<KampanyaModel>> response) {
                if (response.body().get(0).isTf()) {
                    kampanyaList = response.body();
                    kampanyaAdapter = new KampanyaAdapter(kampanyaList, getContext(),getActivity());
                    kampanyRecView.setAdapter(kampanyaAdapter);
                } else {
                    Toast.makeText(getContext(), "Herhangi bir kampanya bulumamaktadır.", Toast.LENGTH_LONG).show();
                    changeFragments.change(new HomeFragment());
                }
            }

            @Override
            public void onFailure(Call<List<KampanyaModel>> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addKampanya() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.kampanyaeklelayout, null);

        final EditText kampanyaBaslikEditText = view.findViewById(R.id.kampanyaBaslikEditText);
        final EditText kampanyaIcerikText = view.findViewById(R.id.kampanyaIcerikText);
        Button kampanyaEkleButon = view.findViewById(R.id.kampanyaEkleButon);
        Button kampanyaImageEkleButon = view.findViewById(R.id.kampanyaImageEkleButon);
        kampanyaEkleImageView = view.findViewById(R.id.kampanyaEkleImageView);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog alertDialog = alert.create();
        kampanyaImageEkleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }
        });
        alertDialog.show();
        kampanyaEkleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageToString().equals("") && !kampanyaBaslikEditText.getText().toString().equals("")
                        && !kampanyaIcerikText.getText().toString().equals("")) {
                    kampanyaEkle(kampanyaBaslikEditText.getText().toString(),
                            kampanyaIcerikText.getText().toString(),
                            imageToString(),
                            alertDialog);
                    kampanyaBaslikEditText.setText("");
                    kampanyaIcerikText.setText("");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777 && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                kampanyaEkleImageView.setImageBitmap(bitmap);
                kampanyaEkleImageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void kampanyaEkle(String baslik, String icerik, String image, AlertDialog alertDialog) {
        Call<KampanyaEkleModel> req = ManagerAll.getInstance().addKampanya(baslik, icerik, image);
        req.enqueue(new Callback<KampanyaEkleModel>() {
            @Override
            public void onResponse(Call<KampanyaEkleModel> call, Response<KampanyaEkleModel> response) {
                if (response.body().isTf()) {
                    Toast.makeText(getContext(), response.body().getSonuc(), Toast.LENGTH_SHORT).show();
                    getKampanya();
                    alertDialog.cancel();
                } else {
                    Toast.makeText(getContext(), response.body().getSonuc(), Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<KampanyaEkleModel> call, Throwable t) {
                Toast.makeText(getContext(), Warnings.internetProblemText, Toast.LENGTH_SHORT).show();
            }
        });
    }

}