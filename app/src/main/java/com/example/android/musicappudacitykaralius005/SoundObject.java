package com.example.android.musicappudacitykaralius005;

/**
 * Created by Justas on 3/24/2018.
 */

public class SoundObject {
    String mArtist,
    mTitle,
    mDuration,
    mDurationInMili;
    public SoundObject(String artist, String title, String duration, String durationInMili)
    {
        mArtist = artist;
        mTitle= title;
        mDuration = duration;
        mDurationInMili = durationInMili;
    }

    public String getmArtist (){return mArtist;}

    public String getmDuration() {
        return mDuration;
    }

    public String getmDurationInMili() {
        return mDurationInMili;
    }

    public String getmTitle() {
        return mTitle;
    }
}
