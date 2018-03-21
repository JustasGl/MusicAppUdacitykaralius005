package com.example.android.musicappudacitykaralius005;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static int times;
    ListView listView;
    static ArrayList<String> arrayList;
    static ArrayList<String> arrayTitles;
    static ArrayList<String> arrayLocations;
    static ArrayList<String> arrayDuration;
    static ArrayList<String> arrayDurationints;
    static ArrayList<Integer> images;
    ArrayAdapter<String> adapter;
    LinearLayout titles;
    static MediaPlayer mp;
   static int current=0;
    static boolean ispaused=true;
    int restore;
    static TextView artist;
    static TextView track;
    static ImageView pause,back,forward;
    private static final int MY_PERMISION_REQUEST = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = new MediaPlayer();
        artist = findViewById(R.id.artist);
        track= findViewById(R.id.track);
        pause =findViewById(R.id.pause);
        back = findViewById(R.id.back);
        forward = findViewById(R.id.forward);

        titles = findViewById(R.id.titles);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISION_REQUEST);
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISION_REQUEST);
            }
        }
        else { dostuff();
        }
    }
    public void dostuff()
    {
        listView = findViewById(R.id.listview);
        arrayList = new ArrayList<>();
        arrayLocations = new ArrayList<>();
        arrayTitles = new ArrayList<>();
        arrayDuration = new ArrayList<>();
        arrayDurationints = new ArrayList<>();
        images = new ArrayList<Integer>();
        getMusic();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayTitles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                try{
                    mp.reset();
                    if(times>0)
                        if(releaseMediaPlayer())
                            mp = new MediaPlayer();
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.setDataSource(arrayLocations.get(pos));
                    mp.prepare();
                    mp.start();
                    current = pos;
                    restore = current;
                   setText();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "exeption catched"+ e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              pause();
            }
        });
        titles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, player.class);
                intent.putExtra("artist",arrayList.get(current));
                intent.putExtra("titles",arrayTitles.get(current));
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    previousSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nextsong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getMusic()
    {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null,null,null,null);
        if(songCursor != null && songCursor.moveToFirst())
        {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int du = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int img = (songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));

            do{
                arrayList.add(songCursor.getString(songArtist));
                arrayTitles.add(songCursor.getString(songTitle));
                arrayLocations.add(songCursor.getString(songLocation));
                String duration = convertDuration(Integer.parseInt(songCursor.getString(du)));
                arrayDuration.add(duration);
                arrayDurationints.add(songCursor.getString(du));
                images.add(img);
            }while(songCursor.moveToNext());
        }
    }
    public static void pause()
    {
        if(!ispaused&& mp!= null) {
            mp.pause();
            ispaused=true;
            pause.setImageResource(R.drawable.ic_play);
        }
        else {
            if(mp==null)
                mp = new MediaPlayer();
            mp.start();
            ispaused = false;
            pause.setImageResource(R.drawable.ic_pause);
        }
    }
    static public void previousSong() throws IOException {
        if(current==0)
            current=arrayLocations.size()-1;
        else
            current--;
        if(releaseMediaPlayer())
            mp = new MediaPlayer();
        if(mp!=null) {
            mp.setDataSource(arrayLocations.get(current));
            mp.prepare();
            mp.start();
            setText();
        }
    }
    static public void nextsong() throws IOException {
        if(current<arrayLocations.size()-1)
            current++;
        else
            current=0;
        if(releaseMediaPlayer())
            mp = new MediaPlayer();
        if (mp!=null) {
            mp.setDataSource(arrayLocations.get(current));
            mp.prepare();
            mp.start();
            setText();
        }

    }
    public String convertDuration(int duration) {
        String out = null;
        long hours=0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;

    }
    static public void setText()
    {
        pause.setImageResource(R.drawable.ic_pause);
        times++;
        track.setText(arrayTitles.get(current));
        artist.setText(arrayList.get(current));
        ispaused=false;
        player.titl = arrayTitles.get(current);
        player.artis = arrayList.get(current);
    }
   static private boolean releaseMediaPlayer() {
        if (mp != null) {
            mp.reset();
            mp = null;
            return true;
        }
        return false;
    }
    static public void seek(int mseconds)
    {
        mp.seekTo(mseconds);
    }

}
