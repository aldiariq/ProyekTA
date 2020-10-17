package com.example.projektakripto.network;

import com.example.projektakripto.model.FilePengguna;
import com.example.projektakripto.response.ResponseDaftar;
import com.example.projektakripto.response.ResponseDeleteFile;
import com.example.projektakripto.response.ResponseDownloadFile;
import com.example.projektakripto.response.ResponseGantiPasswordPengguna;
import com.example.projektakripto.response.ResponseGetFile;
import com.example.projektakripto.response.ResponseGetKunciRSA;
import com.example.projektakripto.response.ResponseKeluar;
import com.example.projektakripto.response.ResponseMasuk;
import com.example.projektakripto.response.ResponseUploadFile;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
            @Field("password") String password,
            @Field("kunciprivate") String kunciprivate,
            @Field("kuncipublic") String kuncipublic,
            @Field("kuncimodulus") String kuncimodulo
    );

    @FormUrlEncoded
    @POST(Server.API_GANTIPASSWORD)
    Call<ResponseGantiPasswordPengguna> apiGantipassword(
            @Field("id_pengguna") String id_pengguna,
            @Field("password_lama") String passwordlama,
            @Field("password_baru") String passwordbaru
    );

    @GET(Server.API_KELUAR + "{id_pengguna")
    Call<ResponseKeluar> apiKeluar(
            @Path("id_pengguna") String id_pengguna
    );

    //Implementasi Endpoint Algoritma RSA
    @GET(Server.API_GETKUNCIRSA + "{id_pengguna}")
    Call<ResponseGetKunciRSA> apiGetkuncirsa(
            @Path("id_pengguna") String id_pengguna
    );

    //Implementasi Endpoint Operasi File Pengguna
    @Multipart
    @POST(Server.API_UPLOADFILE)
    Call<ResponseUploadFile> apiUploadfile(
            @Part("id_pengguna") RequestBody id_pengguna,
            @Part("kunci_file") RequestBody kunci_file,
            @Part MultipartBody.Part file_enkripsi
    );

    @GET(Server.API_DELETEFILE + "{id_file}")
    Call<ResponseDeleteFile> apiDeletefile(
            @Path("id_file") String id_file
    );

    @GET(Server.API_GETFILE + "{id_pengguna}")
    Call<ResponseGetFile<List<FilePengguna>>> apiGetfile(
            @Path("id_pengguna") String id_pengguna
    );

    @GET(Server.API_DOWNLOADFILE + "{id_pengguna}" + "/" + "{id_file}")
    Call<ResponseDownloadFile> apiDownloadfile(
            @Path("id_pengguna") String id_pengguna,
            @Path("id_file") String id_file
    );
}
