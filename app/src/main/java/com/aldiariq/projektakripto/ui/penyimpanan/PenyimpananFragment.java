package com.aldiariq.projektakripto.ui.penyimpanan;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.MasterKeys;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aldiariq.projektakripto.R;
import com.aldiariq.projektakripto.adapter.FilePenggunaAdapter;
//import com.aldiariq.projektakripto.algoritma.accesstime.AccessTime;
import com.aldiariq.projektakripto.algoritma.avalancheeffect.AvalancheEffect;
import com.aldiariq.projektakripto.algoritma.blowfish.Blowfish;
import com.aldiariq.projektakripto.algoritma.rsa.RSA;
import com.aldiariq.projektakripto.model.FilePengguna;
import com.aldiariq.projektakripto.model.KunciRSA;
import com.aldiariq.projektakripto.network.DataService;
import com.aldiariq.projektakripto.network.Server;
import com.aldiariq.projektakripto.network.ServiceGenerator;
import com.aldiariq.projektakripto.response.ResponseDeleteFile;
import com.aldiariq.projektakripto.response.ResponseDownloadFile;
import com.aldiariq.projektakripto.response.ResponseGetFile;
import com.aldiariq.projektakripto.response.ResponseGetKunciRSA;
import com.aldiariq.projektakripto.response.ResponseUploadFile;
import com.aldiariq.projektakripto.utils.FileUtils;
import com.aldiariq.projektakripto.utils.OnDeleteClickListener;
import com.aldiariq.projektakripto.utils.OnDownloadClickListener;
import com.aldiariq.projektakripto.utils.SharedPreferencesEncUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class PenyimpananFragment extends Fragment implements OnDownloadClickListener, OnDeleteClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView tvnamahalaman;
    private EditText etcari;
    private DataService dataService;
    private FilePenggunaAdapter filePenggunaAdapter;
    private RecyclerView rvFile;

    private ImageView imgpilihfileformupload;
    private TextView txtlokasifileformupload;
    private EditText etpasswordblowfishformupload;
    private EditText etpasswordblowfishformdownload;

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;


    public static ArrayList<FilePengguna> filePenggunas = new ArrayList<>();
    private DownloadManager downloadManager;
    private DownloadManager.Request downloadManagerrequest;

    private String token_pengguna;
    private String id_pengguna;

    private ProgressDialog progressDialog;

    private Blowfish blowfishsatu;
    private Blowfish blowfishdua;
    private Blowfish blowfishdekripsi;
    private AvalancheEffect avalancheEffect;
    private Random random;
