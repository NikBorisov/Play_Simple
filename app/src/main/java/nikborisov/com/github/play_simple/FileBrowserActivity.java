package nikborisov.com.github.play_simple;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

public class FileBrowserActivity extends AppCompatActivity {

    private File currentDir;
    private ListView dirsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        initDirList();
    }

    public void initDirList() {
        currentDir = Environment.getExternalStorageDirectory();
        dirsView = (ListView) findViewById(R.id.currentDirFilesList);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                ServiceProvider.getMediaFiles(currentDir, false));
        dirsView.setAdapter(listAdapter);

    }
}
