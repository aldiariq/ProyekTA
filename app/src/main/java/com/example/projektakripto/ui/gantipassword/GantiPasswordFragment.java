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
import android.widget.TextView;

import com.example.projektakripto.R;

public class GantiPasswordFragment extends Fragment {

    private GantiPasswordViewModel gantiPasswordViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gantiPasswordViewModel =  ViewModelProviders.of(this).get(GantiPasswordViewModel.class);
        View root = inflater.inflate(R.layout.ganti_password_fragment, container, false);
        final TextView textView = root.findViewById(R.id.tv_ganti_password);
        gantiPasswordViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}