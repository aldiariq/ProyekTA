package com.example.projektakripto.ui.gantipassword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projektakripto.DaftarActivity;
import com.example.projektakripto.MainActivity;
import com.example.projektakripto.R;
import com.example.projektakripto.network.DataService;
import com.example.projektakripto.network.ServiceGenerator;
import com.example.projektakripto.response.ResponseGantiPasswordPengguna;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GantiPasswordFragment extends Fragment {

    private TextView tvnamahalaman;
    private EditText passwordLama, passwordBaru1, passwordBaru2;
    private Button btnSimpan;

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    public DataService dataService;

    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ganti_password_fragment, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(getContext(), "Proses Ganti Password", "Silahkan Menunggu..");
                String id_pengguna = preference.getString("id_pengguna", null);
                String passwordlama = passwordLama.getText().toString().trim();
                String passwordbaru1 = passwordBaru1.getText().toString().trim();
                String passwordbaru2 = passwordBaru2.getText().toString().trim();

                if (passwordbaru1.equals(passwordbaru2)){
                    //Inisialisasi dan Pemanggilan Method Call Retrofit
                    Call<ResponseGantiPasswordPengguna> callGantipassword = dataService.apiGantipassword(id_pengguna, passwordlama, passwordbaru2);
                    callGantipassword.enqueue(new Callback<ResponseGantiPasswordPengguna>() {
                        @Override
                        public void onResponse(Call<ResponseGantiPasswordPengguna> call, Response<ResponseGantiPasswordPengguna> response) {
                            //Pengecekan Response Code
                            if (response.code() == 200){
                                //Pengecekan Status dari Response Body
                                if (response.body().isBerhasil()){
                                    Toast.makeText(getContext(), "Berhasil Mengganti Password", Toast.LENGTH_SHORT).show();
                                    editor.putBoolean("sudah_masuk", false);
                                    editor.putString("id_pengguna", "");
                                    editor.putString("email_pengguna", "");
                                    editor.apply();
                                    getActivity().finish();
                                    Intent pindahkehalamanmasuk = new Intent(getContext(), MainActivity.class);
                                    startActivity(pindahkehalamanmasuk);
                                }else {
                                    Toast.makeText(getContext(), "Gagal Mengganti Password", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "Gagal Mengganti Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseGantiPasswordPengguna> call, Throwable t) {
                            Toast.makeText(getContext(), "Gagal Mengganti Password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getContext(), "Pastikan Password Sama", Toast.LENGTH_SHORT).show();
                }
                resetInputan();
                progressDialog.dismiss();
            }
        });
        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        preference = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preference.edit();
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_ganti_password);
        passwordLama = (EditText) view.findViewById(R.id.etpasswordlamaGantipassword);
        passwordBaru1 = (EditText) view.findViewById(R.id.etpasswordbaru1Gantipassword);
        passwordBaru2 = (EditText) view.findViewById(R.id.etpasswordbaru2Gantipassword);
        btnSimpan = (Button) view.findViewById(R.id.btnsimpanpasswordGantipassword);
    }

    //Method Untuk Mengosongkan Field Inputan
    private void resetInputan(){
        passwordLama.setText("");
        passwordBaru1.setText("");
        passwordBaru2.setText("");
    }

}