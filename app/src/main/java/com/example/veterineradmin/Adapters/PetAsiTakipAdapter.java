package com.example.veterineradmin.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.veterineradmin.Models.AsiOnaylaModel;
import com.example.veterineradmin.Models.KampanyaModel;
import com.example.veterineradmin.Models.KampanyaSilModel;
import com.example.veterineradmin.Models.PetAsiTakipModel;
import com.example.veterineradmin.R;
import com.example.veterineradmin.RestApi.ManagerAll;
import com.example.veterineradmin.Utils.Warnings;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class PetAsiTakipAdapter extends RecyclerView.Adapter<PetAsiTakipAdapter.ViewHolder> {

    List<PetAsiTakipModel> list;
    Context context;
    Activity activity;

    public PetAsiTakipAdapter(List<PetAsiTakipModel> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.asitakiplayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.asiTakipPetName.setText(list.get(position).getPetisim());
        holder.asiTakipBilgiText.setText(list.get(position).getKadi() + " isimli kullanıcının " + list.get(position).getPetisim() +
                " isimli petinin " + list.get(position).getAsiisim() + " aşısı yapılacaktır.");
        Picasso.get().load(list.get(position).getPetresim()).resize(200, 200)
                .into(holder.asiTakipImage);
        holder.asiTakipAraButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ara(list.get(position).getTelefon().toString());
            }
        });

        holder.asiTakipOkButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asiOnayla(list.get(position).getAsiid().toString(),position);
            }
        });

        holder.asiTakipCancelButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onaylama(list.get(position).getAsiid().toString(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView asiTakipPetName, asiTakipBilgiText;
        ImageView asiTakipImage, asiTakipOkButon, asiTakipCancelButon, asiTakipAraButon;

        //CardView kampanyaCardView;
        //itemView ile listviewin her elemanı için layout ile oluşturduğumuz view tanımlaması işlemi gerçekleşir
        public ViewHolder(View itemView) {
            super(itemView);
            asiTakipPetName = itemView.findViewById(R.id.asiTakipPetName);
            asiTakipBilgiText = itemView.findViewById(R.id.asiTakipBilgiText);
            asiTakipImage = itemView.findViewById(R.id.asiTakipImage);
            asiTakipOkButon = itemView.findViewById(R.id.asiTakipOkButon);
            asiTakipCancelButon = itemView.findViewById(R.id.asiTakipCancelButon);
            asiTakipAraButon = itemView.findViewById(R.id.asiTakipAraButon);
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

    public void asiOnayla(String id, int position)
    {
        Call<AsiOnaylaModel> req = ManagerAll.getInstance().asiOnayla(id);
        req.enqueue(new Callback<AsiOnaylaModel>() {
            @Override
            public void onResponse(Call<AsiOnaylaModel> call, Response<AsiOnaylaModel> response) {
                Toast.makeText(context, response.body().getText(), Toast.LENGTH_SHORT).show();
                deleteToList(position);
            }

            @Override
            public void onFailure(Call<AsiOnaylaModel> call, Throwable t) {
                Toast.makeText(context, Warnings.internetProblemText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onaylama(String id, int position)
    {
        Call<AsiOnaylaModel> req = ManagerAll.getInstance().asiIptal(id);
        req.enqueue(new Callback<AsiOnaylaModel>() {
            @Override
            public void onResponse(Call<AsiOnaylaModel> call, Response<AsiOnaylaModel> response) {
                Toast.makeText(context, response.body().getText(), Toast.LENGTH_SHORT).show();
                deleteToList(position);
            }

            @Override
            public void onFailure(Call<AsiOnaylaModel> call, Throwable t) {
                Toast.makeText(context, Warnings.internetProblemText, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
