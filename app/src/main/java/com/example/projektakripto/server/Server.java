package com.example.projektakripto.server;

public class Server {
    //Inisialisasi Base Url Server Penyimpanan
    public static String BASE_URL = "http://192.168.100.11/ProjekTAKripto/api/";

    //Inisialisasi Endpoint API Aplikasi
    public static final String API_INFO_APLIKASI = "infoaplikasi";

    public static final String API_MASUK = "masukpengguna";
    public static final String API_DAFTAR = "daftarpengguna";
    public static final String API_KELUAR = "keluarpengguna";

    public static final String API_GETKUNCIRSA = "operasikunci/getkuncirsa";

    public static final String API_UPLOADFILE = "operasifile/uploadfile";
    public static final String API_DELETEFILE = "operasifile/deletefile";
    public static final String API_GETFILE = "operasifile/getfile";
    public static final String API_DOWNLOADFILE = "operasifile/downloadfile";
}
