package nikborisov.com.github.play_simple;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;



public class MainActivity extends AppCompatActivity {

    boolean isPlayed = false;

    private Button allSongs;
    private Button playFrom;
    private Button buttonPrev;
    private Button buttonStop;
    private Button buttonNext;
    private SeekBar songSeeek;
    private MediaPlayer mainPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void initPlayer() {
        /**
         * initialize buttons, seekBar and media player
         */
        allSongs = (Button) findViewById(R.id.AllSongsBut);
        playFrom = (Button) findViewById(R.id.playFromBut);
        buttonPrev = (Button) findViewById(R.id.prevSong);
        buttonStop = (Button) findViewById(R.id.stopSong);
        buttonNext = (Button) findViewById(R.id.nextSong);

        mainPlayer = MediaPlayer.create(this, R.raw.doggystyle);

        songSeeek = (SeekBar) findViewById(R.id.seekBar);
        songSeeek.setMax(mainPlayer.getDuration());
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
    }

    /**
     * switch play/stop Action
     */
    public void playPauseSwitch(View v) {
        if (!isPlayed)
            isPlayed = true;
        else
            isPlayed = false;
        playStop();
    }

    /**
     * handler for seekBar
     */
    private void changeCurrentSeek(View v) {
        if (mainPlayer.isPlaying()) {
            SeekBar sb = (SeekBar) v;
            mainPlayer.seekTo(sb.getProgress());
        }
    }

    /**
     * method perform "play" and "pause" actions
     */
    private void playStop() {
        if (isPlayed) {
            try {
                mainPlayer.start();

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                mainPlayer.pause();
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
    }


}
