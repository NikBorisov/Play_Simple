package nikborisov.com.github.play_simple;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;



public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private Button allSongs;
    private Button playFrom;
    private Button buttonPrev;
    private Button buttonStop;
    private Button buttonPausePlay;
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
        buttonPausePlay = (Button) findViewById(R.id.pausePlaySong);
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
        if (buttonPausePlay.getText().toString().equals(R.string.pauseString)) {
            try {
                mainPlayer.start();
                buttonPausePlay.setText(R.string.pauseString);
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
     *stop
     */
    public void progress() {
        songSeeek.setProgress(mainPlayer.getCurrentPosition());
        if (mainPlayer.isPlaying()) {
            Runnable runner = new Runnable() {
                @Override
                public void run() {
                    progress();
                }
            };
            handler.postDelayed(runner,500);
        }
        else {
            mainPlayer.pause();
            buttonPausePlay.setText(R.string.playString);
            songSeeek.setProgress(mainPlayer.getCurrentPosition());
        }
    }



}
