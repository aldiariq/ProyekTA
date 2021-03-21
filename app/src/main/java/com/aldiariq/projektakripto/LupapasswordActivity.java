package com.aldiariq.projektakripto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aldiariq.projektakripto.network.DataService;
import com.aldiariq.projektakripto.network.ServiceGenerator;
import com.aldiariq.projektakripto.response.ResponseLupaPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LupapasswordActivity extends AppCompatActivity {

    public DataService dataService;

    private EditText txtEmail;
    private Button btnAturulang;
    private TextView txtmasukLupapassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupapassword);

        //Memanggil Method Inisialisasi Komponen View
        initView();

        btnAturulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(LupapasswordActivity.this, "Proses Permintaan Reset Password", "Silahkan Menunggu..");
                //Inisialisasi Inputan Lupa Password
                String email;
                email = txtEmail.getText().toString();

                if(email.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(LupapasswordActivity.this, "Pastikan Semua Inputan Terisi", Toast.LENGTH_SHORT).show();
                }else {
                    Call<ResponseLupaPassword> lupaPasswordCall = dataService.apiLupapassword(email);
                    lupaPasswordCall.enqueue(new Callback<ResponseLupaPassword>() {
                        @Override
                        public void onResponse(Call<ResponseLupaPassword> call, Response<ResponseLupaPassword> response) {
                            //Pengecekan Response Code
                            if (response.code() == 200){
                                //Pengecekan Status dari Response Body
                                if (response.body().isBerhasil()){
                                    Toast.makeText(LupapasswordActivity.this, "Berhasil Mereset Password", Toast.LENGTH_SHORT).show();
                                    //Destroy Activity & Menjalankan MainActivity(Login)
                                    finish();
                                    Intent masuk = new Intent(LupapasswordActivity.this, MainActivity.class);
                                    startActivity(masuk);
                                }else {
                                    //Memanggil Method Reset Inputan & Menampilkan Dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(LupapasswordActivity.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                //Memanggil Method Reset Inputan & Menampilkan Dialog
                                txtEmail.getText().clear();
                                progressDialog.dismiss();
                                Toast.makeText(LupapasswordActivity.this, "Gagal Mereset Password, Pastikan Terkoneksi Internet 1", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseLupaPassword> call, Throwable t) {
                            txtEmail.getText().clear();
                            progressDialog.dismiss();
                            Toast.makeText(LupapasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(LupapasswordActivity.this, "Gagal Mereset Password, Pastikan Terkoneksi Internet 2", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        txtmasukLupapassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent masuk = new Intent(LupapasswordActivity.this, MainActivity.class);
                startActivity(masuk);
            }
        });
    }

    private void initView(){
        dataService = (DataService) ServiceGenerator.createBaseService(this, DataService.class);
        txtEmail = (EditText) findViewById(R.id.inputemailLupapassword);
        btnAturulang = (Button) findViewById(R.id.btnaturulangLupapassword);
        txtmasukLupapassword = (TextView) findViewById(R.id.txtmasukLupapassword);
    }
}