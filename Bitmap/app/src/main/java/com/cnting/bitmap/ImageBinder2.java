package com.cnting.bitmap;

import android.graphics.Bitmap;
import android.os.Binder;

/**
 * Created by cnting on 2021/8/30
 */
class ImageBinder2 extends Binder {

    private Bitmap bitmap;

    public ImageBinder2(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
