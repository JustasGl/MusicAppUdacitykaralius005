package com.example.android.musicappudacitykaralius005;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MoreInfo extends AppCompatActivity {
    static TextView artist;
    static TextView title;
    static TextView location;
    static TextView duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);
        artist = findViewById(R.id.artist);
        title = findViewById(R.id.filename);
        location = findViewById(R.id.filelocation);
        duration = findViewById(R.id.duration);

        setText();
    }

    public static void setText() {
        artist.setText(MainActivity.SongInfo.get(MainActivity.current).getmArtist());
        title.setText(MainActivity.SongInfo.get(MainActivity.current).getmTitle());
        if(MainActivity.IsThereMusic)
        location.setText(MainActivity.arrayLocations.get(MainActivity.current));
        else location.setText("Built in aplication");
        duration.setText(MainActivity.SongInfo.get(MainActivity.current).getmDuration());
    }
}
