/*package nikborisov.com.github.play_simple;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import java.io.File;

*//**
 * Created by nikolay on 13.04.16.
 * represent selected item on songListView
 *//*
public class SelectedItem implements AdapterView.OnItemSelectedListener {

    private File[] currentDirAllFiles = PlayerUtils
                    .fileNamesAgregator(MainActivity.ROOT_DIR_NAME)
                        .toArray(new File[PlayerUtils.fileNamesAgregator(MainActivity.ROOT_DIR_NAME).size()]);
    private MediaPlayer player;
    private Context context;

    public SelectedItem(MediaPlayer player, Context context) {
        this.player = player;
        this.context = context;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        player = MediaPlayer.create(context, Uri.fromFile(currentDirAllFiles[position]));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}*/
