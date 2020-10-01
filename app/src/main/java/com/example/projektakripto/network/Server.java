package com.example.projektakripto.network;

public class Server {
    //Inisialisasi Base Url Server Penyimpanan
    public static String BASE_URL = "http://10.1.8.109/ProjekTAKripto/api/";

    //Inisialisasi Endpoint API Aplikasi
    public static final String API_INFO_APLIKASI = "infoaplikasi";

    public static final String API_MASUK = "masukpengguna";
    public static final String API_DAFTAR = "daftarpengguna";
    public static final String API_GANTIPASSWORD = "gantipasswordpengguna";
    public static final String API_KELUAR = "keluarpengguna";

    public static final String API_GETKUNCIRSA = "operasikunci/getkuncirsa";

    public static final String API_UPLOADFILE = "opelrasifile/uploadfile";
    public static final String API_DELETEFILE = "operasifile/deletefile";
    public static final String API_GETFILE = "operasifile/getfile";
    public static final String API_DOWNLOADFILE = "operasifile/downloadfile";
}
