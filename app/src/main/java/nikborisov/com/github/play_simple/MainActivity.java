package nikborisov.com.github.play_simple;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();

    private Button allSongs;
    private Button playFrom;

    private ListView songList;
    private Button buttonPrev;
    private Button buttonStop;
    private Button buttonPausePlay;
    private Button buttonNext;
    private SeekBar songSeeek;
    private MediaPlayer mainPlayer;
    private TextView totalPlayingTime;
    private TextView currentPlayingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPlayer();
    }

    public void initPlayer() {
        /**
         * initialize buttons, seekBar and media player
         */
        allSongs = (Button) findViewById(R.id.AllSongsBut);
        playFrom = (Button) findViewById(R.id.playFromBut);
        buttonPrev = (Button) findViewById(R.id.prevSong);
        buttonStop = (Button) findViewById(R.id.stopSong);
        buttonPausePlay = (Button) findViewById(R.id.pausePlaySong);
        buttonNext = (Button) findViewById(R.id.nextSong);
        mainPlayer = MediaPlayer.create(this, R.raw.doggystyle);
        songSeeek = (SeekBar) findViewById(R.id.seekBar);
        songSeeek.setMax(mainPlayer.getDuration());
        totalPlayingTime = (TextView) findViewById(R.id.totalTime);
        currentPlayingTime = (TextView) findViewById(R.id.currentTime);
        /**
         * annonymous class for seekbar action listener
         */
        songSeeek.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent currentEvent) {
                changeCurrentSeek(view);
                return false;
            }
        });
        totalPlayingTime.setText(PlayerUtils.formatPlaybackTime(mainPlayer.getDuration()));

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
    /**
     * playing progress, update seekbar condition and current playback time
     */
    public void progress() {
        songSeeek.setProgress(mainPlayer.getCurrentPosition());
        currentPlayingTime.setText(PlayerUtils.formatPlaybackTime(mainPlayer.getCurrentPosition()));
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
        songSeeek.setProgress(0);
        mainPlayer.seekTo(songSeeek.getProgress());
        mainPlayer.pause();
    }

    /**
     * method init listView on main screen
     */
    public void initSongList() {
        
    }
}