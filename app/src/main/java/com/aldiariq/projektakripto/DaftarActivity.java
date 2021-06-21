 package com.aldiariq.projektakripto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aldiariq.projektakripto.network.DataService;
import com.aldiariq.projektakripto.network.ServiceGenerator;
import com.aldiariq.projektakripto.response.ResponseDaftar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {

    public DataService dataService;

    private EditText txtEmail, txtNama, txtPassword1, txtPassword2;
    private Button btnDaftar, btnMasuk;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        //Memanggil Method Inisialisasi Komponen View
        initView();

        //Listener btnDaftar
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(DaftarActivity.this, "Proses Daftar", "Silahkan Menunggu..");
                //Inisialisasi Inputan Registrasi
                String email, nama, password1, password2;
                email = txtEmail.getText().toString();
                nama = txtNama.getText().toString();
                password1 = txtPassword1.getText().toString();
                password2 = txtPassword2.getText().toString();

                if(email.isEmpty() || nama.isEmpty() || password1.isEmpty() || password2.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(DaftarActivity.this, "Pastikan Semua Inputan Terisi", Toast.LENGTH_SHORT).show();
                }else {
                    if (password1.equals(password2)){
                        Call<ResponseDaftar> callDaftar = dataService.apiDaftar(email, nama, password1);
                        callDaftar.enqueue(new Callback<ResponseDaftar>() {
                            @Override
                            public void onResponse(Call<ResponseDaftar> call, Response<ResponseDaftar> response) {
                                //Pengecekan Response Code
                                if (response.code() == 200){
                                    //Pengecekan Status dari Response Body
                                    if (response.body().isBerhasil()){
                                        Toast.makeText(DaftarActivity.this, "Berhasil Mendaftarkan Akun", Toast.LENGTH_SHORT).show();
                                        //Destroy Activity & Menjalankan MainActivity(Login)
                                        finish();
                                        Intent masuk = new Intent(DaftarActivity.this, MainActivity.class);
                                        startActivity(masuk);
                                    }else {
                                        //Memanggil Method Reset Inputan & Menampilkan Dialog
                                        progressDialog.dismiss();
                                        Toast.makeText(DaftarActivity.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    //Memanggil Method Reset Inputan & Menampilkan Dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(DaftarActivity.this, "Gagal Mendaftarkan Akun, Pastikan Email Belum Terdaftar", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseDaftar> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(DaftarActivity.this, "Gagal Mendaftarkan Akun, Pastikan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(DaftarActivity.this, "Pastikan Password Sama", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Listener btnMasuk
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Destroy Activity & Menjalankan MainActivity(Login)
                finish();
                Intent masuk = new Intent(DaftarActivity.this, MainActivity.class);
                startActivity(masuk);
            }
        });
    }

    //Inisialisasi Komponen View
    private void initView(){
        dataService = (DataService) ServiceGenerator.createBaseService(this, DataService.class);
        txtEmail = (EditText) findViewById(R.id.inputemailDaftar);
        txtNama = (EditText) findViewById(R.id.inputnamaDaftar);
        txtPassword1 = (EditText) findViewById(R.id.inputpasswordDaftar);
        txtPassword2 = (EditText) findViewById(R.id.inputpassword2Daftar);
        btnDaftar = (Button) findViewById(R.id.btndaftarDaftar);
        btnMasuk = (Button) findViewById(R.id.btnmasukDaftar);
    }
}