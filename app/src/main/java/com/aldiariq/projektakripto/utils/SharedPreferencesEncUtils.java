package com.aldiariq.projektakripto.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedPreferencesEncUtils {
    public SharedPreferences getEncryptedSharedPreferences(String masterKeyAlias, Context context) throws GeneralSecurityException, IOException {
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                "projektakripto_preferences_enc",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
        return sharedPreferences;
    }
}
