package com.example.projektakripto.ui.gantipassword;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.projektakripto.R;

public class GantiPasswordFragment extends Fragment {

    private GantiPasswordViewModel gantiPasswordViewModel;

    private TextView tvnamahalaman;
    private EditText passwordLama, passwordBaru1, passwordBaru2;
    private Button btnSimpan;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gantiPasswordViewModel =  ViewModelProviders.of(this).get(GantiPasswordViewModel.class);
        View root = inflater.inflate(R.layout.ganti_password_fragment, container, false);
        initView(root);
        gantiPasswordViewModel.getnamaHalamangantipassword().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvnamahalaman.setText(s);
            }
        });
        return root;
    }

    private void initView(View view){
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_ganti_password);
        passwordLama = (EditText) view.findViewById(R.id.etpasswordlamaGantipassword);
        passwordBaru1 = (EditText) view.findViewById(R.id.etpasswordbaru1Gantipassword);
        passwordBaru2 = (EditText) view.findViewById(R.id.etpasswordbaru2Gantipassword);
        btnSimpan = (Button) view.findViewById(R.id.btnsimpanpasswordGantipassword);
    }

}