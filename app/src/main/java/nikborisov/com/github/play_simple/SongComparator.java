package nikborisov.com.github.play_simple;

import android.net.Uri;

import java.io.File;
import java.util.Comparator;

/**
 * Created by nikolay on 30.04.16.
 */
public class SongComparator implements Comparator<File> {
    SortType sortType;

    public SongComparator(SortType sortType) {
        this.sortType = sortType;
    }

    @Override
    public int compare(File first, File second) {
        String itemOne;
        String itemTwo;
        switch (sortType) {
            case BYARTIST: {
                itemOne = new TitleExtractor(Uri.fromFile(first)).getArtistOnly();
                itemTwo = new TitleExtractor(Uri.fromFile(second)).getArtistOnly();
                return itemOne.compareTo(itemTwo);
            }
            case BYALBUM: {
                itemOne = new TitleExtractor(Uri.fromFile(first)).getAlbumOnly();
                itemTwo = new TitleExtractor(Uri.fromFile(second)).getAlbumOnly();
                return itemOne.compareTo(itemTwo);
            }
            case BYDURATION: {
                int itemOneDuration = new TitleExtractor(Uri.fromFile(first)).getDurationValue();
                int itemTwoDuration = new TitleExtractor(Uri.fromFile(second)).getDurationValue();
                return Integer.valueOf(itemOneDuration).compareTo(Integer.valueOf(itemTwoDuration));
            }
            default: {
                itemOne = new TitleExtractor(Uri.fromFile(first)).getTitleOnly();
                itemTwo = new TitleExtractor(Uri.fromFile(second)).getTitleOnly();
                return itemOne.compareTo(itemTwo);
            }
        }

    }

}
