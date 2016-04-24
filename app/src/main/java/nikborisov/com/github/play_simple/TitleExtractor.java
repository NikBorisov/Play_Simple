package nikborisov.com.github.play_simple;

import android.media.MediaMetadataRetriever;
import android.net.Uri;

/**
 * Created by nikolay on 14.04.16.
 * class designed for data extraction from media files
 */
public class TitleExtractor {
    private static final String UNKNOWN_DESC = "Unknown";
    private MediaMetadataRetriever titleRetriver;
    private String titleName;
    private String artist;
    private String album;
    private Uri titleUri;


    /*
     * create TitelExtractor instance
     * @param titleUri
     */
    public TitleExtractor(Uri titleUri) {
        this.titleUri = titleUri;
        titleRetriver = new MediaMetadataRetriever();
        titleRetriver.setDataSource(titleUri.getPath());
    }

    /*
     * extract data about title
     */
    public String getFullTitleInfo() {
        titleName = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        artist = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        album = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        if (titleName == null)
            titleName = UNKNOWN_DESC;
        if (artist == null)
            artist = UNKNOWN_DESC;
        if (album == null)
            album = UNKNOWN_DESC;
        return titleName + "\n" + artist + "\n" + album;
    }

    public int getDurationValue() {
        return Integer.parseInt(titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    public String getDuration() {
        int titleDuration = Integer.parseInt(titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        return ServiceProvider.formatPlaybackTime(titleDuration);
    }

    public String getTitleOnly() {
        titleName = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if (titleName == null)
            return UNKNOWN_DESC;
        else return titleName;
    }

    public String getArtistOnly() {
        artist = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (artist == null)
            return UNKNOWN_DESC;
        else return artist;
    }

    public String getAlbumOnly() {
        album = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        if (album == null)
            return UNKNOWN_DESC;
        else return album;
    }



}
