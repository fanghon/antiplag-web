package cn.mooyyu.antiplagweb.util;

import java.io.*;

public class FileIO {
    //将str以指定编码方式，写入文件
    public static void saveFile(File outfile, String str, String encode) {
        BufferedWriter fr = null;
        try {
            fr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile, true), encode));
            fr.write(str);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fr != null)
                    fr.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}