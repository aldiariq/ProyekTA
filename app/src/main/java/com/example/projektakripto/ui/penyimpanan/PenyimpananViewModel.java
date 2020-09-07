package com.example.projektakripto.ui.penyimpanan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PenyimpananViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PenyimpananViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ini Halaman Penyimpanan");
    }

    public LiveData<String> getText() {
        return mText;
    }
}