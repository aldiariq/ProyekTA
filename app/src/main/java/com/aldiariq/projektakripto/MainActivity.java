package com.aldiariq.projektakripto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.MasterKeys;

import com.aldiariq.projektakripto.algoritma.rsa.RSA;
import com.aldiariq.projektakripto.model.Pengguna;
import com.aldiariq.projektakripto.network.DataService;
import com.aldiariq.projektakripto.network.ServiceGenerator;
import com.aldiariq.projektakripto.response.ResponseGenerateKunciRSA;
import com.aldiariq.projektakripto.response.ResponseMasuk;
import com.aldiariq.projektakripto.utils.SharedPreferencesEncUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public DataService dataService;

    private EditText txtEmail, txtPassword;
    private Button btnMasuk, btnDaftar;

    private ProgressDialog progressDialog;

    private RSA rsa;

    private SharedPreferencesEncUtils sharedPreferencesEncUtils;
    private String masterKeyAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Memanggil Method Inisialisasi Komponen View
        try {
            initView();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        //Mengecek Apakah Pengguna Sudah Pernah Masuk
        try {
            if (sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, MainActivity.this).getBoolean("sudah_masuk", false)){
                //Destroy Activity, Menampilkan Dialog & Menjalankan DashboardActivity
                finish();
                Toast.makeText(MainActivity.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                Intent pindahkedashboard = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(pindahkedashboard);
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Listener btnMasuk
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(MainActivity.this, "Proses Masuk", "Silahkan Menunggu..");
                //Inisialisasi Inputan Registrasi
                String email, password;
                email = txtEmail.getText().toString().trim();
                password = txtPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Pastikan Semua Inputan Terisi", Toast.LENGTH_SHORT).show();
                }else {
                    //Inisialisasi dan Pemanggilan Method Call Retrofit
                    Call<ResponseMasuk> callMasuk = dataService.apiMasuk(email, password);
                    callMasuk.enqueue(new Callback<ResponseMasuk>() {
                        @Override
                        public void onResponse(Call<ResponseMasuk> call, Response<ResponseMasuk> response) {
                            //Pengecekan Response Code
                            if (response.code() == 200){
                                //Pengecekan Status dari Response Body
                                if (response.body().isBerhasil()){
                                    //Menampung Data Pengguna di Shared Preferences
                                    List<Pengguna> pengguna = (List<Pengguna>) response.body().getPengguna();
                                    try {
                                        sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, MainActivity.this).edit().putBoolean("sudah_masuk", true).apply();
                                        sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, MainActivity.this).edit().putString("token_pengguna", response.body().getToken()).apply();
                                        sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, MainActivity.this).edit().putString("id_pengguna", pengguna.get(0).getId_pengguna()).apply();
                                        sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, MainActivity.this).edit().putString("email_pengguna", pengguna.get(0).getEmail_pengguna()).apply();
                                        sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, MainActivity.this).edit().putString("nama_pengguna", pengguna.get(0).getNama_pengguna()).apply();
                                    } catch (GeneralSecurityException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    Call<ResponseGenerateKunciRSA> callGeneratekuncirsa = dataService.apiGeneratekuncirsa(response.body().getToken(), pengguna.get(0).getId_pengguna(), rsa.getPublicKey(), rsa.getModulus());
                                    callGeneratekuncirsa.enqueue(new Callback<ResponseGenerateKunciRSA>() {
                                        @Override
                                        public void onResponse(Call<ResponseGenerateKunciRSA> call, Response<ResponseGenerateKunciRSA> response) {
                                            try {
                                                sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, MainActivity.this).edit().putString("kunci_private", rsa.getPrivatekey()).apply();
                                            } catch (GeneralSecurityException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseGenerateKunciRSA> call, Throwable t) {
                                            //Memanggil Method Reset Inputan & Menampilkan Dialog
                                            resetInputan();
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Gagal Menyimpan Kunci RSA", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    //Destroy Activity, Menampilkan Dialog & Menjalankan DashboardActivity
                                    finish();
                                    Toast.makeText(MainActivity.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                    Intent pindahkedashboard = new Intent(MainActivity.this, DashboardActivity.class);
                                    startActivity(pindahkedashboard);
                                }else {
                                    //Memanggil Method Reset Inputan & Menampilkan Dialog
                                    resetInputan();
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                //Memanggil Method Reset Inputan & Menampilkan Dialog
                                resetInputan();
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Gagal Masuk, Pastikan Password Benar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseMasuk> call, Throwable t) {
                            //Memanggil Method Reset Inputan & Menampilkan Dialog
                            resetInputan();
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Gagal Masuk, Pastikan Terkoneksi Internet 1", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //Listener btnDaftar
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Destroy Activity & Menjalankan DaftarActivity
                finish();
                Intent daftar = new Intent(MainActivity.this, DaftarActivity.class);
                startActivity(daftar);
            }
        });
    }

    //Inisialisasi Komponen View & Data Service
    private void initView() throws GeneralSecurityException {
        sharedPreferencesEncUtils = new SharedPreferencesEncUtils();
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataService = (DataService) ServiceGenerator.createBaseService(this, DataService.class);
        txtEmail = (EditText) findViewById(R.id.inputemailMasuk);
        txtPassword = (EditText) findViewById(R.id.inputpasswordMasuk);
        btnMasuk = (Button) findViewById(R.id.btnmasukMasuk);
        btnDaftar = (Button) findViewById(R.id.btndaftarMasuk);
        rsa = new RSA(512);
    }

    //Method Untuk Mengosongkan Field Inputan
    private void resetInputan(){
        txtEmail.setText("");
        txtPassword.setText("");
    }
}