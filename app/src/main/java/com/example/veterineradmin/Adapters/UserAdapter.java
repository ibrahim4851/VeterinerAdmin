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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.veterineradmin.Fragments.KullaniciPetlerFragmenti;
import com.example.veterineradmin.Models.KampanyaModel;
import com.example.veterineradmin.Models.KampanyaSilModel;
import com.example.veterineradmin.Models.KullaniciSil;
import com.example.veterineradmin.Models.KullanicilarModel;
import com.example.veterineradmin.R;
import com.example.veterineradmin.RestApi.ManagerAll;
import com.example.veterineradmin.Utils.ChangeFragments;
import com.example.veterineradmin.Utils.Warnings;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    List<KullanicilarModel> list;
    Context context;
    Activity activity;
    ChangeFragments changeFragments;

    public UserAdapter(List<KullanicilarModel> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        changeFragments = new ChangeFragments(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kullaniciitemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.kullaniciNameText.setText(list.get(position).getKadi().toString());
        holder.userAramaYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ara(list.get(position).getTelefon());
            }
        });
        holder.userPetlerButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments.changeWithParameter(new KullaniciPetlerFragmenti(),list.get(position).getId().toString());
            }
        });
        holder.kullanicilarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kullaniciSilOpenAlert(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView kullaniciNameText;
        Button userPetlerButon, userAramaYap;
        CardView userCardView;
        LinearLayout kullanicilarLayout;
        //itemView ile listviewin her elemanı için layout ile oluşturduğumuz view tanımlaması işlemi gerçekleşir
        public ViewHolder(View itemView) {
            super(itemView);
            kullaniciNameText = itemView.findViewById(R.id.kullaniciNameText);
            userPetlerButon = itemView.findViewById(R.id.userPetList);
            userCardView = itemView.findViewById(R.id.userCardView);
            userAramaYap = itemView.findViewById(R.id.userAramaYap);
            kullanicilarLayout = itemView.findViewById(R.id.kullanicilarLayout);
        }
    }

    public void ara(String numara)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:"+numara));
        activity.startActivity(intent);
    }

    public void kullaniciSilOpenAlert(int position) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.kullanicisillayout, null);
        Button kullaniciSilButon = view.findViewById(R.id.kullaniciSilButon);
        Button kullaniciSilIptal = view.findViewById(R.id.kullaniciSilIptal);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog alertDialog = alert.create();
        kullaniciSilButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kullaniciSil(list.get(position).getId(), position);
                alertDialog.cancel();
            }
        });

        kullaniciSilIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void kullaniciSil(String id, int position)
    {
        Call<KullaniciSil> req = ManagerAll.getInstance().kadiSil(id);
        req.enqueue(new Callback<KullaniciSil>() {
            @Override
            public void onResponse(Call<KullaniciSil> call, Response<KullaniciSil> response) {
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
            public void onFailure(Call<KullaniciSil> call, Throwable t) {
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
