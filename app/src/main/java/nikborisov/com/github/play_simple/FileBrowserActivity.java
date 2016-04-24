package nikborisov.com.github.play_simple;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

public class FileBrowserActivity extends AppCompatActivity {

    private File currentDir;
    private File selectedDir;
    private File[] currentDirContent;
    private ListView dirsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        initDirList();
    }

    /*
     * agregate dirs, that contain media files;
     */
    public void initDirList() {
        currentDir = Environment.getExternalStorageDirectory(); //start directory is root dir
        currentDirContent = ServiceProvider.dirsWithMusicAgregator(currentDir) //work only with dirs that contain media files
                .toArray(new File[ServiceProvider.dirsWithMusicAgregator(currentDir).size()]);
        dirsView = (ListView) findViewById(R.id.currentDirFilesList);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                R.layout.filebrowser_list_item, R.id.listItemFolderName,
                ServiceProvider.getMediaFiles(currentDir, false));
        dirsView.setAdapter(listAdapter);
        dirsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDir = currentDirContent[position];
            }
        });
    }

    /*
     * Add selected dir to playlist;
     */
    public void addAllToPlaylist(View view) {
        if (selectedDir == null)
            selectedDir = Environment.getExternalStorageDirectory();
        Intent backToMainActivity = new Intent(this, MainActivity.class);
        if (MainActivity.getPlayer() != null)
            MainActivity.getPlayer().stop();
        MainActivity.changeCurrentDir(selectedDir);
        startActivity(backToMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
