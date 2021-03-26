package com.example.veterineradmin.RestApi;

import java.util.List;

import com.example.veterineradmin.Models.*;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface RestApi {

    @GET("kampanyaidli.php")
    Call<List<KampanyaModel>> getKampanya();

    @FormUrlEncoded
    @POST("kampanyaekle.php")
    Call<KampanyaEkleModel> addKampanya(@Field("baslik") String baslik,
                                        @Field("text") String text,
                                        @Field("resim") String resim);

    @FormUrlEncoded
    @POST("kampanyasil.php")
    Call<KampanyaSilModel> kampanyaSil(@Field("id") String petid);

    @FormUrlEncoded
    @POST("veterinerasitakip.php")
    Call<List<PetAsiTakipModel>> getPetAsiTakip(@Field("tarih") String tarih);

    @FormUrlEncoded
    @POST("asionayla.php")
    Call<AsiOnaylaModel> asiOnayla(@Field("id") String id);

    @FormUrlEncoded
    @POST("asiiptal.php")
    Call<AsiOnaylaModel> asiIptal(@Field("id") String id);

    @GET("sorular.php")
    Call<List<SoruModel>> getSoru();

    @FormUrlEncoded
    @POST("cevapla.php")
    Call<CevaplaModel> cevapla(@Field("musid") String musid,
                               @Field("soruid") String soruid,
                               @Field("text") String text);

    @GET("kullanicilar.php")
    Call<List<KullanicilarModel>> getKullanicilar();

    @FormUrlEncoded
    @POST("petlerim.php")
    Call<List<PetModel>> getPets(@Field("musid") String id);

    @FormUrlEncoded
    @POST("petekle.php")
    Call<PetEkle> petEkle(@Field("musid") String musid,
                          @Field("isim") String isim,
                               @Field("tur") String tur,
                               @Field("cins") String cins,
                               @Field("resim") String resim);

    @FormUrlEncoded
    @POST("asiekle.php")
    Call<AsiEkle> asiEkle(@Field("musid") String musid,
                          @Field("petid") String petid,
                          @Field("name") String name,
                          @Field("tarih") String tarih);

    @FormUrlEncoded
    @POST("kullanicisil.php")
    Call<KullaniciSil> kadiSil(@Field("id") String id);

    @FormUrlEncoded
    @POST("petsil.php")
    Call<PetSil> petSil(@Field("id") String id);

}
