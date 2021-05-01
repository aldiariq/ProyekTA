package com.aldiariq.projektakripto.algoritma.avalancheeffect;

import java.io.File;
import java.io.FileInputStream;

public class AvalancheEffect {

    private String filesatu;
    private String filedua;

    public AvalancheEffect(String filesatu, String filedua){
        this.filesatu = filesatu;
        this.filedua = filedua;
    }

    public String getFilesatu() {
        return filesatu;
    }

    public String getFiledua() {
        return filedua;
    }

    private byte[] ambilBytefile (File file)
    {
        FileInputStream input = null;
        if (file.exists()) try
        {
            input = new FileInputStream (file);
            int len = (int) file.length();
            byte[] data = new byte[len];
            int count, total = 0;
            while ((count = input.read (data, total, len - total)) > 0) total += count;
            return data;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (input != null) try
            {
                input.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public double hitungAvalanche(){
        double tingkatAvalanche;
        byte[] bytefileSatu = this.ambilBytefile(new File(this.getFilesatu()));
        byte[] bytefileDua = this.ambilBytefile(new File(this.getFiledua()));
        int penghitung = 0;
        int pembeda = 0;

        for (int i = 0;i < bytefileSatu.length; i++){
            String bilsatu = Integer.toBinaryString(bytefileSatu[i] & 255 | 256);
            String bildua = Integer.toBinaryString(bytefileDua[i] & 255 | 256);

            for (int j = 0; j < bilsatu.length(); j++){
                String charsatu = bilsatu.substring(j, (j+1));
                String chardua = bildua.substring(j, (j+1));

                if (!charsatu.equals(chardua)){
                    pembeda++;
                }

                penghitung++;
            }
        }

        tingkatAvalanche = ((pembeda * 100) / penghitung);

        return tingkatAvalanche;
    }
}