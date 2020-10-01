package com.example.projektakripto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projektakripto.algoritma.rsa.RSA;
import com.example.projektakripto.network.DataService;
import com.example.projektakripto.network.ServiceGenerator;
import com.example.projektakripto.response.ResponseDaftar;
import com.example.projektakripto.response.ResponseMasuk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {

    public DataService dataService;

    private EditText txtEmail, txtNamalengkap, txtNohp, txtPassword1, txtPassword2;
    private Button btnDaftar, btnMasuk;

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
                //Inisialisasi Inputan Registrasi

                String email, namalengkap, nohp, password1, password2;
                email = txtEmail.getText().toString();
                nohp = txtNohp.getText().toString();
                password1 = txtPassword1.getText().toString();
                password2 = txtPassword2.getText().toString();

                RSA daftarRSA = new RSA(128);

                if (password1.equals(password2)){
                    Call<ResponseDaftar> callDaftar = dataService.apiDaftar(email, nohp, password1, daftarRSA.getPrivatekey(), daftarRSA.getPublicKey(), daftarRSA.getModulus());
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
                                    Toast.makeText(DaftarActivity.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                //Memanggil Method Reset Inputan & Menampilkan Dialog
                                Toast.makeText(DaftarActivity.this, "Gagal Masukdde", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseDaftar> call, Throwable t) {
                            Toast.makeText(DaftarActivity.this, "Gagal Mendaftarkan Akun", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(DaftarActivity.this, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
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
        txtNamalengkap = (EditText) findViewById(R.id.inputnamaDaftar);
        txtNohp = (EditText) findViewById(R.id.inputnohpDaftar);
        txtPassword1 = (EditText) findViewById(R.id.inputpasswordDaftar);
        txtPassword2 = (EditText) findViewById(R.id.inputpassword2Daftar);
        btnDaftar = (Button) findViewById(R.id.btndaftarDaftar);
        btnMasuk = (Button) findViewById(R.id.btnmasukDaftar);
    }
}