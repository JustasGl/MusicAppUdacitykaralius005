package com.example.android.musicappudacitykaralius005;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISION_REQUEST = 1;
    static int times;
    static ArrayList<String> arrayLocations;
    SoundObject soundObject;
    static ArrayList<SoundObject> SongInfo;
    static ArrayList<Integer> images;
    static ArrayList <Integer> sounds;
    static MediaPlayer mp;
    static boolean IsThereMusic = true; //Checks is there music in your phone
    static int current = 0;
    static boolean ispaused = true;
    static TextView artist;
    static TextView track;
    static ImageView pause, back, forward;
    ListView listView;
    ArrayAdapter<String> adapter;
    LinearLayout titles;
    int restore;

    public static void pause() {
        if (!ispaused && mp != null) {
            mp.pause();
            ispaused = true;
            pause.setImageResource(R.drawable.ic_play);
        } else {
            if (mp == null)
                mp = new MediaPlayer();
            mp.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            ispaused = false;
            pause.setImageResource(R.drawable.ic_pause);
        }
    }

    static public void previousSong() throws IOException {
        if(IsThereMusic)
            if (current == 0)
                current = arrayLocations.size() - 1;
            else
                current--;
        if (releaseMediaPlayer())
            mp = new MediaPlayer();
        if (mp != null) {
            if(IsThereMusic) {
                if (mp.isPlaying()) {
                    mp.pause();
                }
                mp.prepare();
                mp.setOnPreparedListener(new OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.selectTrack(current);
                        mediaPlayer.start();
                    }
                });
            }
            else {

                mp.prepare();
                mp.setOnPreparedListener(new OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.selectTrack(current);
                        mediaPlayer.start();
                    }
                });

            }
            mp.prepare();
            mp.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            setText();
        }
    }

    static public void nextsong() throws IOException {
        if(IsThereMusic)
            if (current < arrayLocations.size() - 1)
                current++;
            else
                current = 0;
        if (releaseMediaPlayer())
            mp = new MediaPlayer();
        if (mp != null) {
            if (IsThereMusic)
                mp.setDataSource(arrayLocations.get(current));
            else {
                mp.setDataSource(String.valueOf(sounds.get(current)));
            }
            mp.prepare();
            mp.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            setText();
        }
        else {
            if (current < arrayLocations.size() - 1)
                current++;
            else
            {
                current = 0;
            }
            mp.selectTrack(sounds.get(current));
            mp.prepare();
            mp.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            setText();
        }

    }

    static public void setText() {
        pause.setImageResource(R.drawable.ic_pause);
        times++;
        track.setText(SongInfo.get(current).getmTitle());
        artist.setText(SongInfo.get(current).getmArtist());
        ispaused = false;
        Player.titl = SongInfo.get(current).getmTitle();
        Player.artis = SongInfo.get(current).getmArtist();
    }

    static private boolean releaseMediaPlayer() {
        if (mp != null) {
            mp.reset();
            mp = null;
            return true;
        }
        return false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = new MediaPlayer();
        artist = findViewById(R.id.artist);
        track = findViewById(R.id.track);
        pause = findViewById(R.id.pause);
        back = findViewById(R.id.back);
        forward = findViewById(R.id.forward);

        titles = findViewById(R.id.titles);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISION_REQUEST);
            }
        } else {
            dostuff();
        }
        mp = new MediaPlayer();
        if(arrayLocations.size()==0 || arrayLocations.isEmpty())
        {
            sounds = new ArrayList<>();
            IsThereMusic = false;
            sounds.add(R.raw.a50_cent_ayo_technologytwonottyremix);
            soundObject = new SoundObject(getString(R.string.Song0Artist),getString(R.string.song0),getString(R.string.song0duration),getString(R.string.Song0DurationInMili));
            SongInfo.add(soundObject);

            sounds.add(R.raw.arctic_monkeys_do_i_wanna_know_official_video);
            soundObject = new SoundObject(getString(R.string.Song1Artist),getString(R.string.Song1),getString(R.string.Song1Duration),getString(R.string.Song1DurationInMili));
            SongInfo.add(soundObject);

            sounds.add(R.raw.in_my_mind_dynoro_remix);
            soundObject = new SoundObject(getString(R.string.Song2Artist),getString(R.string.Song2),getString(R.string.Song2Duration),getString(R.string.Song2DurationInMili));
            SongInfo.add(soundObject);

            sounds.add(R.raw.jovani_feat_beissoul_einius_adopted_child_of_love);
            soundObject = new SoundObject(getString(R.string.Song3Artist),getString(R.string.Song3),getString(R.string.Song3Duration),getString(R.string.Song3DurationInMili));
            SongInfo.add(soundObject);

            sounds.add(R.raw.lika_morgan_feel_the_same_edxs_dubai_skyline_remix);
            soundObject = new SoundObject(getString(R.string.Song4Artist),getString(R.string.Song4),getString(R.string.Song4Duration),getString(R.string.Song4DurationInMili));
            SongInfo.add(soundObject);

            sounds.add(R.raw.liu_step_head_feat_vano);
            soundObject = new SoundObject(getString(R.string.Song5Artist),getString(R.string.Song5),getString(R.string.Song5Duration),getString(R.string.Song5DurationInMili));
            SongInfo.add(soundObject);

            sounds.add(R.raw.lucky_luke_m_a_d_e);
            soundObject = new SoundObject(getString(R.string.Song6Artist),getString(R.string.Song6),getString(R.string.Song6Duration),getString(R.string.Song6DurationInMili));
            SongInfo.add(soundObject);

            sounds.add(R.raw.martin_garrix_david_guetta_so_far_away_feat_jamie_scott_romy_rya_official_video);
            soundObject = new SoundObject(getString(R.string.Song7Artist),getString(R.string.Song7),getString(R.string.Song7Duration),getString(R.string.Song7DurationInMili));
            SongInfo.add(soundObject);

            ArrayList<String>Titles = new ArrayList<>();
            for(int i=0; i<SongInfo.size(); i++)
            {
                Titles.add(SongInfo.get(i).getmTitle());
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Titles);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Toast.makeText(getApplicationContext(), "ListItem clicked",Toast.LENGTH_SHORT).show();
                    try {
                        mp.reset();
                        if (times > 0)
                            if (releaseMediaPlayer())
                                mp = new MediaPlayer();
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        current = pos;
                        restore = current;
                        setText();
                      mp.setOnPreparedListener(new OnPreparedListener() {
                          @Override
                          public void onPrepared(MediaPlayer mediaPlayer) {
                              mediaPlayer.selectTrack(sounds.get(current));
                              mediaPlayer.start();
                              Toast.makeText(getApplicationContext(), "OnPrepared called",Toast.LENGTH_SHORT).show();
                          }
                      });
                    } catch (Exception e) {
                        Log.e("","",e);
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void dostuff() {
        listView = findViewById(R.id.listview);
        SongInfo = new ArrayList<>();
        arrayLocations = new ArrayList<>();

        images = new ArrayList<>();
        getMusic();
        ArrayList<String>Titles = new ArrayList<>();
        for(int i=0; i<SongInfo.size(); i++)
        {
            Titles.add(SongInfo.get(i).getmTitle());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Titles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                try {
                    mp.reset();
                    if (times > 0)
                        if (releaseMediaPlayer())
                            mp = new MediaPlayer();
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    if(IsThereMusic)
                        mp.setDataSource(arrayLocations.get(pos));
                    else
                        mp.selectTrack(sounds.get(current));
                    mp.prepare();
                    mp.setOnPreparedListener(new OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();

                        }
                    });
                    current = pos;
                    restore = current;
                    setText();
                } catch (Exception e) {
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
                Intent PlayerActivity = new Intent(MainActivity.this, Player.class);
                PlayerActivity.putExtra(getString(R.string.artistKey),SongInfo.get(current).getmArtist());
                PlayerActivity.putExtra(getString(R.string.titles),SongInfo.get(current).getmTitle());
                startActivity(PlayerActivity);
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

    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int du = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int img = (songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));

            do {
                arrayLocations.add(songCursor.getString(songLocation));
                String duration = convertDuration(Integer.parseInt(songCursor.getString(du)));
                soundObject = new SoundObject(songCursor.getString(songArtist),songCursor.getString(songTitle),duration,songCursor.getString(du));
                SongInfo.add(soundObject);

                images.add(img);
            } while (songCursor.moveToNext());
        }
    }

    public String convertDuration(int duration) {
        String out = null;
        long hours = 0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = getString(R.string.zeros);
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = getString(R.string.zeros);
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + getString(R.string.seperator) + minutes + getString(R.string.seperator) + seconds;
        } else {
            out = minutes + getString(R.string.seperator) + seconds;
        }

        return out;

    }

}