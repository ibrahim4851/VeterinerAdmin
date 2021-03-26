package com.example.veterineradmin.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.veterineradmin.Models.KampanyaModel;
import com.example.veterineradmin.Models.KampanyaSilModel;
import com.example.veterineradmin.R;
import com.example.veterineradmin.RestApi.ManagerAll;
import com.example.veterineradmin.Utils.Warnings;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class KampanyaAdapter extends RecyclerView.Adapter<KampanyaAdapter.ViewHolder>{

    List<KampanyaModel> list;
    Context context;
    Activity activity;

    public KampanyaAdapter(List<KampanyaModel> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kampanyaitemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.kampanyaBaslik.setText(list.get(position).getBaslik());
        holder.kampanyaIcerik.setText(list.get(position).getText());
        Picasso.get().load(list.get(position).getResim()).resize(200, 200)
                .into(holder.kampanyaResim);
        holder.kampanyaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kampanyaSil(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView kampanyaBaslik, kampanyaIcerik;
        ImageView kampanyaResim;
        CardView kampanyaCardView;
        //itemView ile listviewin her elemanı için layout ile oluşturduğumuz view tanımlaması işlemi gerçekleşir
        public ViewHolder(View itemView) {
            super(itemView);
            kampanyaBaslik = itemView.findViewById(R.id.kampanyaBaslik);
            kampanyaIcerik= itemView.findViewById(R.id.kampanyaIcerik);
            kampanyaResim = itemView.findViewById(R.id.kampanyaResim);
            kampanyaCardView = itemView.findViewById(R.id.kampanyaCardView);
        }
    }

    public void kampanyaSil(int position) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.kampanyasillayout, null);
        Button kampanyaSilTamam = view.findViewById(R.id.kampanyaSilTamam);
        Button kampanyaSilIptal = view.findViewById(R.id.kampanyaSilIptal);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog alertDialog = alert.create();
        kampanyaSilTamam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kampanyaSil(list.get(position).getId().toString(),position);
                alertDialog.cancel();
            }
        });

        kampanyaSilIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void kampanyaSil(String id, int position)
    {
        Call<KampanyaSilModel> req = ManagerAll.getInstance().kampanyaSil(id);
        req.enqueue(new Callback<KampanyaSilModel>() {
            @Override
            public void onResponse(Call<KampanyaSilModel> call, Response<KampanyaSilModel> response) {
                if (response.body().isTf())
                {
                    Toast.makeText(context, response.body().getText(), Toast.LENGTH_SHORT).show();
                    deleteToList(position);
                }
                else
                {
                    Toast.makeText(context, response.body().getText(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KampanyaSilModel> call, Throwable t) {
                Toast.makeText(context, Warnings.internetProblemText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteToList(int position)
    {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

}
