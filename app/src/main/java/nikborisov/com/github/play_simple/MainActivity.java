package nikborisov.com.github.play_simple;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static MediaPlayer mainPlayer;
    private static File dirName = Environment.getExternalStorageDirectory();
    private final Handler handler = new Handler();
    private int currenSongNumber = -1;
    private EditText searchAction;
    private Button buttonPausePlay;
    private SeekBar songSeeek;
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

    /*
     * setup Player to valid condition
     */
    public void playerInitialization() {
        searchAction = (EditText) findViewById(R.id.searchEditText);
        /*
         * handle "search" user input from editText "searchAction"
         */
        searchAction.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do something, e.g. set your TextView here via .setText()
                    String searched = searchAction.getText().toString();
                    searchAction.setText("");
                    searchAction.clearFocus();
                    Log.e("searched is ", searched); // for testing only
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    makeSearch(searched);
                    return true;
                }
                return false;
            }
        });

        buttonPausePlay = (Button) findViewById(R.id.pausePlaySong);
        songSeeek = (SeekBar) findViewById(R.id.seekBar);
        currentTitleInfo = (TextView) findViewById(R.id.songTitle);
        totalPlayingTime = (TextView) findViewById(R.id.totalTime);
        currentPlayingTime = (TextView) findViewById(R.id.currentTime);
        songListView = (ListView) findViewById(R.id.songList);

        currentDirAllFiles = ServiceProvider
                .fileNamesAgregator(MainActivity.dirName)
                .toArray(new File[ServiceProvider.fileNamesAgregator(MainActivity.dirName).size()]);

        //playlist initalization start
        playListInitalization(currentDirAllFiles);
    }

    /*
     * method performing playlist initalization
     */
    public void playListInitalization(File[] content) {
        String[] mediaInfo = new String[content.length];
        for (int i = 0; i < mediaInfo.length; i++) {
            TitleExtractor extractor = new TitleExtractor(Uri.fromFile(content[i]));
            mediaInfo[i] = extractor.getTitleInfo() + "\n" + extractor.getDurationOnly();
        }
        PlayListAdpapter listAdapter = new PlayListAdpapter(this, mediaInfo);
        songListView.setAdapter(listAdapter);
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currenSongNumber = position;
                startPlaying(position);
            }
        });
    }

    /*
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
        songListView.setSelection(currenSongNumber);
        pausePlay(buttonPausePlay);
    }

    /*
     * handler for seekBar
     */
    private void changeCurrentSeek(View view) {
        if (mainPlayer.isPlaying()) {
            SeekBar sb = (SeekBar) view;
            mainPlayer.seekTo(sb.getProgress());
        }
    }

    /*
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

    /*
     * playing progress, update seekbar condition and current playback time
     */
    public void progress() {
        /*
         * if song is over,start next
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

    /*
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

    /*
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

    /*
     * method performing search by user input
     */
    public void makeSearch(String searched) {
        if (!searched.isEmpty()) {
            ArrayList<File> matches = new ArrayList<>();
            for (int i = 0; i < currentDirAllFiles.length; i++) {
                TitleExtractor extractor = new TitleExtractor(Uri.fromFile(currentDirAllFiles[i]));
                String checkCoincedence = extractor.getTitleInfo();
                if (checkCoincedence.toLowerCase().contains(searched.toLowerCase()))
                    matches.add(currentDirAllFiles[i]);
            }
            playListInitalization(matches.toArray(new File[matches.size()]));
        }
    }
}