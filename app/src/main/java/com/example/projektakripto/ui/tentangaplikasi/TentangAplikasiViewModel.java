package com.example.projektakripto.ui.tentangaplikasi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TentangAplikasiViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public TentangAplikasiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ini Halaman Tentang Aplikasi");
    }

    public LiveData<String> getText() {
        return mText;
    }
}