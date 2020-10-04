package com.example.projektakripto;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projektakripto.algoritma.blowfish.Blowfish;
import com.example.projektakripto.network.DataService;
import com.example.projektakripto.network.ServiceGenerator;
import com.example.projektakripto.response.ResponseUploadFile;
import com.example.projektakripto.utils.FileUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ImageView imgpilihfileformupload;
    private TextView txtlokasifileformupload;
    private EditText etpasswordblowfishformupload;

    public DataService dataService;

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private static final String LOG_TAG = "AndroidExample";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inisialisasi Komponen Halaman Upload File
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DashboardActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.form_upload_file, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);

                dialog.setPositiveButton("Upload", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataService = (DataService) ServiceGenerator.createBaseService(DashboardActivity.this, DataService.class);
                        String lokasifileinput = txtlokasifileformupload.getText().toString();
                        String lokasifileoutput = txtlokasifileformupload.getText().toString() + ".enc";
                        String passwordblowfish = etpasswordblowfishformupload.getText().toString();

                        Blowfish enkripsiblowfish = new Blowfish(passwordblowfish);
                        enkripsiblowfish.encrypt(lokasifileinput, lokasifileoutput);

                        File filehasilenkripsi = new File(lokasifileoutput);
                        RequestBody requestBodynamafile = RequestBody.create(MediaType.parse("text/plain"), filehasilenkripsi.getName());

//                        Call<ResponseUploadFile> callUpload = dataService.apiUploadfile(requestBodynamafile, );
//                        callUpload.enqueue(new Callback<ResponseUploadFile>() {
//                            @Override
//                            public void onResponse(Call<ResponseUploadFile> call, Response<ResponseUploadFile> response) {
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseUploadFile> call, Throwable t) {
//                                Toast.makeText(DashboardActivity.this, "Gagal Mengupload File", Toast.LENGTH_SHORT).show();
//                            }
//                        });

                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                imgpilihfileformupload = dialogView.findViewById(R.id.imgpilihfileformupload);
                txtlokasifileformupload = dialogView.findViewById(R.id.txtlokasifileformupload);
                etpasswordblowfishformupload = dialogView.findViewById(R.id.etpasswordblowfishformupload);

                imgpilihfileformupload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deteksiPermissionandroid();
                    }
                });
            }
        });

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

        //Listener Navigasi Untuk Menyembunyikan Floating Action Button di Halaman Upload File
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int idMenu = destination.getId();
                switch (idMenu){
                    case R.id.nav_penyimpanan:
                        fab.setVisibility(View.VISIBLE);
                        break;
                    default:
                        fab.setVisibility(View.GONE);
                        break;
                }
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

    private void deteksiPermissionandroid()  {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23

            // Check if we have read storage permission
            int pemissionread = ActivityCompat.checkSelfPermission(DashboardActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (pemissionread != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }

            //check if we have write storage permission
            int permissionwrite = ActivityCompat.checkSelfPermission(DashboardActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionwrite != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }
        }
        this.pilihFile();
    }

    private void pilihFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("application/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i( LOG_TAG,"Permission granted!");
                    Toast.makeText(DashboardActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();

                    this.pilihFile();
                }
                // Cancelled or denied.
                else {
                    Log.i(LOG_TAG,"Permission denied!");
                    Toast.makeText(DashboardActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        Uri fileUri = data.getData();
                        Log.i(LOG_TAG, "Uri: " + fileUri);

                        String filePath = null;
                        try {
                            filePath = FileUtils.getPath(DashboardActivity.this,fileUri);
                        } catch (Exception e) {
                            Log.e(LOG_TAG,"Error: " + e);
                            Toast.makeText(DashboardActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.txtlokasifileformupload.setText(filePath);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}