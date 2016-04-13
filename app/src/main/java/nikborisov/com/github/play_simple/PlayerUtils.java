package nikborisov.com.github.play_simple;

import java.io.File;
import java.util.Arrays;
import java.util.List;
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

    public static String[] getMediaFiles(File dirName) {
        List<File> buffer = getMp3List(dirName);
        String[] returned = new String[buffer.size()];
        for (int i = 0; i < buffer.size(); i++)
            returned[i] = buffer.get(i).getName();
        return returned;

    }

    public static List<File> getMp3List(File dirName) {
        /*ArrayList<File> inFiles = new ArrayList<>();
        File[] files = dirName.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getMp3List(file));
            } else {
                if(file.getName().endsWith(".mp3")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;*/
        return Arrays.asList(dirName.listFiles());
    }
}
