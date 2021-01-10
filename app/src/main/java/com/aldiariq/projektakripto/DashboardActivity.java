package com.aldiariq.projektakripto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.security.crypto.MasterKeys;

import com.aldiariq.projektakripto.network.DataService;
import com.aldiariq.projektakripto.network.ServiceGenerator;
import com.aldiariq.projektakripto.response.ResponseKeluar;
import com.aldiariq.projektakripto.utils.SharedPreferencesEncUtils;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private DataService dataService;

    private SharedPreferencesEncUtils sharedPreferencesEncUtils;
    private String masterKeyAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inisialisasi Komponen Halaman Upload File
        sharedPreferencesEncUtils = new SharedPreferencesEncUtils();
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

        dataService = (DataService) ServiceGenerator.createBaseService(this, DataService.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_halaman_utama, R.id.nav_penyimpanan, R.id.nav_ganti_password, R.id.nav_tentangaplikasi, R.id.nav_keluar)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Setting Email Pengguna di Navigation Drawer
        View headerView = navigationView.getHeaderView(0);
        TextView txtusernamenavigation = (TextView) headerView.findViewById(R.id.txtusernamenavigation);
        try {
            txtusernamenavigation.setText("Username : " + sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, DashboardActivity.this).getString("nama_pengguna", ""));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Setting Warna Icon Navigasi Menjadi Null
        navigationView.setItemIconTintList(null);

        //Listener Menu Navigasi Keluar
        navigationView.getMenu().findItem(R.id.nav_keluar).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DashboardActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_keluar, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);

                dialog.setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String token_pengguna = "";
                        String id_pengguna = "";

                        try {
                            token_pengguna = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, DashboardActivity.this).getString("token_pengguna", "");
                            id_pengguna = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, DashboardActivity.this).getString("id_pengguna", "");
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Menjalankan Endpoint Keluar Pengguna
                        Call<ResponseKeluar> callKeluarpengguna = dataService.apiKeluar(token_pengguna, id_pengguna);
                        callKeluarpengguna.enqueue(new Callback<ResponseKeluar>() {
                            @Override
                            public void onResponse(Call<ResponseKeluar> call, Response<ResponseKeluar> response) {
                                //Pengecekan Response Code
                                if (response.code() == 200){
                                    if (response.body().isBerhasil()){
                                        //Destroy Activity & Menjalankan Activity MainActivity(Login)
                                        try {
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, DashboardActivity.this).edit().putString("email_pengguna", "").apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, DashboardActivity.this).edit().putString("token_pengguna", "").apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, DashboardActivity.this).edit().putBoolean("sudah_masuk", false).apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, DashboardActivity.this).edit().putString("nama_pengguna", "").apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, DashboardActivity.this).edit().putString("kunci_private", "").apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, DashboardActivity.this).edit().putString("id_pengguna", "").apply();
                                        } catch (GeneralSecurityException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(DashboardActivity.this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent pindahkehalamanmasuk = new Intent(DashboardActivity.this, MainActivity.class);
                                        startActivity(pindahkehalamanmasuk);
                                    }else {
                                        Toast.makeText(DashboardActivity.this, "Gagal Keluar", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(DashboardActivity.this, "Gagal Keluar", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseKeluar> call, Throwable t) {
                                Toast.makeText(DashboardActivity.this, "Gagal Keluar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}