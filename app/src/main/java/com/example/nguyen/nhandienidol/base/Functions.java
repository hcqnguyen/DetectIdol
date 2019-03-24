package com.example.nguyen.nhandienidol.base;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Functions {

    public static Functions share = new Functions();


    public File persistImage(Bitmap bitmap, File oldFile, int quality) {
        File imageFile = new File(oldFile + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("TAG", "Error writing bitmap", e);
        }
        return imageFile;
    }

    public float getFileSizeInMB(File fileName) {
        float ret = getFileSizeInBytes(fileName);
        ret = ret / (float) (1024 * 1024);
        return ret;
    }

    private static long getFileSizeInBytes(File f) {
        long ret = 0;
        if (f.isFile()) {
            return f.length();
        } else if (f.isDirectory()) {
            File[] contents = f.listFiles();
            for (File content : contents) {
                if (content.isFile()) {
                    ret += content.length();
                } else if (content.isDirectory())
                    ret += getFileSizeInBytes(content);
            }
        }
        return ret;
    }
}
