package com.aldiariq.projektakripto.ui.tentangaplikasi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aldiariq.projektakripto.R;

public class TentangAplikasiFragment extends Fragment {

    public static TentangAplikasiFragment newInstance() {
        return new TentangAplikasiFragment();
    }

    private TextView tvtentangAplikasi;
    private TextView tvpembimbingAplikasi;
    private TextView tvmahasiswaAplikasi;
    private TextView tvintroAplikasi;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tentang_aplikasi_fragment, container, false);

        progressDialog = ProgressDialog.show(getContext(), "Proses Tentang Aplikasi", "Silahkan Menunggu..");

        //Memanggil Method Inisialisasi Komponen View
        initView(root);

        //Menampilkan Variabel ke Fragment Tentang Aplikasi
        tvtentangAplikasi.setText(getResources().getString(R.string.nama_aplikasi));
        tvpembimbingAplikasi.setText("Pembimbing : " + getResources().getString(R.string.nama_pembimbing_1));
        tvmahasiswaAplikasi.setText("Mahasiswa : " + getResources().getString(R.string.nama_mahasiswa));
        tvintroAplikasi.setText(getResources().getString(R.string.intro_aplikasi));

        progressDialog.dismiss();

        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        tvtentangAplikasi = view.findViewById(R.id.tv_tentang_tentangaplikasi);
        tvpembimbingAplikasi = view.findViewById(R.id.tv_pembimbing_tentangaplikasi);
        tvmahasiswaAplikasi = view.findViewById(R.id.tv_mahasiswa_tentangaplikasi);
        tvintroAplikasi = view.findViewById(R.id.tv_intro_tentangaplikasi);
    }

}