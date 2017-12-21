package com.ecjia.component.network.responsmodel.goodinfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-02.
 */

public class GoodDescImage implements Parcelable {

    private String file_id;

    private String file_path;

    private String img;

    private String isdel;

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public GoodDescImage() {
    }

    protected GoodDescImage(Parcel in) {
        file_id = in.readString();
        file_path = in.readString();
        img = in.readString();
        isdel = in.readString();
    }

    public static final Creator<GoodDescImage> CREATOR = new Creator<GoodDescImage>() {
        @Override
        public GoodDescImage createFromParcel(Parcel in) {
            return new GoodDescImage(in);
        }

        @Override
        public GoodDescImage[] newArray(int size) {
            return new GoodDescImage[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(file_id);
        dest.writeString(file_path);
        dest.writeString(img);
        dest.writeString(isdel);
    }
}
