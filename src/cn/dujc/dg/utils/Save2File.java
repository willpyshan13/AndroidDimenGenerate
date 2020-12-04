package cn.dujc.dg.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * 保存到文件
 * Created by Du JC on 2016/6/12.
 */
public class Save2File {
    private Save2File(File file, String content) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("file不能为文件夹");
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(file));
            pw.print(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) pw.close();
        }
    }

    public static void save(File file, String content) {
        new Save2File(file, content);
    }
}
