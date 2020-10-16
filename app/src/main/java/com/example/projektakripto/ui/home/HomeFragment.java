package com.example.projektakripto.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.projektakripto.MainActivity;
import com.example.projektakripto.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    private TextView tvnamahalaman;
    private CardView cvpenyimpanan, cvtentangaplikasi, cvgantipassword, cvkeluar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =  ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_halaman_utama, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);

        homeViewModel.getNamahalamanhome().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvnamahalaman.setText(s + preference.getString("nama_pengguna", null));
            }
        });

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
                editor.putBoolean("sudah_masuk", false);
                editor.putString("id_pengguna", "");
                editor.putString("email_pengguna", "");
                editor.putString("nohp_pengguna", "");
                editor.apply();
                getActivity().finish();
                Intent pindahkehalamanmasuk = new Intent(getContext(), MainActivity.class);
                startActivity(pindahkehalamanmasuk);
            }
        });

        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        preference = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preference.edit();
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_home);
        cvpenyimpanan = (CardView) view.findViewById(R.id.cvpenyimpanan_halaman_utama);
        cvtentangaplikasi = (CardView) view.findViewById(R.id.cvtentangaplikasi_halaman_utama);
        cvgantipassword = (CardView) view.findViewById(R.id.cvgantipassword_halaman_utama);
        cvkeluar = (CardView) view.findViewById(R.id.cvkeluar_halaman_utama);
    }
}