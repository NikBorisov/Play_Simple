package nikborisov.com.github.play_simple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by nikolay on 19.04.16.
 * custom adapter for playlist listView
 */
public class PlayListAdpapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public PlayListAdpapter(Context context, String[] values) {
        super(context, R.layout.playlist_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View playListView = inflater.inflate(R.layout.playlist_list_item, parent, false);
        TextView title = (TextView) playListView.findViewById(R.id.listItemTitle);
        TextView autor = (TextView) playListView.findViewById(R.id.listItemArtist);
        TextView album = (TextView) playListView.findViewById(R.id.listItemAlbum);
        TextView duration = (TextView) playListView.findViewById(R.id.listItemDuration);

        String[] currentItem = values[position].split("\n");
        title.setText(currentItem[0]);
        autor.setText(currentItem[1]);
        album.setText(currentItem[2]);
        duration.setText(currentItem[3]);

        return playListView;
    }
}
