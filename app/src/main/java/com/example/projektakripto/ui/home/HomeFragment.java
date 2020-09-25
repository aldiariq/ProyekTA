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
import com.example.projektakripto.R;
import com.example.projektakripto.ui.penyimpanan.PenyimpananFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private TextView tvnamahalaman;
    private CardView cvpenyimpanan, cvtentangaplikasi, cvgantipassword, cvkeluar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =  ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_halaman_utama, container, false);
        initView(root);

        homeViewModel.getNamahalamanhome().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvnamahalaman.setText(s);
            }
        });

        cvpenyimpanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setTitle("Penyimpanan");
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new PenyimpananFragment());
                fragmentTransaction.addToBackStack("Kembali ke Dashboard");
                fragmentTransaction.commit();
            }
        });

        cvtentangaplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cvgantipassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cvkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }

    private void initView(View view){
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_home);
        cvpenyimpanan = (CardView) view.findViewById(R.id.cvpenyimpanan_halaman_utama);
        cvtentangaplikasi = (CardView) view.findViewById(R.id.cvtentangaplikasi_halaman_utama);
        cvgantipassword = (CardView) view.findViewById(R.id.cvgantipassword_halaman_utama);
        cvkeluar = (CardView) view.findViewById(R.id.cvkeluar_halaman_utama);
    }
}