package nikborisov.com.github.play_simple;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;


/**
 * Created by nikolay on 13.04.16.
 * represent selected item on songListView
 */
public class SelectedItem implements AdapterView.OnItemSelectedListener {

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
        player = MediaPlayer.create(context, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
