package com.aldiariq.projektakripto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.aldiariq.projektakripto.network.DataService;
import com.aldiariq.projektakripto.network.ServiceGenerator;
import com.aldiariq.projektakripto.response.ResponseKeluar;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    private DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inisialisasi Komponen Halaman Upload File
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();

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
        txtusernamenavigation.setText("Username : " + preference.getString("email_pengguna", null));

        //Setting Warna Icon Navigasi Menjadi Null
        navigationView.setItemIconTintList(null);

        //Listener Menu Navigasi Keluar
        navigationView.getMenu().findItem(R.id.nav_keluar).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                //Menjalankan Endpoint Keluar Pengguna
                Call<ResponseKeluar> callKeluarpengguna = dataService.apiKeluar(preference.getString("token_pengguna", ""), preference.getString("id_pengguna", ""));
                callKeluarpengguna.enqueue(new Callback<ResponseKeluar>() {
                    @Override
                    public void onResponse(Call<ResponseKeluar> call, Response<ResponseKeluar> response) {
                        //Pengecekan Response Code
                        if (response.code() == 200){
                            if (response.body().isBerhasil()){
                                //Destroy Activity & Menjalankan Activity MainActivity(Login)
                                editor.putString("email_pengguna", "");
                                editor.putString("token_pengguna", "");
                                editor.putBoolean("sudah_masuk", false);
                                editor.putString("nama_pengguna", "");
                                editor.putString("kunci_private", "");
                                editor.putString("id_pengguna", "");
                                editor.apply();
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