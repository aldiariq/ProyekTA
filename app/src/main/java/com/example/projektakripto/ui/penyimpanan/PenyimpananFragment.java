package com.example.projektakripto.ui.penyimpanan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projektakripto.R;

public class PenyimpananFragment extends Fragment {

    private PenyimpananViewModel penyimpananViewModel;
    private TextView tvnamahalaman;
    private RecyclerView rvFile;

    public static PenyimpananFragment newInstance() {
        return new PenyimpananFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        penyimpananViewModel =  ViewModelProviders.of(this).get(PenyimpananViewModel.class);
        View root = inflater.inflate(R.layout.penyimpanan_fragment, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);

        penyimpananViewModel.getNamahalamanpenyimpanan().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvnamahalaman.setText(s);
            }
        });
        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_penyimpanan);
        rvFile = (RecyclerView) view.findViewById(R.id.rv_file_penyimpanan);
    }
}