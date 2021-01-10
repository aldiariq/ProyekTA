package com.aldiariq.projektakripto.algoritma.avalancheeffect;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

public class AvalancheEffect {

    private String fileinput;
    private String fileoutput;

    public AvalancheEffect(String fileinput, String fileoutput){
        this.fileinput = fileinput;
        this.fileoutput = fileoutput;
    }

    public String getFileinput() {
        return fileinput;
    }

    public String getFileoutput() {
        return fileoutput;
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
        byte[] bytefileInput = this.ambilBytefile(new File(this.getFileinput()));
        byte[] bytefileOutput = this.ambilBytefile(new File(this.getFileoutput()));
        int penghitung = 0;
        int pembeda = 0;

        for (int i = 0;i < bytefileInput.length; i++){
            String bilsatu = Integer.toBinaryString(bytefileInput[i] & 255 | 256);
            String bildua = Integer.toBinaryString(bytefileOutput[i] & 255 | 256);

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