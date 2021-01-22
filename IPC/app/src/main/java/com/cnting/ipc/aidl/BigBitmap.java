package com.cnting.ipc.aidl;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cnting on 2021/1/20
 */
public class BigBitmap implements Parcelable {
    Bitmap bitmap;

    public BigBitmap(Bitmap b) {
        this.bitmap = b;
    }

    protected BigBitmap(Parcel in) {
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<BigBitmap> CREATOR = new Creator<BigBitmap>() {
        @Override
        public BigBitmap createFromParcel(Parcel in) {
            return new BigBitmap(in);
        }

        @Override
        public BigBitmap[] newArray(int size) {
            return new BigBitmap[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bitmap, flags);
    }
}
