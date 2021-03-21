package com.aldiariq.projektakripto.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.security.crypto.MasterKeys;

import com.aldiariq.projektakripto.MainActivity;
import com.aldiariq.projektakripto.R;
import com.aldiariq.projektakripto.network.DataService;
import com.aldiariq.projektakripto.network.ServiceGenerator;
import com.aldiariq.projektakripto.response.ResponseKeluar;
import com.aldiariq.projektakripto.utils.SharedPreferencesEncUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private SharedPreferencesEncUtils sharedPreferencesEncUtils;
    private String masterKeyAlias;

    private TextView tvnamahalaman;
    private CardView cvpenyimpanan, cvtentangaplikasi, cvgantipassword, cvkeluar;

    private DataService dataService;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);

        String nama_pengguna = "";

        try {
            nama_pengguna = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).getString("nama_pengguna", "");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvnamahalaman.setText("Selamat Datang, " + nama_pengguna);

        //Listener Click CardView Penyimpanan
        cvpenyimpanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Halaman Penyimpanan", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigate(R.id.action_nav_halaman_utama_to_nav_penyimpanan);
            }
        });

        //Listener Click CardView Tentang Aplikasi
        cvtentangaplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Halaman Tentang Aplikasi", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigate(R.id.action_nav_halaman_utama_to_nav_tentangaplikasi);
            }
        });

        //Listener Click CardView Ganti Password
        cvgantipassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Halaman Ganti Password", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigate(R.id.action_nav_halaman_utama_to_nav_ganti_password);
            }
        });

        //Listener Click CardView Keluar Aplikasi
        String finalMasterKeyAlias = masterKeyAlias;
        cvkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_keluar, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Menjalankan Endpoint Keluar Pengguna
                        String token_pengguna = "";
                        String id_pengguna = "";

                        try {
                            token_pengguna = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).getString("token_pengguna", "");
                            id_pengguna = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).getString("id_pengguna", "");
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Call<ResponseKeluar> callKeluarpengguna = dataService.apiKeluar(token_pengguna, id_pengguna);
                        callKeluarpengguna.enqueue(new Callback<ResponseKeluar>() {
                            @Override
                            public void onResponse(Call<ResponseKeluar> call, Response<ResponseKeluar> response) {
                                //Pengecekan Response Code
                                if (response.code() == 200){
                                    if (response.body().isBerhasil()){
                                        //Destroy Activity & Menjalankan Activity MainActivity(Login)
                                        try {
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(finalMasterKeyAlias, getContext()).edit().putString("email_pengguna", "").apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(finalMasterKeyAlias, getContext()).edit().putString("token_pengguna", "").apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(finalMasterKeyAlias, getContext()).edit().putBoolean("sudah_masuk", false).apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(finalMasterKeyAlias, getContext()).edit().putString("nama_pengguna", "").apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(finalMasterKeyAlias, getContext()).edit().putString("kunci_private", "").apply();
                                            sharedPreferencesEncUtils.getEncryptedSharedPreferences(finalMasterKeyAlias, getContext()).edit().putString("id_pengguna", "").apply();
                                        } catch (GeneralSecurityException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(getActivity(), "Berhasil Keluar", Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                        Intent pindahkehalamanmasuk = new Intent(getActivity(), MainActivity.class);
                                        startActivity(pindahkehalamanmasuk);
                                    }else {
                                        Toast.makeText(getActivity(), response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(getActivity(), response.body().getPesan(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseKeluar> call, Throwable t) {
                                Toast.makeText(getActivity(), "Gagal Keluar", Toast.LENGTH_SHORT).show();
                                Log.i("RESPONKELUAR", "GAGAL");
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
            }
        });

        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        sharedPreferencesEncUtils = new SharedPreferencesEncUtils();
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        dataService = (DataService) ServiceGenerator.createBaseService(getActivity(), DataService.class);
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_home);
        cvpenyimpanan = (CardView) view.findViewById(R.id.cvpenyimpanan_halaman_utama);
        cvtentangaplikasi = (CardView) view.findViewById(R.id.cvtentangaplikasi_halaman_utama);
        cvgantipassword = (CardView) view.findViewById(R.id.cvgantipassword_halaman_utama);
        cvkeluar = (CardView) view.findViewById(R.id.cvkeluar_halaman_utama);
    }
}