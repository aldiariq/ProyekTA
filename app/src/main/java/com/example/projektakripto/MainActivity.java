package com.example.projektakripto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projektakripto.algoritma.rsa.*;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

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

//                try {
//                    Enkripsi enkripsi = new Enkripsi(email);
//                    Log.d("hasilenkripsi", enkripsi.getBytehasilEnkripsikunciblowfish().toString());
//                } catch (IOException e) {
//                    Toast.makeText(MainActivity.this, "Gagal Mengenkripsi", Toast.LENGTH_SHORT).show();
//                }

                finish();
                Intent pindahkedashboard = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(pindahkedashboard);
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
        txtEmail = (EditText) findViewById(R.id.inputemailMasuk);
        txtPassword = (EditText) findViewById(R.id.inputpasswordMasuk);
        btnMasuk = (Button) findViewById(R.id.btnmasukMasuk);
        btnDaftar = (Button) findViewById(R.id.btndaftarMasuk);
    }
}