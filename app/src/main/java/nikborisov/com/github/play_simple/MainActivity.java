package nikborisov.com.github.play_simple;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static File dirName = Environment.getExternalStorageDirectory();
    private final Handler handler = new Handler();
    private Button playFrom;
    private Button buttonPausePlay;
    private SeekBar songSeeek;
    private MediaPlayer mainPlayer;
    private int currenSongNumber = -1;
    private TextView currentTitleInfo;
    private TextView totalPlayingTime;
    private TextView currentPlayingTime;
    private ListView songListView;
    private File[] currentDirAllFiles = ServiceProvider
            .fileNamesAgregator(MainActivity.dirName)
            .toArray(new File[ServiceProvider.fileNamesAgregator(MainActivity.dirName).size()]);

    public static void setDirName(File dirName) {
        MainActivity.dirName = dirName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerInitialization();
    }

    /**
     * setup Player to valid condition
     */
    public void playerInitialization() {
        playFrom = (Button) findViewById(R.id.selectDirBut);
        buttonPausePlay = (Button) findViewById(R.id.pausePlaySong);
        songSeeek = (SeekBar) findViewById(R.id.seekBar);
        currentTitleInfo = (TextView) findViewById(R.id.songTitle);
        totalPlayingTime = (TextView) findViewById(R.id.totalTime);
        currentPlayingTime = (TextView) findViewById(R.id.currentTime);
        songListView = (ListView) findViewById(R.id.songList);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                ServiceProvider.getMediaFiles(dirName));
        songListView.setAdapter(listAdapter);

        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currenSongNumber = position;
                startPlaying(position);
            }
        });

    }

    /**
     * handler for seekBar
     */
    private void changeCurrentSeek(View view) {
        if (mainPlayer.isPlaying()) {
            SeekBar sb = (SeekBar) view;
            mainPlayer.seekTo(sb.getProgress());
        }
    }

    /**
     * play and pause actions invoke
     */
    public void pausePlay(View view) {
        if (mainPlayer != null) {
            if (buttonPausePlay.getText() == getString(R.string.playString)) {
                try {
                    //next line syns player with seekBar
                    mainPlayer.seekTo(songSeeek.getProgress());
                    mainPlayer.start();
                    buttonPausePlay.setText((R.string.pauseString));
                    progress();

                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                }
            } else {
                buttonPausePlay.setText(R.string.playString);
                mainPlayer.pause();
            }
        } else {
            currenSongNumber++;
        }
    }

    /**
     * playing progress, update seekbar condition and current playback time
     */
    public void progress() {
        songSeeek.setProgress(mainPlayer.getCurrentPosition());
        currentPlayingTime.setText(ServiceProvider.formatPlaybackTime(mainPlayer.getCurrentPosition()));
        if (mainPlayer.isPlaying()) {
            Runnable runner = new Runnable() {
                @Override
                public void run() {
                    progress();
                }
            };
            handler.postDelayed(runner, 500);
        } else {
            mainPlayer.pause();
            buttonPausePlay.setText(R.string.playString);
            songSeeek.setProgress(mainPlayer.getCurrentPosition());
        }
    }

    /**
     * method performing stop action
     * work correct now
     */
    public void stopAction(View view) {
        if (mainPlayer != null) {
            songSeeek.setProgress(0);
            mainPlayer.seekTo(songSeeek.getProgress());
            currentPlayingTime.setText(ServiceProvider.formatPlaybackTime(mainPlayer.getCurrentPosition()));
            mainPlayer.pause();
        }
    }

    /**
     * make "next" onClick action
     */
    public void next(View view) {
        if (currenSongNumber < currentDirAllFiles.length - 1) {
            currenSongNumber++;
            startPlaying(currenSongNumber);
        } else {
            currenSongNumber = 0;
            startPlaying(currenSongNumber);
        }
    }

    /**
     * make prev onClick action
     */
    public void prev(View view) {
        if (currenSongNumber > 0) {
            currenSongNumber--;
            startPlaying(currenSongNumber);
        } else {
            currenSongNumber = currentDirAllFiles.length - 1;
            startPlaying(currenSongNumber);
        }
    }

    /**
     * switch played song to next or prev
     */
    public void startPlaying(int songId) {
        if (mainPlayer != null) {
            if (mainPlayer.isPlaying()) {
                mainPlayer.stop();
            }
        }
        mainPlayer = MediaPlayer.create(MainActivity.this, Uri.fromFile(currentDirAllFiles[songId]));
        totalPlayingTime.setText(ServiceProvider.formatPlaybackTime(mainPlayer.getDuration()));
        songSeeek.setMax(mainPlayer.getDuration());
        songSeeek.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent currentEvent) {
                changeCurrentSeek(view);
                return false;
            }
        });

        currentPlayingTime.setText(ServiceProvider.formatPlaybackTime(mainPlayer.getCurrentPosition()));
        songSeeek.setProgress(mainPlayer.getCurrentPosition());

        mainPlayer.start();
        currentTitleInfo.setText(new TitleExtractor(Uri.fromFile(currentDirAllFiles[songId])).getTitleInfo());
        buttonPausePlay.setText(R.string.pauseString);
        pausePlay(buttonPausePlay);
    }


    public void openFileBrowser(View view) {
        Intent openBrowserIntent = new Intent(this, FileBrowserActivity.class);
        startActivity(openBrowserIntent);
    }

}