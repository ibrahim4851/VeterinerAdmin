package com.example.veterineradmin.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.veterineradmin.Models.CevaplaModel;
import com.example.veterineradmin.Models.SoruModel;
import com.example.veterineradmin.R;
import com.example.veterineradmin.RestApi.ManagerAll;
import com.example.veterineradmin.Utils.Warnings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class VeterinerSoruAdapter extends RecyclerView.Adapter<VeterinerSoruAdapter.ViewHolder> {

    List<SoruModel> list;
    Context context;
    Activity activity;

    public VeterinerSoruAdapter(List<SoruModel> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sorularitemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.soruKullaniciText.setText(list.get(position).getKadi());
        holder.soruSoruText.setText(list.get(position).getSoru());
        holder.soruAramaButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ara(list.get(position).getTelefon().toString());
            }
        });

        holder.soruCevaplaButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cevaplaAlert(list.get(position).getMusid().toString(), list.get(position).getSoruid().toString(), position, list.get(position).getSoru());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView soruKullaniciText, soruSoruText;
        ImageView soruCevaplaButon, soruAramaButon;

        //CardView kampanyaCardView;
        //itemView ile listviewin her elemanı için layout ile oluşturduğumuz view tanımlaması işlemi gerçekleşir
        public ViewHolder(View itemView) {
            super(itemView);
            soruKullaniciText = itemView.findViewById(R.id.soruKullaniciText);
            soruSoruText = itemView.findViewById(R.id.soruSoruText);
            soruCevaplaButon = itemView.findViewById(R.id.soruCevaplaButon);
            soruAramaButon = itemView.findViewById(R.id.soruAramaButon);
        }
    }

    public void deleteToList(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void ara(String numara)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:"+numara));
        activity.startActivity(intent);
    }

    public void cevaplaAlert(String musid, String soruid, int position, String soru) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.cevaplaalertlayout, null);

        final EditText cevaplaEditText = view.findViewById(R.id.cevaplaEditText);
        Button cevaplaButon = view.findViewById(R.id.cevaplaButon);
        TextView cevaplanacakSoruText = view.findViewById(R.id.cevaplanacakSoruText);
        cevaplanacakSoruText.setText(soru);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog alertDialog = alert.create();
        cevaplaButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cevap = cevaplaEditText.getText().toString();
                cevaplaEditText.setText("");
                alertDialog.cancel();
                cevapla(musid, soruid, cevap, alertDialog, position);
            }
        });
        alertDialog.show();
    }

    public void cevapla(String musid, String soruid, String text, AlertDialog alertDialog, int position)
    {
        Call<CevaplaModel> req = ManagerAll.getInstance().cevapla(musid, soruid, text);
        req.enqueue(new Callback<CevaplaModel>() {
            @Override
            public void onResponse(Call<CevaplaModel> call, Response<CevaplaModel> response) {
                if (response.body().isTf())
                {
                    Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                    deleteToList(position);
                }
                else
                {
                    Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<CevaplaModel> call, Throwable t) {
                Toast.makeText(context, Warnings.internetProblemText, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
