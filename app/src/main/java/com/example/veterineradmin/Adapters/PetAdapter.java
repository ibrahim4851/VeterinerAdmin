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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.veterineradmin.Fragments.KullaniciPetlerFragmenti;
import com.example.veterineradmin.Models.AsiEkle;
import com.example.veterineradmin.Models.KullaniciSil;
import com.example.veterineradmin.Models.KullanicilarModel;
import com.example.veterineradmin.Models.PetModel;
import com.example.veterineradmin.Models.PetSil;
import com.example.veterineradmin.R;
import com.example.veterineradmin.RestApi.ManagerAll;
import com.example.veterineradmin.Utils.ChangeFragments;
import com.example.veterineradmin.Utils.Warnings;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    List<PetModel> list;
    Context context;
    Activity activity;
    ChangeFragments changeFragments;
    String musid;
    String tarih = "", formatliTarih="";

    public PetAdapter(List<PetModel> list, Context context, Activity activity, String musid) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        changeFragments = new ChangeFragments(context);
        this.musid = musid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userpetitemlayout/*<-bu layouttan gelir*/, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.petNameText.setText(list.get(position).getPetisim().toString());
        holder.petBilgiText.setText(list.get(position).getPetisim().toString()+"'in kaydını silmek için buraya tıklayın.");
        holder.petBilgiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petSilOpenAlert(position);
            }
        });
        Picasso.get().load(list.get(position).getPetresim().toString()).resize(200, 200).into(holder.petImage);
        holder.petAsiEkleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAsiEkle(list.get(position).getPetid().toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView petBilgiText, petNameText;
        LinearLayout petAsiEkleLayout;
        ImageView petImage;

        //itemView ile listviewin her elemanı için layout ile oluşturduğumuz view tanımlaması işlemi gerçekleşir
        public ViewHolder(View itemView) {
            super(itemView);
            petBilgiText = itemView.findViewById(R.id.petBilgiText);
            petNameText = itemView.findViewById(R.id.petNameText);
            petAsiEkleLayout = itemView.findViewById(R.id.petAsiEkleLayout);
            petImage = itemView.findViewById(R.id.petImage);
        }
    }

    public void addAsiEkle(String petId) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.asieklelayout, null);
        CalendarView calendarView = view.findViewById(R.id.asiEkleTakvim);
        Button asiEkleButon = view.findViewById(R.id.asiEkleButon);
        EditText asiEkleAsiName = view.findViewById(R.id.asiEkleAsiName);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog alertDialog = alert.create();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                tarih = dayOfMonth + "/" + (month + 1) + "/" + year;
                try {
                    Date date = inputFormat.parse(tarih);
                    formatliTarih = format.format(date).toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        asiEkleButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!formatliTarih.equals("") && !asiEkleAsiName.getText().toString().equals("")) {
                    addAsi(musid, petId, asiEkleAsiName.getText().toString(), formatliTarih, alertDialog);
                } else {
                    Toast.makeText(context, "Tarih seçiniz veya aşı ismi giriniz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }

    public void addAsi(String musid, String petid, String asiName, String tarih, AlertDialog alertDialog) {
        Call<AsiEkle> req = ManagerAll.getInstance().addAsi(musid, petid, asiName, tarih);
        req.enqueue(new Callback<AsiEkle>() {
            @Override
            public void onResponse(Call<AsiEkle> call, Response<AsiEkle> response) {
                    alertDialog.cancel();
                    Toast.makeText(context, response.body().getText().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AsiEkle> call, Throwable t) {
                Toast.makeText(context, Warnings.internetProblemText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void petSilOpenAlert(int position) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.petsillayout, null);
        Button petSilButon = view.findViewById(R.id.petSilButon);
        Button petSilIptal = view.findViewById(R.id.petSilIptal);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(true);
        AlertDialog alertDialog = alert.create();
        petSilButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petSil(list.get(position).getPetid().toString(), position);
                alertDialog.cancel();
            }
        });

        petSilIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void petSil(String id, int position)
    {
        Call<PetSil> req = ManagerAll.getInstance().petSil(id);
        req.enqueue(new Callback<PetSil>() {
            @Override
            public void onResponse(Call<PetSil> call, Response<PetSil> response) {
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
            public void onFailure(Call<PetSil> call, Throwable t) {
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
