package com.aldiariq.projektakripto.ui.gantipassword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.MasterKeys;

import com.aldiariq.projektakripto.MainActivity;
import com.aldiariq.projektakripto.R;
import com.aldiariq.projektakripto.network.DataService;
import com.aldiariq.projektakripto.network.ServiceGenerator;
import com.aldiariq.projektakripto.response.ResponseGantiPasswordPengguna;
import com.aldiariq.projektakripto.utils.SharedPreferencesEncUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GantiPasswordFragment extends Fragment {

    private TextView tvnamahalaman;
    private EditText passwordLama, passwordBaru1, passwordBaru2;
    private Button btnSimpan;

    public DataService dataService;

    private ProgressDialog progressDialog;

    private String id_pengguna;
    private String token_pengguna;

    private SharedPreferencesEncUtils sharedPreferencesEncUtils;
    private String masterKeyAlias;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ganti_password_fragment, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(getContext(), "Proses Ganti Password", "Silahkan Menunggu..");
                String passwordlama = passwordLama.getText().toString().trim();
                String passwordbaru1 = passwordBaru1.getText().toString().trim();
                String passwordbaru2 = passwordBaru2.getText().toString().trim();

                if (passwordbaru1.equals(passwordbaru2)){
                    //Inisialisasi dan Pemanggilan Method Call Retrofit
                    Call<ResponseGantiPasswordPengguna> callGantipassword = dataService.apiGantipassword(token_pengguna, id_pengguna, passwordlama, passwordbaru2);
                    callGantipassword.enqueue(new Callback<ResponseGantiPasswordPengguna>() {
                        @Override
                        public void onResponse(Call<ResponseGantiPasswordPengguna> call, Response<ResponseGantiPasswordPengguna> response) {
                            //Pengecekan Response Code
                            if (response.code() == 200){
                                //Pengecekan Status dari Response Body
                                if (response.body().isBerhasil()){
                                    Toast.makeText(getContext(), "Berhasil Mengganti Password", Toast.LENGTH_SHORT).show();
                                    try {
                                        sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).edit().putBoolean("sudah_masuk", false).apply();
                                        sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).edit().putString("id_pengguna", "").apply();
                                        sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).edit().putString("email_pengguna", "").apply();
                                    } catch (GeneralSecurityException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    getActivity().finish();
                                    Intent pindahkehalamanmasuk = new Intent(getContext(), MainActivity.class);
                                    startActivity(pindahkehalamanmasuk);
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Gagal Mengganti Password, Pastikan Password Lama Benar", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Gagal Mengganti Password, Pastikan Password Lama Benar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseGantiPasswordPengguna> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Gagal Mengganti Password, Pastikan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Gagal Mengganti Password, Pastikan Password Sama", Toast.LENGTH_SHORT).show();
                }
                resetInputan();
            }
        });
        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        sharedPreferencesEncUtils = new SharedPreferencesEncUtils();
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_ganti_password);
        passwordLama = (EditText) view.findViewById(R.id.etpasswordlamaGantipassword);
        passwordBaru1 = (EditText) view.findViewById(R.id.etpasswordbaru1Gantipassword);
        passwordBaru2 = (EditText) view.findViewById(R.id.etpasswordbaru2Gantipassword);
        btnSimpan = (Button) view.findViewById(R.id.btnsimpanpasswordGantipassword);
        try {
            id_pengguna = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).getString("id_pengguna", "");
            token_pengguna = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).getString("token_pengguna", "");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method Untuk Mengosongkan Field Inputan
    private void resetInputan(){
        passwordLama.setText("");
        passwordBaru1.setText("");
        passwordBaru2.setText("");
    }

}