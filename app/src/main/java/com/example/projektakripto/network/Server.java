package com.example.projektakripto.network;

public class Server {
    //Inisialisasi Base Url Server Penyimpanan
    public static String BASE_URL = "http://192.168.100.13/ProjekTAKripto/";

    //Inisialisasi Endpoint API Aplikasi
    public static final String API_INFO_APLIKASI = "api/infoaplikasi";

    public static final String API_MASUK = "api/masukpengguna";
    public static final String API_DAFTAR = "api/daftarpengguna";
    public static final String API_GANTIPASSWORD = "api/gantipasswordpengguna";
    public static final String API_KELUAR = "api/keluarpengguna";

    public static final String API_GETKUNCIRSA = "api/operasikunci/getkuncirsa/";

    public static final String API_UPLOADFILE = "api/operasifile/uploadfile";
    public static final String API_DELETEFILE = "api/operasifile/deletefile";
    public static final String API_GETFILE = "api/operasifile/getfile/";
    public static final String API_DOWNLOADFILE = "api/operasifile/downloadfile/";
}
