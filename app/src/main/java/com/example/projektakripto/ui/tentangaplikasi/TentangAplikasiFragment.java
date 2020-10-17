package com.example.projektakripto.ui.tentangaplikasi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projektakripto.R;

public class TentangAplikasiFragment extends Fragment {

    public static TentangAplikasiFragment newInstance() {
        return new TentangAplikasiFragment();
    }

    private TextView tvtentangAplikasi;
    private TextView tvpembimbing1Aplikasi;
    private TextView tvpembimbing2Aplikasi;
    private TextView tvmahasiswaAplikasi;
    private TextView tvintroAplikasi;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tentang_aplikasi_fragment, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);

        //Menampilkan Variabel ke Fragment Tentang Aplikasi
        tvtentangAplikasi.setText(getResources().getString(R.string.nama_aplikasi));
        tvpembimbing1Aplikasi.setText("Pembimbing 1 : " + getResources().getString(R.string.nama_pembimbing_1));
        tvpembimbing2Aplikasi.setText("Pembimbing 2 : " + getResources().getString(R.string.nama_pembimbing_2));
        tvmahasiswaAplikasi.setText("Mahasiswa : " + getResources().getString(R.string.nama_mahasiswa));
        tvintroAplikasi.setText(getResources().getString(R.string.intro_aplikasi));

        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        tvtentangAplikasi = view.findViewById(R.id.tv_tentang_tentangaplikasi);
        tvpembimbing1Aplikasi = view.findViewById(R.id.tv_pembimbing_1_tentangaplikasi);
        tvpembimbing2Aplikasi = view.findViewById(R.id.tv_pembimbing_2_tentangaplikasi);
        tvmahasiswaAplikasi = view.findViewById(R.id.tv_mahasiswa_tentangaplikasi);
        tvintroAplikasi = view.findViewById(R.id.tv_intro_tentangaplikasi);
    }

}