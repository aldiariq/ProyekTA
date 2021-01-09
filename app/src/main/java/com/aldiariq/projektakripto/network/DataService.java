package com.aldiariq.projektakripto.network;

import com.aldiariq.projektakripto.model.FilePengguna;
import com.aldiariq.projektakripto.response.ResponseDaftar;
import com.aldiariq.projektakripto.response.ResponseDeleteFile;
import com.aldiariq.projektakripto.response.ResponseDownloadFile;
import com.aldiariq.projektakripto.response.ResponseGantiPasswordPengguna;
import com.aldiariq.projektakripto.response.ResponseGenerateKunciRSA;
import com.aldiariq.projektakripto.response.ResponseGetFile;
import com.aldiariq.projektakripto.response.ResponseGetKunciRSA;
import com.aldiariq.projektakripto.response.ResponseKeluar;
import com.aldiariq.projektakripto.response.ResponseMasuk;
import com.aldiariq.projektakripto.response.ResponseUploadFile;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DataService {
    //Implementasi Endpoint Operasi Pengguna
    @FormUrlEncoded
    @POST(Server.API_MASUK)
    Call<ResponseMasuk> apiMasuk(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(Server.API_DAFTAR)
    Call<ResponseDaftar> apiDaftar(
            @Field("email") String email,
            @Field("nama") String nama,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(Server.API_GANTIPASSWORD)
    Call<ResponseGantiPasswordPengguna> apiGantipassword(
            @Header("Authorization") String Token,
            @Field("id_pengguna") String id_pengguna,
            @Field("password_lama") String passwordlama,
            @Field("password_baru") String passwordbaru
    );

    @GET(Server.API_KELUAR + "{id_pengguna}")
    Call<ResponseKeluar> apiKeluar(
            @Header("Authorization") String Token,
            @Path("id_pengguna") String id_pengguna
    );

    //Implementasi Endpoint Algoritma RSA
    @FormUrlEncoded
    @POST(Server.API_GENERATEKUNCIRSA)
    Call<ResponseGenerateKunciRSA> apiGeneratekuncirsa(
            @Header("Authorization") String Token,
            @Field("id_pengguna") String id_pengguna,
            @Field("kunci_public") String kunci_public,
            @Field("kunci_modulus") String kunci_modulus
    );
    @GET(Server.API_GETKUNCIRSA + "{id_pengguna}")
    Call<ResponseGetKunciRSA> apiGetkuncirsa(
            @Header("Authorization") String Token,
            @Path("id_pengguna") String id_pengguna
    );

    //Implementasi Endpoint Operasi File Pengguna
    @Multipart
    @POST(Server.API_UPLOADFILE)
    Call<ResponseUploadFile> apiUploadfile(
            @Header("Authorization") String Token,
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("kunci_file") RequestBody kunci_file,
            @Part MultipartBody.Part file_enkripsi
    );

    @FormUrlEncoded
    @POST(Server.API_DELETEFILE)
    Call<ResponseDeleteFile> apiDeletefile(
            @Header("Authorization") String Token,
            @Field("id_file") String id_file,
            @Field("nama_file") String nama_file,
            @Field("id_pengguna") String id_pengguna
    );

    @GET(Server.API_GETFILE + "{id_pengguna}")
    Call<ResponseGetFile<List<FilePengguna>>> apiGetfile(
            @Header("Authorization") String Token,
            @Path("id_pengguna") String id_pengguna
    );

    @GET(Server.API_DOWNLOADFILE + "{id_pengguna}" + "/" + "{id_file}")
    Call<ResponseDownloadFile> apiDownloadfile(
            @Header("Authorization") String Token,
            @Path("id_pengguna") String id_pengguna,
            @Path("id_file") String id_file
    );
}
