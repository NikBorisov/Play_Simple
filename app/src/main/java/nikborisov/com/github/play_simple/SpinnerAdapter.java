package nikborisov.com.github.play_simple;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by nikolay on 23.04.16.
 */
public class SpinnerAdapter extends Activity implements AdapterView.OnItemSelectedListener {
    private SortType sortType;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        for (SortType setSortType : SortType.values()) {
            if (setSortType.ordinal() == position)
                sortType = setSortType;
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
