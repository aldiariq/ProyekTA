package com.example.projektakripto.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.projektakripto.R;
import com.example.projektakripto.ui.penyimpanan.PenyimpananFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

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
                tvnamahalaman.setText(s);
            }
        });

        //Listener Click CardView Penyimpanan
        cvpenyimpanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.createNavigateOnClickListener(R.id.action_nav_halaman_utama_to_nav_penyimpanan, null);
            }
        });

        //Listener Click CardView Tentang Aplikasi
        cvtentangaplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.createNavigateOnClickListener(R.id.action_nav_halaman_utama_to_nav_tentangaplikasi, null);
            }
        });

        //Listener Click CardView Ganti Password
        cvgantipassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.createNavigateOnClickListener(R.id.action_nav_halaman_utama_to_nav_ganti_password, null);
            }
        });

        //Listener Click CardView Keluar Aplikasi
        cvkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_home);
        cvpenyimpanan = (CardView) view.findViewById(R.id.cvpenyimpanan_halaman_utama);
        cvtentangaplikasi = (CardView) view.findViewById(R.id.cvtentangaplikasi_halaman_utama);
        cvgantipassword = (CardView) view.findViewById(R.id.cvgantipassword_halaman_utama);
        cvkeluar = (CardView) view.findViewById(R.id.cvkeluar_halaman_utama);
    }
}