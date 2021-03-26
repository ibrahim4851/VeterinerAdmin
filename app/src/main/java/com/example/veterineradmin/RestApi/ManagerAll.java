package com.example.veterineradmin.RestApi;

import java.util.List;

import com.example.veterineradmin.Models.*;
import retrofit2.Call;

public class ManagerAll extends BaseManager{
    private  static ManagerAll ourInstance = new ManagerAll();

    public  static synchronized ManagerAll getInstance()
    {
        return  ourInstance;
    }

    public Call<List<KampanyaModel>> getKampanya()
    {
        Call<List<KampanyaModel>> x = getRestApi().getKampanya();
        return  x ;
    }

    public Call<KampanyaEkleModel> addKampanya(String baslik, String icerik, String imageString)
    {
        Call<KampanyaEkleModel> x = getRestApi().addKampanya(baslik, icerik, imageString);
        return  x ;
    }

    public Call<KampanyaSilModel> kampanyaSil(String kamid)
    {
        Call<KampanyaSilModel> x = getRestApi().kampanyaSil(kamid);
        return  x ;
    }

    public Call<List<PetAsiTakipModel>> getPetAsiTakip(String tarih)
    {
        Call<List<PetAsiTakipModel>> x = getRestApi().getPetAsiTakip(tarih);
        return  x ;
    }

    public Call<AsiOnaylaModel> asiOnayla(String petid)
    {
        Call<AsiOnaylaModel> x = getRestApi().asiOnayla(petid);
        return  x ;
    }

    public Call<AsiOnaylaModel> asiIptal(String petid)
    {
        Call<AsiOnaylaModel> x = getRestApi().asiIptal(petid);
        return  x ;
    }

    public Call<List<SoruModel>> getSoru()
    {
        Call<List<SoruModel>> x = getRestApi().getSoru();
        return  x ;
    }

    public Call<CevaplaModel> cevapla(String musid, String soruid, String text)
    {
        Call<CevaplaModel> x = getRestApi().cevapla(musid, soruid, text);
        return  x ;
    }

    public Call<List<KullanicilarModel>> getKullanicilar()
    {
        Call<List<KullanicilarModel>> x = getRestApi().getKullanicilar();
        return  x ;
    }

    public Call<List<PetModel>> getPets(String id)
    {
        Call<List<PetModel>> x = getRestApi().getPets(id);
        return  x ;
    }

    public Call<PetEkle> petEkle(String musid, String isim, String tur, String cins, String resim)
    {
        Call<PetEkle> x = getRestApi().petEkle(musid, isim, tur, cins, resim);
        return  x ;
    }

    public Call<AsiEkle> addAsi(String musid, String petid, String name, String tarih)
    {
        Call<AsiEkle> x = getRestApi().asiEkle(musid, petid, name, tarih);
        return  x ;
    }

    public Call<KullaniciSil> kadiSil(String id)
    {
        Call<KullaniciSil> x = getRestApi().kadiSil(id);
        return  x ;
    }

    public Call<PetSil> petSil(String id)
    {
        Call<PetSil> x = getRestApi().petSil(id);
        return  x ;
    }

}
