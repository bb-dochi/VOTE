package com.example.user.vote;

import android.graphics.Bitmap;

/**
 * Created by USER on 2017-11-24.
 */

public class ListViewItem {
    private Bitmap bitmap;

    public String getListmemo() {
        return listmemo;
    }

    public void setListmemo(String listmemo) {
        this.listmemo = listmemo;
    }

    private String listmemo;


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


}
