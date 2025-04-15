package cn.mooyyu.antiplagweb.util;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TextExtractor {
    public static String convertToText(File f) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(f);
            Tika tika = new Tika();
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName()); //避免gbk编码txt文本提取错误
            return tika.parseToString(new FileInputStream(f),metadata);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}