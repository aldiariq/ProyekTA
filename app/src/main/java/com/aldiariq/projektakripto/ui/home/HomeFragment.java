package com.aldiariq.projektakripto.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.aldiariq.projektakripto.DashboardActivity;
import com.aldiariq.projektakripto.MainActivity;
import com.aldiariq.projektakripto.R;
import com.aldiariq.projektakripto.network.DataService;
import com.aldiariq.projektakripto.network.ServiceGenerator;
import com.aldiariq.projektakripto.response.ResponseKeluar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    private TextView tvnamahalaman;
    private CardView cvpenyimpanan, cvtentangaplikasi, cvgantipassword, cvkeluar;

    private DataService dataService;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);

        tvnamahalaman.setText("Selamat Datang, " + preference.getString("nama_pengguna", ""));

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
        cvkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        preference = PreferenceManager.getDefaultSharedPreferences(getContext());
        dataService = (DataService) ServiceGenerator.createBaseService(getActivity(), DataService.class);
        editor = preference.edit();
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_home);
        cvpenyimpanan = (CardView) view.findViewById(R.id.cvpenyimpanan_halaman_utama);
        cvtentangaplikasi = (CardView) view.findViewById(R.id.cvtentangaplikasi_halaman_utama);
        cvgantipassword = (CardView) view.findViewById(R.id.cvgantipassword_halaman_utama);
        cvkeluar = (CardView) view.findViewById(R.id.cvkeluar_halaman_utama);
    }
}