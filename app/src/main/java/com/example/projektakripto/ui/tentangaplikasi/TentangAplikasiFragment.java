package com.example.projektakripto.ui.tentangaplikasi;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projektakripto.R;

import org.w3c.dom.Text;

public class TentangAplikasiFragment extends Fragment {

    private TentangAplikasiViewModel tentangAplikasiViewModel;

    public static TentangAplikasiFragment newInstance() {
        return new TentangAplikasiFragment();
    }

    private TextView tvtentangAplikasi;
    private TextView tvpembimbingAplikasi;
    private TextView tvpembuatAplikasi;
    private TextView tvintroAplikasi;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tentangAplikasiViewModel =  ViewModelProviders.of(this).get(TentangAplikasiViewModel.class);
        View root = inflater.inflate(R.layout.tentang_aplikasi_fragment, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);

        tentangAplikasiViewModel.gettentangAplikasi().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvtentangAplikasi.setText(s);
            }
        });

        tentangAplikasiViewModel.getpembimbingTugasakhir().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvpembimbingAplikasi.setText("Pembimbing Tugas Akhir : " + s);
            }
        });

        tentangAplikasiViewModel.getpembuatAplikasi().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvpembuatAplikasi.setText("Pembuat Aplikasi : " + s);
            }
        });

        tentangAplikasiViewModel.getintroAplikasi().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvintroAplikasi.setText(s);
            }
        });

        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        tvtentangAplikasi = view.findViewById(R.id.tv_tentang_tentangaplikasi);
        tvpembimbingAplikasi = view.findViewById(R.id.tv_pembimbing_tentangaplikasi);
        tvpembuatAplikasi = view.findViewById(R.id.tv_pembuat_tentangaplikasi);
        tvintroAplikasi = view.findViewById(R.id.tv_intro_tentangaplikasi);
    }

}