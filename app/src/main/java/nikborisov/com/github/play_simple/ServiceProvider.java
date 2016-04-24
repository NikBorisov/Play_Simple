package nikborisov.com.github.play_simple;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by nikolay on 12.04.16.
 * class for audio player services
 */
public class ServiceProvider {

    private static final Pattern checkIsFormatSuppoerted = Pattern.compile("([^\\s]+(\\.(?i)(mp3|wav|ogg|aac|flac))$)");

    /*
     * returns formatted playback time for total time view and current playback time view;
     */
    public static String formatPlaybackTime(int millsTime) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millsTime),
                TimeUnit.MILLISECONDS.toSeconds(millsTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millsTime)));
    }

    /*
     * if @param filesOrDirs true, return String[] of music files names, that represented in selected directory;
     * if @param filesOrDirs false, return String[] of dirs names with music files inside;
     */
    public static String[] getMediaFiles(File dirName, boolean filesOrDirs) {
        ArrayList<File> buffer = new ArrayList<>();
        if (filesOrDirs) {
            buffer = fileNamesAgregator(dirName);
        } else {
            buffer = dirsWithMusicAgregator(dirName);
        }
        String[] returned = new String[buffer.size()];
        for (int i = 0; i < buffer.size(); i++)
            returned[i] = buffer.get(i).getName();
        return returned;
    }

    /*
     * agregate all music files in selected dir and subdirs;
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

    /*
     * agregate dirs with media files inside;
     */
    public static ArrayList<File> dirsWithMusicAgregator(File parentDir) {
        ArrayList<File> filesList = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                for (File check : file.listFiles()) {
                    Matcher findMatches = checkIsFormatSuppoerted.matcher(check.getName());
                    if (findMatches.find()) {
                        filesList.add(file);
                        break;
                    }
                }
            }
        }
        return filesList;
    }

    /*
     * sort files by title, artist, album or duration (user choiced);
     */
    public static File[] sort(File[] songList, SortType sortType) {
        if (sortType == SortType.BYDURATION) {
            int itemOneDuration;
            int itemTwoDuration;
            for (int border = songList.length - 1; border >= 0; border--) {
                for (int current = 0; current < border; current++) {
                    itemOneDuration = new TitleExtractor(Uri.fromFile(songList[current])).getDurationValue();
                    itemTwoDuration = new TitleExtractor(Uri.fromFile(songList[current + 1])).getDurationValue();
                    if (itemOneDuration > itemTwoDuration) {
                        File buffer = songList[current];
                        songList[current] = songList[current + 1];
                        songList[current + 1] = buffer;
                    }
                }
            }
            return songList;
        } else {
            String itemOne;
            String itemTwo;
            for (int border = songList.length - 1; border >= 0; border--) {
                for (int current = 0; current < border; current++) {
                    switch (sortType) {
                        case BYARTIST: {
                            itemOne = new TitleExtractor(Uri.fromFile(songList[current])).getArtistOnly();
                            itemTwo = new TitleExtractor(Uri.fromFile(songList[current + 1])).getArtistOnly();
                            break;
                        }
                        case BYALBUM: {
                            itemOne = new TitleExtractor(Uri.fromFile(songList[current])).getAlbumOnly();
                            itemTwo = new TitleExtractor(Uri.fromFile(songList[current + 1])).getAlbumOnly();
                            break;
                        }
                        default: {
                            itemOne = new TitleExtractor(Uri.fromFile(songList[current])).getTitleOnly();
                            itemTwo = new TitleExtractor(Uri.fromFile(songList[current + 1])).getTitleOnly();
                        }
                    }
                    if (itemOne.compareTo(itemTwo) > 0) {
                        File buffer = songList[current];
                        songList[current] = songList[current + 1];
                        songList[current + 1] = buffer;
                    }
                }
            }
            return songList;
        }
    }
}
