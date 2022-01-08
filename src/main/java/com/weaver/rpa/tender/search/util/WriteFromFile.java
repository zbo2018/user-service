package com.weaver.rpa.tender.search.util;

import java.io.*;

public class WriteFromFile {
    /**
     * 写入指定的文本文件
     * @param filePath 文件路径
     * @param append append为true表示追加，false表示重头开始写，
     * @param text 要写入的文本字符串，text为null时直接返回
     */
    public static void write(String filePath, boolean append, String text) {  
        if (text == null)  
            return;  
        try {  
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath,  
                    append));  
            try {  
                out.write(text);  
            } finally {  
                out.close();  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    
    // 把字节数组为写入二进制文件，数组为null时直接返回  
    public static void write(String filePath, byte[] data) {  
        if (data == null)  
            return;  
        try {  
            BufferedOutputStream out = new BufferedOutputStream(  
                    new FileOutputStream(filePath));  
            try {  
                out.write(data);  
            } finally {  
                out.close();  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }

    /**
     * 将输入流，写入文件
     * @param inputStream 输入流
     * @param filePath 文件路径
     */
    public static void inputStreamToFile(InputStream inputStream, String filePath) {
        OutputStream outputStream = null;
        try {
            File file = new File(filePath);
            outputStream = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
