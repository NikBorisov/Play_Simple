package nikborisov.com.github.play_simple;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nikolay on 12.04.16.
 * class for audio player utils
 */
public class ServiceProvider {

    private static final Pattern checkIsFormatSuppoerted = Pattern.compile("([^\\s]+(\\.(?i)(mp3|wav|ogg|aac|flac))$)");
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
     * return String[] of music files names, that represented in selected directory
     */
    public static String[] getMediaFiles(File dirName) {
        ArrayList<File> buffer = fileNamesAgregator(dirName);
        String[] returned = new String[buffer.size()];
        for (int i = 0; i < buffer.size(); i++)
            returned[i] = buffer.get(i).getName();
        return returned;

    }

    /**
     * agregate all music files in selected dir and subdirs
     */
    public static ArrayList<File> fileNamesAgregator(File parentDir) {

        ArrayList<File> filesList = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                filesList.addAll(fileNamesAgregator(file));
            } else {
                Matcher findMatches = checkIsFormatSuppoerted.matcher(file.getName());
                if (findMatches.find())
                    filesList.add(file);
            }
        }
        return filesList;
    }

    public static ArrayList<File> dirsWithMusicAgregator(File parentDir) {
        return null;
    }
}
