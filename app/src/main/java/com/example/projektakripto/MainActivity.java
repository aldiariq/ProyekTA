package com.example.projektakripto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projektakripto.network.DataService;
import com.example.projektakripto.network.ServiceGenerator;
import com.example.projektakripto.response.ResponseMasuk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    public DataService dataService;

    private EditText txtEmail, txtPassword;
    private Button btnMasuk, btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String email, password;
                email = txtEmail.getText().toString().trim();
                password = txtPassword.getText().toString().trim();

                Call<ResponseMasuk> callMasuk = dataService.apiMasuk(email, password);
                callMasuk.enqueue(new Callback<ResponseMasuk>() {
                    @Override
                    public void onResponse(Call<ResponseMasuk> call, Response<ResponseMasuk> response) {
                        if (response.code() == 200){
                            if (response.body().isBerhasil()){
                                finish();
                                Toast.makeText(MainActivity.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                Intent pindahkedashboard = new Intent(MainActivity.this, DashboardActivity.class);
                                startActivity(pindahkedashboard);
                            }else {
                                resetInputan();
                                Toast.makeText(MainActivity.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            resetInputan();
                            Toast.makeText(MainActivity.this, "Gagal Masukdde", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMasuk> call, Throwable t) {
                        resetInputan();
                        Toast.makeText(MainActivity.this, "Gagal Masukdd", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent daftar = new Intent(MainActivity.this, DaftarActivity.class);
                startActivity(daftar);
            }
        });
    }

    private void initView(){
        dataService = (DataService) ServiceGenerator.createBaseService(this, DataService.class);
        txtEmail = (EditText) findViewById(R.id.inputemailMasuk);
        txtPassword = (EditText) findViewById(R.id.inputpasswordMasuk);
        btnMasuk = (Button) findViewById(R.id.btnmasukMasuk);
        btnDaftar = (Button) findViewById(R.id.btndaftarMasuk);
    }

    private void resetInputan(){
        txtEmail.setText("");
        txtPassword.setText("");
    }
}