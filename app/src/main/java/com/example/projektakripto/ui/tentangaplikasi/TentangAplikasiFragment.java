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
import com.example.projektakripto.ui.penyimpanan.PenyimpananViewModel;

public class TentangAplikasiFragment extends Fragment {

    private TentangAplikasiViewModel tentangAplikasiViewModel;

    public static TentangAplikasiFragment newInstance() {
        return new TentangAplikasiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tentangAplikasiViewModel =  ViewModelProviders.of(this).get(TentangAplikasiViewModel.class);
        View root = inflater.inflate(R.layout.tentang_aplikasi_fragment, container, false);
        final TextView textView = root.findViewById(R.id.tv_tentangaplikasi);
//        tentangAplikasiViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        initView(root);
        return root;
    }

    private void initView(View view){

    }

}