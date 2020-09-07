package com.example.projektakripto.ui.gantipassword;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GantiPasswordViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public GantiPasswordViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ini Halaman Ganti Password");
    }

    public LiveData<String> getText() {
        return mText;
    }
}