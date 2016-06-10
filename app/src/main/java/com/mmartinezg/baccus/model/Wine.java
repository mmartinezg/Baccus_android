package com.mmartinezg.baccus.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class Wine implements Serializable{
    private String mId = null;
    private String mName = null;
    private String mType = null;
    private Bitmap mPhoto = null;
    private String mPhotoURL = null;
    private String mComanyName = null;
    private String mComanyWeb = null;
    private String mNotes = null;
    private String mOrigin = null;
    private int mRagin = 0; // 0 to 5
    private List<String> mGrapes = new LinkedList<>();

    public Wine(String id, String name, String type, String photoURL, String comanyName, String comanyWeb, String notes, String origin, int ragin) {
        mId = id;
        mName = name;
        mType = type;
        mPhotoURL = photoURL;
        mComanyName = comanyName;
        mComanyWeb = comanyWeb;
        mNotes = notes;
        mOrigin = origin;
        mRagin = ragin;
    }

    public String getId() {
        return mId;
    }

    public String getPhotoURL() {
        return mPhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        mPhotoURL = photoURL;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Bitmap getPhoto(Context context) {
        if(mPhoto == null) {
            mPhoto = getBitmapFromURL(getPhotoURL(), context);
        }
        return mPhoto;
    }

    private Bitmap getBitmapFromURL(String photoURL, Context context) {
        File imageFile = new File(context.getCacheDir(),getId());
        if(imageFile.exists()){
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }

        InputStream in = null;
        try{
            in = new URL(photoURL).openStream();
            Bitmap bmp =  BitmapFactory.decodeStream(in);

            //Lo guardamos a cache
            FileOutputStream fos = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
            return bmp;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }finally {
            try {
                if(in != null){
                    in.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void setPhoto(Bitmap photo) {
        mPhoto = photo;
    }

    public String getComanyName() {
        return mComanyName;
    }

    public void setComanyName(String comanyName) {
        mComanyName = comanyName;
    }

    public String getComanyWeb() {
        return mComanyWeb;
    }

    public void setComanyWeb(String comanyWeb) {
        mComanyWeb = comanyWeb;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String origin) {
        mOrigin = origin;
    }

    public int getRagin() {
        return mRagin;
    }

    public void setRagin(int ragin) {
        mRagin = ragin;
    }

    public void addGrape(String grape){
        mGrapes.add(grape);
    }

    public int getGrapesCount(){
        return mGrapes.size();
    }

    public String getGrape(int index){
        return mGrapes.get(index);
    }

    @Override
    public String toString() {
        return getName();
    }
}
