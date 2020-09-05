package com.example.projektakripto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DaftarActivity extends AppCompatActivity {

    private EditText txtEmail, txtNamalengkap, txtNohp, txtPassword1, txtPassword2;
    private Button btnDaftar, btnMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        initView();

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, namalengkap, nohp, password1, password2;
                email = txtEmail.getText().toString();
                namalengkap = txtNamalengkap.getText().toString();
                nohp = txtNohp.getText().toString();
                password1 = txtPassword1.getText().toString();
                password2 = txtPassword2.getText().toString();

                finish();
                Intent masuk = new Intent(DaftarActivity.this, MainActivity.class);
                startActivity(masuk);
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent masuk = new Intent(DaftarActivity.this, MainActivity.class);
                startActivity(masuk);
            }
        });
    }

    private void initView(){
        txtEmail = (EditText) findViewById(R.id.inputemailDaftar);
        txtNamalengkap = (EditText) findViewById(R.id.inputnamaDaftar);
        txtNohp = (EditText) findViewById(R.id.inputnohpDaftar);
        txtPassword1 = (EditText) findViewById(R.id.inputpasswordDaftar);
        txtPassword2 = (EditText) findViewById(R.id.inputpassword2Daftar);
        btnDaftar = (Button) findViewById(R.id.btndaftarDaftar);
        btnMasuk = (Button) findViewById(R.id.btnmasukDaftar);
    }
}