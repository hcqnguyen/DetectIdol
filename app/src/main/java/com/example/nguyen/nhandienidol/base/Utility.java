package com.example.nguyen.nhandienidol.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Utility {
    public static Utility share = new Utility();

    public File reducingImageQualify(File file) {
        Bitmap original = BitmapFactory.decodeFile(file.toString());
//        int quality = Functions.share.getQuality(file);
        int quality = 50;
        return Functions.share.persistImage(original, file, quality);
    }
}
