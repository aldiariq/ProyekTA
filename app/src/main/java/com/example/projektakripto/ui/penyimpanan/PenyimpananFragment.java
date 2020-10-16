package com.example.projektakripto.ui.penyimpanan;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projektakripto.R;
import com.example.projektakripto.adapter.FilePenggunaAdapter;
import com.example.projektakripto.algoritma.blowfish.Blowfish;
import com.example.projektakripto.algoritma.rsa.RSA;
import com.example.projektakripto.model.FilePengguna;
import com.example.projektakripto.model.KunciRSA;
import com.example.projektakripto.network.DataService;
import com.example.projektakripto.network.Server;
import com.example.projektakripto.network.ServiceGenerator;
import com.example.projektakripto.response.ResponseDeleteFile;
import com.example.projektakripto.response.ResponseDownloadFile;
import com.example.projektakripto.response.ResponseGetFile;
import com.example.projektakripto.response.ResponseGetKunciRSA;
import com.example.projektakripto.response.ResponseUploadFile;
import com.example.projektakripto.utils.FileUtils;
import com.example.projektakripto.utils.OnDeleteClickListener;
import com.example.projektakripto.utils.OnDownloadClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class PenyimpananFragment extends Fragment implements OnDownloadClickListener, OnDeleteClickListener {

    private PenyimpananViewModel penyimpananViewModel;
    private TextView tvnamahalaman;
    private DataService dataService;
    private FilePenggunaAdapter filePenggunaAdapter;
    private SharedPreferences preference;
    private RecyclerView rvFile;

    public static PenyimpananFragment newInstance() {
        return new PenyimpananFragment();
    }

    private ImageView imgpilihfileformupload;
    private TextView txtlokasifileformupload;
    private EditText etpasswordblowfishformupload;
    private EditText etpasswordblowfishformdownload;

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    private static final String LOG_TAG = "AndroidExample";

    private ArrayList<FilePengguna> filePenggunas = new ArrayList<>();
    private DownloadManager downloadManager;
    private DownloadManager.Request downloadManagerrequest;

    private String id_pengguna;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        penyimpananViewModel =  ViewModelProviders.of(this).get(PenyimpananViewModel.class);
        View root = inflater.inflate(R.layout.penyimpanan_fragment, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);
        rvFile.setAdapter(filePenggunaAdapter);
        rvFile.setLayoutManager(new LinearLayoutManager(getContext()));

        //Memanggil Method Load Data Pengguna
        loadData();

        final FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.form_upload_file, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);

                dialog.setPositiveButton("Upload", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String lokasifileinput = txtlokasifileformupload.getText().toString();
                        String lokasifileoutput = txtlokasifileformupload.getText().toString() + ".enc";
                        String passwordblowfish = etpasswordblowfishformupload.getText().toString();

                        if (lokasifileinput.isEmpty() || lokasifileoutput.isEmpty() || passwordblowfish.isEmpty()){
                            Toast.makeText(getContext(), "Pastikan File Sudah Dipilih dan Password Sudah Diinputkan", Toast.LENGTH_SHORT).show();
                        }else {
                            Blowfish enkripsiblowfish = new Blowfish(passwordblowfish);
                            enkripsiblowfish.encrypt(lokasifileinput, lokasifileoutput);

                            Call<ResponseGetKunciRSA> getKunciRSACall = dataService.apiGetkuncirsa(id_pengguna);
                            getKunciRSACall.enqueue(new Callback<ResponseGetKunciRSA>() {
                                @Override
                                public void onResponse(Call<ResponseGetKunciRSA> call, Response<ResponseGetKunciRSA> response) {
                                    List<KunciRSA> kunciRSA = (List<KunciRSA>) response.body().getKunci_rsa();
                                    String kunciprivate = kunciRSA.get(0).getKunci_private();
                                    String kuncipublic = kunciRSA.get(0).getKunci_public();
                                    String kuncimodulus = kunciRSA.get(0).getKunci_modulus();

                                    RSA enkripsirsa = new RSA(new BigInteger(kunciprivate), new BigInteger(kuncipublic), new BigInteger(kuncimodulus));

                                    File filehasilenkripsi = new File(lokasifileoutput);
                                    RequestBody requestBodynamafile = RequestBody.create(MediaType.parse("text/plain"), filehasilenkripsi.getName());
                                    RequestBody idPengguna = RequestBody.create(MediaType.parse("text/plain"), preference.getString("id_pengguna", null));
                                    RequestBody kunciFile = RequestBody.create(MediaType.parse("text/plain"), enkripsirsa.encrypt(passwordblowfish));

                                    RequestBody requestBodyfile = RequestBody.create(MediaType.parse("/"), filehasilenkripsi);
                                    MultipartBody.Part fileEnkripsi = MultipartBody.Part.createFormData("file_enkripsi", filehasilenkripsi.getName(), requestBodyfile);

                                    Call<ResponseUploadFile> callUpload = dataService.apiUploadfile(requestBodynamafile, idPengguna, kunciFile, fileEnkripsi);
                                    callUpload.enqueue(new Callback<ResponseUploadFile>() {
                                        @Override
                                        public void onResponse(Call<ResponseUploadFile> call, Response<ResponseUploadFile> response) {
                                            //Pengecekan Response Code
                                            if (response.code() == 200){
                                                //Pengecekan Status dari Response Body
                                                if (response.body().isBerhasil()){
                                                    Toast.makeText(getContext(), "Berhasil Mengupload File", Toast.LENGTH_SHORT).show();
                                                    filehasilenkripsi.delete();
                                                    loadData();
                                                }else {
                                                    Toast.makeText(getContext(), "Gagal Mengupload File", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(getContext(), "Gagal Mengupload File", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseUploadFile> call, Throwable t) {
                                            Toast.makeText(getContext(), "Gagal Mengupload File", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<ResponseGetKunciRSA> call, Throwable t) {
                                    Toast.makeText(getContext(), "Gagal Mendekripsi File", Toast.LENGTH_SHORT).show();
                                }
                            });

                            dialog.dismiss();
                        }
                    }
                });

                dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                imgpilihfileformupload = dialogView.findViewById(R.id.imgpilihfileformupload);
                txtlokasifileformupload = dialogView.findViewById(R.id.txtlokasifileformupload);
                etpasswordblowfishformupload = dialogView.findViewById(R.id.etpasswordblowfishformupload);

                imgpilihfileformupload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deteksiPermissionandroid();
                    }
                });
            }
        });

        penyimpananViewModel.getNamahalamanpenyimpanan().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvnamahalaman.setText(s);
            }
        });
        return root;
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_penyimpanan);
        filePenggunaAdapter = new FilePenggunaAdapter(getContext());
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        preference = PreferenceManager.getDefaultSharedPreferences(getContext());
        rvFile = (RecyclerView) view.findViewById(R.id.rv_file_penyimpanan);
        filePenggunaAdapter.setOnDownloadClickListener(this);
        filePenggunaAdapter.setOnDeleteClickListener(this);
        downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        id_pengguna = preference.getString("id_pengguna", "");
    }

    //Method Load Data Pengguna
    private void loadData(){
        String idPengguna = preference.getString("id_pengguna", null);
        filePenggunas.clear();
        filePenggunaAdapter.clear();
        Call<ResponseGetFile<List<FilePengguna>>> loadDatapengguna = dataService.apiGetfile(idPengguna);
        loadDatapengguna.enqueue(new Callback<ResponseGetFile<List<FilePengguna>>>() {
            @Override
            public void onResponse(Call<ResponseGetFile<List<FilePengguna>>> call, Response<ResponseGetFile<List<FilePengguna>>> response) {
                //Pengecekan Response Code
                if (response.code() == 200){
                    filePenggunas.addAll(response.body().getFile_pengguna());
                    filePenggunaAdapter.addAll(filePenggunas);
                    filePenggunaAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(), "Gagal Mengambil File Pengguna", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseGetFile<List<FilePengguna>>> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mengambil File Pengguna", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        String id_file = filePenggunas.get(position).getId_file();
        Call<ResponseDeleteFile> deleteFile = dataService.apiDeletefile(id_file);
        deleteFile.enqueue(new Callback<ResponseDeleteFile>() {
            @Override
            public void onResponse(Call<ResponseDeleteFile> call, Response<ResponseDeleteFile> response) {
                //Pengecekan Response Code
                if (response.code() == 200){
                    if (response.body().isBerhasil()){
                        Toast.makeText(getContext(), "Berhasil Menghapus File Pengguna", Toast.LENGTH_SHORT).show();
                        loadData();
                    }else {
                        Toast.makeText(getContext(), "Gagal Menghapus File Pengguna", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Gagal Menghapus File Pengguna", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDeleteFile> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Menghapus File Pengguna", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDownloadClick(int position) {
        String id_file = filePenggunas.get(position).getId_file();

        Call<ResponseDownloadFile> downloadFile = dataService.apiDownloadfile(id_pengguna, id_file);
        downloadFile.enqueue(new Callback<ResponseDownloadFile>() {
            @Override
            public void onResponse(Call<ResponseDownloadFile> call, Response<ResponseDownloadFile> response) {
                //Pengecekan Response Code
                if (response.code() == 200){
                    List<FilePengguna> downloadfilePenggunas = (List<FilePengguna>) response.body().getFile_pengguna();
                    String lokasifile = Server.BASE_URL + "FilePengguna/" + downloadfilePenggunas.get(0).getNama_file();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.form_download_file, null);
                    dialog.setView(dialogView);
                    dialog.setCancelable(true);

                    dialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String passwordblowfish = etpasswordblowfishformdownload.getText().toString().trim();

                            Call<ResponseGetKunciRSA> getKunciRSACall = dataService.apiGetkuncirsa(id_pengguna);
                            getKunciRSACall.enqueue(new Callback<ResponseGetKunciRSA>() {
                                @Override
                                public void onResponse(Call<ResponseGetKunciRSA> call, Response<ResponseGetKunciRSA> response) {
                                    List<KunciRSA> kunciRSA = (List<KunciRSA>) response.body().getKunci_rsa();
                                    String kunciprivate = kunciRSA.get(0).getKunci_private();
                                    String kuncipublic = kunciRSA.get(0).getKunci_public();
                                    String kuncimodulus = kunciRSA.get(0).getKunci_modulus();

                                    RSA dekriprsa = new RSA(new BigInteger(kunciprivate), new BigInteger(kuncipublic), new BigInteger(kuncimodulus));

                                    String hasildekrip = dekriprsa.decrypt(downloadfilePenggunas.get(0).getKunci_file());

                                    if (hasildekrip.equals(passwordblowfish)){
                                        downloadManagerrequest = new DownloadManager.Request(Uri.parse(lokasifile));
                                        downloadManagerrequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                                .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS , filePenggunas.get(0).getNama_file())
                                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                                        long downloadId = downloadManager.enqueue(downloadManagerrequest);

                                        String namafilepengguna = URLUtil.guessFileName(lokasifile, null, MimeTypeMap.getFileExtensionFromUrl(lokasifile));

                                        String fileenkripsihasildownload = Environment.getExternalStorageDirectory() + File.separator + DIRECTORY_DOWNLOADS + File.separator + namafilepengguna;
                                        File filepengguna = new File(fileenkripsihasildownload);

                                        String filedekripsihasildownload = Environment.getExternalStorageDirectory() + File.separator + DIRECTORY_DOWNLOADS + File.separator + namafilepengguna.replace(".enc", "");

                                        Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));

                                        if (cursor != null && cursor.moveToNext()) {
                                            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                                            cursor.close();

                                            if (status == DownloadManager.STATUS_FAILED) {
                                                // do something when failed
                                            }
                                            else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
                                                // do something pending or paused
                                            }
                                            else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                                Toast.makeText(getContext(), "Download Selesai", Toast.LENGTH_SHORT).show();
                                            }
                                            else if (status == DownloadManager.STATUS_RUNNING) {
                                                // do something when running
                                            }
                                        }

                                        Blowfish dekripsiblowfish = new Blowfish(passwordblowfish);
                                        dekripsiblowfish.decrypt(fileenkripsihasildownload, filedekripsihasildownload);

//                                filepengguna.delete();
                                    }else {
                                        Toast.makeText(getContext(), "Pastikan Password Benar", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseGetKunciRSA> call, Throwable t) {
                                    Toast.makeText(getContext(), "Gagal Mendekripsi File", Toast.LENGTH_SHORT).show();
                                }
                            });

                            dialog.dismiss();
                        }
                    });

                    dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    etpasswordblowfishformdownload = dialogView.findViewById(R.id.etpasswordblowfishformdownload);
                }else {
                    Toast.makeText(getContext(), "Gagal Mendownload File Pengguna", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDownloadFile> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal Mendownload File Pengguna", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deteksiPermissionandroid()  {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23

            // Check if we have read storage permission
            int pemissionread = ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (pemissionread != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }

            //check if we have write storage permission
            int permissionwrite = ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionwrite != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }
        }
        this.pilihFile();
    }

    private void pilihFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("application/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Akses File Diizinkan", Toast.LENGTH_SHORT).show();

                    this.pilihFile();
                }
                // Cancelled or denied.
                else {
                    Toast.makeText(getContext(), "Akses File Tidak Diizinkan", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        Uri fileUri = data.getData();
                        Log.i(LOG_TAG, "Uri: " + fileUri);

                        String filePath = null;
                        try {
                            filePath = FileUtils.getPath(getContext(),fileUri);
                        } catch (Exception e) {
                            Log.e(LOG_TAG,"Error: " + e);
                            Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.txtlokasifileformupload.setText(filePath);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}