package cn.dujc.dg.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 遍历删除文件工具类
 * Created by Du JC on 2016/9/27.
 */
public class DeleteFile {

    private List<File> mAllFiles = new ArrayList<>();
    private boolean mDeleteSingleFile = false;

    private DeleteFile(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                mAllFiles.addAll(listAllFiles(file));
            } else mDeleteSingleFile = file.delete();
        }
    }

    private boolean delete() {
        boolean delete = true;
        for (int index = 0, size = mAllFiles.size(); index < size; index++) {
            File file = mAllFiles.get(index);
            if (file != null && file.exists()) {
                delete = delete && file.delete();
            }
        }
        return delete || mDeleteSingleFile;
    }

    private static List<File> listAllFiles(File file) {
        List<File> allFiles = new ArrayList<>();
        if (file != null) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File f : listFiles) {
                        allFiles.addAll(listAllFiles(f));
                    }
                }
            }
            allFiles.add(file);
        }
        return allFiles;
    }

    public static boolean delete(String file) {
        return delete(new File(file));
    }

    public static boolean delete(File file) {
        return new DeleteFile(file).delete();
    }
}