//    private AccessTime accessTime;
    private long waktuMulai;
    private long waktuSelesai;
    private RSA rsa;

    private SharedPreferencesEncUtils sharedPreferencesEncUtils;
    private String masterKeyAlias;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.penyimpanan_fragment, container, false);

        //Memanggil Method Inisialisasi Komponen View
        initView(root);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.penyimpanan_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        rvFile.setAdapter(filePenggunaAdapter);
        rvFile.setLayoutManager(new LinearLayoutManager(getContext()));

        //Memanggil Method Load Data Pengguna
        loadData();

        etcari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pencarian = etcari.getText().toString();
                filePenggunaAdapter.getFilter().filter(pencarian);
            }
        });

        final FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inisialisasi Alert Dialog & Inflating Alert Dialog Layout
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.form_upload_file, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);

                //Inisialisasi Komponen di Dalam Alert Dialog
                imgpilihfileformupload = dialogView.findViewById(R.id.imgpilihfileformupload);
                txtlokasifileformupload = dialogView.findViewById(R.id.txtlokasifileformupload);
                etpasswordblowfishformupload = dialogView.findViewById(R.id.etpasswordblowfishformupload);

                //Listener Untuk Gambar Upload
                imgpilihfileformupload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Pengecekan Apakah Aplikasi Sudah Diberi Izin Akses Membaca dan Menulis Penyimpanan
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                            pilihFile();
                        }else {
                            deteksiPermissionandroid();
                        }
                    }
                });

                //Listener Untuk Upload Button
                dialog.setPositiveButton("Upload", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = ProgressDialog.show(getContext(), "Proses Upload File", "Silahkan Menunggu..");
                        //Inisialisasi Variabel Untuk Upload File
                        String lokasifileinput = txtlokasifileformupload.getText().toString();
                        String lokasifileoutputsatu = txtlokasifileformupload.getText().toString() + ".enc";
                        //Inisialisasi Variabel File Untuk Pengetesan Avalache Effect
                        String lokasifileoutputdua = txtlokasifileformupload.getText().toString() + ".enc.test";
                        String passwordblowfish = etpasswordblowfishformupload.getText().toString();

                        //Pengecekan Apakah Variabel Sudah Terisi Semua atau Belum
                        if (lokasifileinput.isEmpty() || lokasifileoutputsatu.isEmpty() || lokasifileoutputdua.isEmpty() || passwordblowfish.isEmpty()){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Pastikan File Sudah Dipilih dan Password Sudah Diinputkan", Toast.LENGTH_SHORT).show();
                        }else {
                            //Proses Pengambilan Kunci RSA Pengguna dari API
                            Call<ResponseGetKunciRSA> getKunciRSACall = dataService.apiGetkuncirsa(token_pengguna, id_pengguna);
                            getKunciRSACall.enqueue(new Callback<ResponseGetKunciRSA>() {
                                @Override
                                public void onResponse(Call<ResponseGetKunciRSA> call, Response<ResponseGetKunciRSA> response) {
                                    //Menampung Kunci RSA Pengguna di Dalam List
                                    List<KunciRSA> kunciRSA = (List<KunciRSA>) response.body().getKunci_rsa();
                                    //Menampung Nilai Kunci RSA
                                    String kunciprivate = "";
                                    try {
                                        kunciprivate = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).getString("kunci_private", "");
                                    } catch (GeneralSecurityException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String kuncipublic = kunciRSA.get(0).getKunci_public();
                                    String kuncimodulus = kunciRSA.get(0).getKunci_modulus();

                                    //Inisialisasi Class RSA
                                    rsa = new RSA(new BigInteger(kunciprivate), new BigInteger(kuncipublic), new BigInteger(kuncimodulus));

                                    //Proses Enkripsi File
                                    random = new Random();

                                    String passwordfilesatu = rsa.encrypt(passwordblowfish);
                                    String passwordfiledua;

                                    String karakteracak = "";

                                    do {
                                        karakteracak = String.valueOf((random.nextInt(9 - 0) + 0));
                                        passwordfiledua = karakteracak + passwordfilesatu.substring(1, passwordfilesatu.length());
                                    }while(karakteracak.equals(String.valueOf(passwordfilesatu.charAt(0))));

                                    //Log Info Enkripsi
                                    Log.i("Pass Blowfish File 1", passwordfilesatu);
                                    Log.i("Pass Blowfish File 2", passwordfiledua);

                                    //Inisialisasi Class Blowfish
                                    blowfishsatu = new Blowfish(passwordfilesatu);
                                    blowfishdua = new Blowfish(passwordfiledua);

                                    //Pemanggilan Method Enkripsi Blowfish dan Pengukuran Access Time
                                    waktuMulai = System.currentTimeMillis();
                                    blowfishsatu.encrypt(lokasifileinput, lokasifileoutputsatu);
                                    waktuSelesai = System.currentTimeMillis();
                                    blowfishdua.encrypt(lokasifileinput, lokasifileoutputdua);

                                    //Penghitung Access Time
//                                    accessTime = new AccessTime(waktuMulai, waktuSelesai);
//                                    Log.i("ACCESSTIME", "Access Time : " + accessTime.hitungAccesstime() + "ms");

                                    //Pemanggilan Method Avalanche Effect
                                    avalancheEffect = new AvalancheEffect(lokasifileoutputsatu, lokasifileoutputdua);
                                    Log.i("TINGKATAVALANCHE", "Tingkat Avalanche Effect : " + avalancheEffect.hitungAvalanche() + "%");
                                    Toast.makeText(getContext(), "Tingkat Avalanche Effect : " + avalancheEffect.hitungAvalanche() + "%", Toast.LENGTH_LONG).show();

                                    //Proses Penghapusan File
                                    blowfishsatu.hapusFile(lokasifileinput);
                                    blowfishdua.hapusFile(lokasifileoutputdua);

                                    //Inisialisasi File Hasil Enkripsi
                                    File filehasilenkripsi = new File(lokasifileoutputsatu);
                                    //Inisialisasi Request Body untuk Dikirimkan ke API
                                    //Menampung Nilai Kunci RSA
                                    RequestBody idPengguna = RequestBody.create(MediaType.parse("text/plain"), id_pengguna);
                                    RequestBody kunciFile = RequestBody.create(MediaType.parse("text/plain"), passwordfilesatu);
                                    RequestBody requestBodyfile = RequestBody.create(MediaType.parse("/"), filehasilenkripsi);
                                    MultipartBody.Part fileEnkripsi = MultipartBody.Part.createFormData("file_enkripsi", filehasilenkripsi.getName(), requestBodyfile);

                                    //Proses Upload File Hasil Enkripsi ke Server
                                    Call<ResponseUploadFile> callUpload = dataService.apiUploadfile(token_pengguna, idPengguna, kunciFile, fileEnkripsi);
                                    callUpload.enqueue(new Callback<ResponseUploadFile>() {
                                        @Override
                                        public void onResponse(Call<ResponseUploadFile> call, Response<ResponseUploadFile> response) {
                                            //Pengecekan Response Code
                                            if (response.code() == 200){
                                                //Pengecekan Status dari Response Body
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Berhasil Mengupload File", Toast.LENGTH_SHORT).show();
                                                filehasilenkripsi.delete();
                                                loadData();
                                            }else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Gagal Mengupload File, Pastikan Terkoneksi Internet 2", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseUploadFile> call, Throwable t) {
                                            progressDialog.dismiss();
                                            Log.i("errorupload", t.getMessage());
                                            Toast.makeText(getContext(), "Gagal Mengupload File, Pastikan Terkoneksi Internet 3", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<ResponseGetKunciRSA> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Gagal Mendekripsi File, Pastikan Terkoneksi Internet", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }
                    }
                });

                //Listener Untuk Batal Button
                dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return root;
    }

    @Override
    public void onRefresh() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            loadData();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    //Inisialisasi Komponen View
    private void initView(View view){
        tvnamahalaman = (TextView) view.findViewById(R.id.tv_penyimpanan);
        etcari = (EditText) view.findViewById(R.id.et_cari_penyimpanan);
        filePenggunaAdapter = new FilePenggunaAdapter(getContext());
        dataService = (DataService) ServiceGenerator.createBaseService(getContext(), DataService.class);
        sharedPreferencesEncUtils = new SharedPreferencesEncUtils();
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        rvFile = (RecyclerView) view.findViewById(R.id.rv_file_penyimpanan);
        filePenggunaAdapter.setOnDownloadClickListener(this);
        filePenggunaAdapter.setOnDeleteClickListener(this);
        downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            id_pengguna = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).getString("id_pengguna", "");
            token_pengguna = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).getString("token_pengguna", "");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method Untuk Memuat Data Pengguna
    private void loadData(){
        //Proses Pengambilan File Pengguna dari API
        Call<ResponseGetFile<List<FilePengguna>>> loadDatapengguna = dataService.apiGetfile(token_pengguna, id_pengguna);
        loadDatapengguna.enqueue(new Callback<ResponseGetFile<List<FilePengguna>>>() {
            @Override
            public void onResponse(Call<ResponseGetFile<List<FilePengguna>>> call, Response<ResponseGetFile<List<FilePengguna>>> response) {
                //Pengecekan Response Code
                if (response.code() == 200){
                    filePenggunas.clear();
//                    filePenggunaAdapter.clear();
                    filePenggunas.addAll(response.body().getFile_pengguna());
                    filePenggunaAdapter.addAll((ArrayList<FilePengguna>) filePenggunas.clone());
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_hapus_file, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        dialog.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog = ProgressDialog.show(getContext(), "Proses Hapus File", "Silahkan Menunggu..");
                //Menampung id_file yang ada dalam Shared Preference
                String id_file = filePenggunas.get(position).getId_file();
                String nama_file = filePenggunas.get(position).getNama_file();

                //Proses Penghapusan File Pengguna dari API
                Call<ResponseDeleteFile> deleteFile = dataService.apiDeletefile(token_pengguna, id_file, nama_file, id_pengguna);
                deleteFile.enqueue(new Callback<ResponseDeleteFile>() {
                    @Override
                    public void onResponse(Call<ResponseDeleteFile> call, Response<ResponseDeleteFile> response) {
                        //Pengecekan Response Code
                        if (response.code() == 200){
                            if (response.body().isBerhasil()){
                                etcari.setText("");
                                Toast.makeText(getContext(), "Berhasil Menghapus File Pengguna", Toast.LENGTH_SHORT).show();
//                                filePenggunas.clear();
//                                filePenggunaAdapter.clear();
//                                rvFile.setAdapter(filePenggunaAdapter);
//                                rvFile.setLayoutManager(new LinearLayoutManager(getContext()));
                                loadData();
//                                filePenggunaAdapter.clear();
//                                filePenggunaAdapter.removeFilepengguna(position);
//                                rvFile.swapAdapter(filePenggunaAdapter,false);
//                                rvFile.setLayoutManager(new LinearLayoutManager(getContext()));
//                                filePenggunaAdapter.notifyDataSetChanged();
//                                etcari.getText().clear();

//                                filePenggunas.remove(position);

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
                progressDialog.dismiss();
            }
        });

        dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onDownloadClick(int position) {
        //Menampung id_file yang ada dalam Shared Preference
        String id_file = filePenggunas.get(position).getId_file();

        //Proses Download File Pengguna dari API
        Call<ResponseDownloadFile> downloadFile = dataService.apiDownloadfile(token_pengguna, id_pengguna, id_file);
        downloadFile.enqueue(new Callback<ResponseDownloadFile>() {
            @Override
            public void onResponse(Call<ResponseDownloadFile> call, Response<ResponseDownloadFile> response) {
                //Pengecekan Response Code
                if (response.code() == 200){
                    //Menampung File Pengguna yang Terenkripsi ke Dalam List
                    List<FilePengguna> downloadfilePenggunas = (List<FilePengguna>) response.body().getFile_pengguna();

                    String urlfileEnkripsi = Server.BASE_URL + "FilePengguna/" + downloadfilePenggunas.get(0).getId_pengguna() + "/" + downloadfilePenggunas.get(0).getNama_file();

                    //Inisialisasi Aler Dialog
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.form_download_file, null);
                    dialog.setView(dialogView);
                    dialog.setCancelable(true);

                    //Inisialisasi Komponen Alert Dialog
                    etpasswordblowfishformdownload = dialogView.findViewById(R.id.etpasswordblowfishformdownload);

                    //Listener untuk Download Button
                    dialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog = ProgressDialog.show(getActivity(), "Proses Download File", "Silahkan Menunggu..");
                            //Menampung Password Blowfish yang Diinputkan Pengguna
                            String passwordblowfish = etpasswordblowfishformdownload.getText().toString().trim();

                            //Proses Pengambilan Kunci RSA Pengguna dari API
                            Call<ResponseGetKunciRSA> getKunciRSACall = dataService.apiGetkuncirsa(token_pengguna, id_pengguna);
                            getKunciRSACall.enqueue(new Callback<ResponseGetKunciRSA>() {
                                @Override
                                public void onResponse(Call<ResponseGetKunciRSA> call, Response<ResponseGetKunciRSA> response) {
                                    //Menampung Kunci RSA Pengguna di Dalam List
                                    List<KunciRSA> kunciRSA = (List<KunciRSA>) response.body().getKunci_rsa();
                                    //Menampung Nilai Kunci RSA
                                    String kunciprivate = "";
                                    try {
                                        kunciprivate = sharedPreferencesEncUtils.getEncryptedSharedPreferences(masterKeyAlias, getContext()).getString("kunci_private", "");
                                    } catch (GeneralSecurityException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String kuncipublic = kunciRSA.get(0).getKunci_public();
                                    String kuncimodulus = kunciRSA.get(0).getKunci_modulus();

                                    //Inisialisasi Class RSA
                                    rsa = new RSA(new BigInteger(kunciprivate), new BigInteger(kuncipublic), new BigInteger(kuncimodulus));

                                    //Menampung Hasil Dekripsi RSA dari Kunci File
                                    String hasildekrip = rsa.decrypt(downloadfilePenggunas.get(0).getKunci_file());

                                    //Pengecekan Apakah Aplikasi Sudah Diberi Izin Akses Membaca dan Menulis Penyimpanan
                                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                                        //Proses Pengecekan Apakah Password File yang Diinputkan Pengguna Sama Dengan Password File Yang Sudah Didekripsi
                                        if (hasildekrip.equals(passwordblowfish)){
                                            //Inisialisasi dan Menjanlankan Download Manager dengan Parameter URL File yang akan di Download
                                            downloadManagerrequest = new DownloadManager.Request(Uri.parse(urlfileEnkripsi));
                                            downloadManagerrequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                                    .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS , downloadfilePenggunas.get(0).getNama_file())
                                                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                                            downloadManager.enqueue(downloadManagerrequest);

                                            //Menampung Nama File Asli, Sebelum Didekripsi, Dan Setelah Didekripsi
                                            String namafile = URLUtil.guessFileName(urlfileEnkripsi, null, MimeTypeMap.getFileExtensionFromUrl(urlfileEnkripsi));
                                            String namafilesebelumdidekripsi = Environment.getExternalStorageDirectory() + File.separator + DIRECTORY_DOWNLOADS + File.separator + namafile;
                                            String namafilesetelahdidekripsi = Environment.getExternalStorageDirectory() + File.separator + DIRECTORY_DOWNLOADS + File.separator + namafile.replace(".enc", "");

                                            //Listener Ketika File yang Terenkripsi Sudah Selesai di Download
                                            BroadcastReceiver selesaiDownload = new BroadcastReceiver() {
                                                public void onReceive(Context ctx, Intent intent) {

                                                    blowfishdekripsi = new Blowfish(downloadfilePenggunas.get(0).getKunci_file());
                                                    blowfishdekripsi.decrypt(namafilesebelumdidekripsi, namafilesetelahdidekripsi);
                                                }
                                            };
                                            getActivity().registerReceiver(selesaiDownload, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                            Toast.makeText(getContext(), "Proses Unduh File Selesai", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getContext(), "Pastikan Password Benar", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        deteksiPermissionandroid();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseGetKunciRSA> call, Throwable t) {
                                    Toast.makeText(getContext(), "Gagal Mendekripsi File", Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressDialog.dismiss();
                            dialog.dismiss();
                        }
                    });

                    //Listener untuk Download Button
                    dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
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

    //Method Untuk Memeriksa Akses Membaca dan Menulis Penyimpanan
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
    }

    //Method Untuk Menjalankan Intent/Activity Pemilihan File
    private void pilihFile() {
        String[] tipeFile =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "text/plain",
                        "application/pdf"};

        Intent intentPilihfile = new Intent(Intent.ACTION_GET_CONTENT);
        intentPilihfile.addCategory(Intent.CATEGORY_OPENABLE);
        intentPilihfile.setType("*/*");
        intentPilihfile.putExtra(Intent.EXTRA_MIME_TYPES, tipeFile);
        startActivityForResult(Intent.createChooser(intentPilihfile,"Pilih File"), MY_RESULT_CODE_FILECHOOSER);
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

                        String filePath = null;
                        try {
                            filePath = FileUtils.getPath(getContext(),fileUri);
                        } catch (Exception e) {
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