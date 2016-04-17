package nikborisov.com.github.play_simple;

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

    public void initDirList() {
        currentDir = Environment.getExternalStorageDirectory(); //start directory is root dir
        currentDirContent = currentDir.listFiles();//array represents all subdirs in current dir

        dirsView = (ListView) findViewById(R.id.currentDirFilesList);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                ServiceProvider.getMediaFiles(currentDir, false));
        dirsView.setAdapter(listAdapter);

        dirsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });



    }
}
