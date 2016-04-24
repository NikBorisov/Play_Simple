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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static MediaPlayer mainPlayer;
    private static File dirName = Environment.getExternalStorageDirectory();
    private static int currenSongNumber = -1;
    private final Handler handler = new Handler();
    private EditText searchAction;
    private Spinner sortChoiser;
    private SortType sortType;
    private SeekBar songSeeek;
    private ImageButton playPauseButton;
    private TextView currentTitleInfo;
    private TextView totalPlayingTime;
    private TextView currentPlayingTime;
    private ListView songListView;
    private File[] currentDirAllFiles;
    private boolean playPauseSwitcher;

    public static MediaPlayer getPlayer() {
        return mainPlayer;
    }

    public static void changeCurrentDir(File changeDir) {
        MainActivity.dirName = changeDir;
    }

    public boolean isPlayed() {
        return playPauseSwitcher;
    }

    public void setPlayed(boolean played) {
        playPauseSwitcher = played;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startUp();
    }

    public void startUp() {
        searchAction = (EditText) findViewById(R.id.searchEditText);
        /*
         * handle "search" user input from editText "searchAction"
         */
        searchAction.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String searched = searchAction.getText().toString();
                    searchAction.setText("");
                    searchAction.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    makeSearch(searched);
                    return true;
                }
                return false;
            }
        });
        /*
         * setup spinner for user "sort by" choice
         */
        sortChoiser = (Spinner) findViewById(R.id.sortBy);
        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(this,
                R.array.userChoice, android.R.layout.simple_spinner_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortChoiser.setAdapter(sortByAdapter);
        //invoke makeSort method by user choice
        sortChoiser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        sortType = SortType.BYALBUM;
                        break;
                    case 2:
                        sortType = SortType.BYARTIST;
                        break;
                    case 3:
                        sortType = SortType.BYDURATION;
                        break;
                    default:
                        sortType = SortType.BYTITLE;
                }
                Log.e("Current sort type is ", sortType.toString());//for test only REMOVE LATER
                makeSort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        songSeeek = (SeekBar) findViewById(R.id.seekBar);
        playPauseButton = (ImageButton) findViewById(R.id.pausePlaySong);
        currentTitleInfo = (TextView) findViewById(R.id.songTitle);
        totalPlayingTime = (TextView) findViewById(R.id.totalTime);
        currentPlayingTime = (TextView) findViewById(R.id.currentTime);
        songListView = (ListView) findViewById(R.id.songList);

        currentDirAllFiles = ServiceProvider
                .fileNamesAgregator(MainActivity.dirName)
                .toArray(new File[ServiceProvider.fileNamesAgregator(MainActivity.dirName).size()]);
        playListInitalization(currentDirAllFiles);
    }

    /*
     * method performing playlist initalization
     */
    public void playListInitalization(File[] content) {
        String[] mediaInfo = new String[content.length];
        for (int i = 0; i < mediaInfo.length; i++) {
            TitleExtractor extractor = new TitleExtractor(Uri.fromFile(content[i]));
            mediaInfo[i] = extractor.getFullTitleInfo() + "\n" + extractor.getDuration();
        }
        PlayListAdpapter listAdapter = new PlayListAdpapter(this, mediaInfo);
        songListView.setAdapter(listAdapter);
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currenSongNumber = position;
                playerInitalization(position);
            }
        });
    }

    /*
     * initialize media player by new instance, start song playback
     */
    public void playerInitalization(int songId) {
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
        currentTitleInfo.setText(new TitleExtractor(Uri.fromFile(currentDirAllFiles[songId])).getFullTitleInfo());
        setPlayed(true);
        songListView.setSelection(currenSongNumber);
        playing();
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
    public void playPauseAction(View view) {
        if (isPlayed())
            setPlayed(false);
        else
            setPlayed(true);
        playing();
    }

    /*
     * switch between paused and play states
     */
    public void playing() {
        if (mainPlayer != null) {
            if (isPlayed()) {
                try {
                    //next line syns player with seekBar
                    mainPlayer.seekTo(songSeeek.getProgress());
                    mainPlayer.start();
                    playPauseButton.setImageResource(R.drawable.pause);
                    progress();

                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                }
            } else {
                playPauseButton.setImageResource(R.drawable.played);
                mainPlayer.pause();
            }
        }
    }

    /*
     * playing progress, update seekbar condition and current playback time
     */
    public void progress() {
        if (mainPlayer.getCurrentPosition() == mainPlayer.getDuration()) {
            if (currenSongNumber < currentDirAllFiles.length - 1) {
                currenSongNumber++;
                playerInitalization(currenSongNumber);
            } else {
                currenSongNumber = 0;
                playerInitalization(currenSongNumber);
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
            setPlayed(false);
            songSeeek.setProgress(mainPlayer.getCurrentPosition());
        }
    }

    /*
     * make prev onClick action
     */
    public void prev(View view) {
        setPlayed(true);
        if (currenSongNumber > 0) {

            currenSongNumber--;
            playerInitalization(currenSongNumber);
        } else {

            currenSongNumber = currentDirAllFiles.length - 1;
            playerInitalization(currenSongNumber);
        }
    }

    /*
     * make "next" onClick action
     */
    public void next(View view) {
        setPlayed(true);
        if (currenSongNumber < currentDirAllFiles.length - 1) {
            currenSongNumber++;
            playerInitalization(currenSongNumber);
        } else {

            currenSongNumber = 0;
            playerInitalization(currenSongNumber);
        }
    }

    /*
     * method performing stop action
     */
    public void stopAction(View view) {
        if (mainPlayer != null) {
            setPlayed(false);
            songSeeek.setProgress(0);
            mainPlayer.seekTo(songSeeek.getProgress());
            currentPlayingTime.setText(ServiceProvider.formatPlaybackTime(mainPlayer.getCurrentPosition()));
            playPauseButton.setImageResource(R.drawable.played);
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
            TitleExtractor extractor;
            for (int i = 0; i < currentDirAllFiles.length; i++) {
                extractor = new TitleExtractor(Uri.fromFile(currentDirAllFiles[i]));
                String checkCoincedence = extractor.getFullTitleInfo();
                if (checkCoincedence.toLowerCase().contains(searched.toLowerCase()))
                    matches.add(currentDirAllFiles[i]);
            }
            if (matches.size() > 0) {
                File[] searchedCoincedenses = matches.toArray(new File[matches.size()]);
                currentDirAllFiles = searchedCoincedenses;
                playListInitalization(searchedCoincedenses);
            }
        }
    }

    public void makeSort() {
        currentDirAllFiles = ServiceProvider.sort(currentDirAllFiles, sortType);
        playListInitalization(currentDirAllFiles);
    }
}