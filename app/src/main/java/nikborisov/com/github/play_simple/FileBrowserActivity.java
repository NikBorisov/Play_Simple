package nikborisov.com.github.play_simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class FileBrowserActivity extends AppCompatActivity {

    private ListView dirsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);
        initDirList();
    }

    public void initDirList() {

    }
}
