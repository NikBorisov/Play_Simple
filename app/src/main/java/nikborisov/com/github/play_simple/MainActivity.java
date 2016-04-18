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
    private static MediaPlayer mainPlayer;
    private static File dirName = Environment.getExternalStorageDirectory();
    private final Handler handler = new Handler();
    private Button buttonPausePlay;
    private SeekBar songSeeek;
    private int currenSongNumber = -1;
    private TextView currentTitleInfo;
    private TextView totalPlayingTime;
    private TextView currentPlayingTime;
    private ListView songListView;
    private File[] currentDirAllFiles;

    public static void changeCurrentDir(File changeDir) {
        MainActivity.dirName = changeDir;


    }

    public static MediaPlayer getPlayer() {
        return mainPlayer;
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
        buttonPausePlay = (Button) findViewById(R.id.pausePlaySong);
        songSeeek = (SeekBar) findViewById(R.id.seekBar);
        currentTitleInfo = (TextView) findViewById(R.id.songTitle);
        totalPlayingTime = (TextView) findViewById(R.id.totalTime);
        currentPlayingTime = (TextView) findViewById(R.id.currentTime);
        songListView = (ListView) findViewById(R.id.songList);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                ServiceProvider.getMediaFiles(dirName, true));
        currentDirAllFiles = ServiceProvider
                .fileNamesAgregator(MainActivity.dirName)
                .toArray(new File[ServiceProvider.fileNamesAgregator(MainActivity.dirName).size()]);
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
     * initialize media player by new instance, start song playback
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
        buttonPausePlay.setText(R.string.playString);
        pausePlay(buttonPausePlay);
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
        }
    }

    /**
     * playing progress, update seekbar condition and current playback time
     */
    public void progress() {
        /**
         * if song is over,start next, if playlist is over send message to user
         */
        if (mainPlayer.getCurrentPosition() == mainPlayer.getDuration()) {
            if (currenSongNumber < currentDirAllFiles.length - 1) {
                currenSongNumber++;
                startPlaying(currenSongNumber);
            } else {
                currenSongNumber = 0;
                startPlaying(currenSongNumber);
            }
        }

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

    /*
     * method performing stop action
     */
    public void stopAction(View view) {
        if (mainPlayer != null) {
            songSeeek.setProgress(0);
            mainPlayer.seekTo(songSeeek.getProgress());
            currentPlayingTime.setText(ServiceProvider.formatPlaybackTime(mainPlayer.getCurrentPosition()));
            mainPlayer.pause();
        }
    }

    /*
     * tap to see all songs on device
     */
    public void seeAll(View view) {
        dirName = Environment.getExternalStorageDirectory();
        if (MainActivity.getPlayer() != null)
            MainActivity.getPlayer().stop();
        Intent restartMainActivity = new Intent(this, MainActivity.class);
        startActivity(restartMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    /*
     * open file browser (allow select directory as source of playlist)
     */
    public void openFileBrowser(View view) {
        Intent openBrowserIntent = new Intent(this, FileBrowserActivity.class);
        startActivity(openBrowserIntent);
    }

}