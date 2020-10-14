package com.example.projektakripto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inisialisasi Komponen Halaman Upload File
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();

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
                //Destroy Activity & Menjalankan Activity MainActivity(Login)
                editor.putBoolean("sudah_masuk", false);
                editor.putString("id_pengguna", "");
                editor.putString("email_pengguna", "");
                editor.putString("nohp_pengguna", "");
                editor.apply();
                finish();
                Intent pindahkehalamanmasuk = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(pindahkehalamanmasuk);
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