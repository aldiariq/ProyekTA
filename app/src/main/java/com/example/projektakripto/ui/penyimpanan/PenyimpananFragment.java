package com.example.projektakripto.ui.penyimpanan;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projektakripto.R;
import com.example.projektakripto.ui.gantipassword.GantiPasswordViewModel;
import com.example.projektakripto.ui.tentangaplikasi.TentangAplikasiFragment;

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
        initView(root);

        penyimpananViewModel.getNamahalamanpenyimpanan().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvnamahalaman.setText(s);
            }
        });
        return root;
    }

    private void initView(View view){
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_penyimpanan);
        rvFile = (RecyclerView) view.findViewById(R.id.rv_file_penyimpanan);
    }
}