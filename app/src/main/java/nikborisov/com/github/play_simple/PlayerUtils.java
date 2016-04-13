package nikborisov.com.github.play_simple;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by nikolay on 12.04.16.
 * class for audio player utils
 */
public class PlayerUtils {
    /**
     * returns formatted playback time for total time view and current playback time view
     */
    public static String formatPlaybackTime(int millsTime) {
        return String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(millsTime),
                TimeUnit.MILLISECONDS.toSeconds(millsTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millsTime)));
    }

    /**
     * method returns list of mp3 files in directory
     */
    public static String[] getMp3Files(File dirName) {
        ArrayList<File> mp3List = new ArrayList<File>();
        File[] files = dirName.listFiles();
        for (File file : files) {
            //if (file.getName().endsWith(".mp3") && !file.isDirectory()) {
                mp3List.add(file);
            //}
        }
        String[] returnedNames = new String[mp3List.size()];
        for (int i = 0; i < mp3List.size(); i++) {
            returnedNames[i] = mp3List.get(i).getName();
        }
        return returnedNames;
    }

}
