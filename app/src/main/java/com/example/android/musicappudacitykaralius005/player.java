package com.example.android.musicappudacitykaralius005;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;

public class Player extends AppCompatActivity {
    static String titl;//title of the song
    static String artis;//artist name
    static TextView title;//Title TextView
    static TextView artist;
    static SeekBar sek;//Seekbar for song progess
    ImageView pause,//pause button
            back,//Back button
            forward,//Forward button
            pic;//Cover Image

    static public void setText() {
        title.setText(titl);
        artist.setText(artis);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_player);
        android.support.v7.widget.Toolbar myToolbar =findViewById(R.id.toolbar);
        myToolbar.setTitle(MainActivity.SongInfo.get(MainActivity.current).getmTitle());
        setSupportActionBar(myToolbar);
        title = findViewById(R.id.title);
        artist = findViewById(R.id.artist);
        pic = findViewById(R.id.pic);
        settingpic();
        titl = getIntent().getStringExtra(getString(R.string.titles));
        artis = getIntent().getStringExtra(getString(R.string.artistKey));
        setText();
        back = findViewById(R.id.back);
        forward = findViewById(R.id.forward);
        pause = findViewById(R.id.pause);
        if (MainActivity.ispaused)
            pause.setImageResource(R.drawable.ic_play);
        else
            pause.setImageResource(R.drawable.ic_pause);
        settingmax();
        final Handler mHandler = new Handler();
        Player.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.mp != null) {
                    sek.setProgress(MainActivity.mp.getCurrentPosition());
                }
                mHandler.postDelayed(this, 1000);
            }
        });
        sek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (MainActivity.mp != null && b) {
                    MainActivity.mp.seekTo(i);
                    MainActivity.length = i;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.nextsong(getApplicationContext());
                    setText();
                    settingmax();
                    settingpic();
                    pause.setImageResource(R.drawable.ic_pause);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.pause();
                if (MainActivity.ispaused)
                    pause.setImageResource(R.drawable.ic_play);
                else
                    pause.setImageResource(R.drawable.ic_pause);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.previousSong(getApplicationContext());
                    setText();
                    pause.setImageResource(R.drawable.ic_pause);
                    settingmax();
                    settingpic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
       title.setText(MainActivity.SongInfo.get(MainActivity.current).getmTitle());
       artist.setText(MainActivity.SongInfo.get(MainActivity.current).getmArtist());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.info) {
            Intent intent = new Intent(Player.this, MoreInfo.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void settingpic() {
        if (MainActivity.IsThereMusic) {
            Bitmap btm = getAlbumart(MainActivity.images.get(MainActivity.current));
            pic.setImageBitmap(btm);
        } else pic.setImageResource(R.drawable.udacity);
    }

    void settingmax() {
        sek = findViewById(R.id.seek);
        sek.setMax(MainActivity.mp.getDuration());
    }

    public Bitmap getAlbumart(Integer album_id) {
        Bitmap bm = null;
        try {
            final Uri sArtworkUri = Uri
                    .parse(getString(R.string.bitmapURI));

            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);

            ParcelFileDescriptor pfd = getApplicationContext().getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (Exception e) {
        }
        return bm;
    }
}
