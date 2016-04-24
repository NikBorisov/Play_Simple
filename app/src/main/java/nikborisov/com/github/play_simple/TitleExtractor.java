package nikborisov.com.github.play_simple;

import android.media.MediaMetadataRetriever;
import android.net.Uri;

/**
 * Created by nikolay on 14.04.16.
 * class designed for data extraction from media files
 */
public class TitleExtractor {
    private static final String UNKNOWN_DESC = "Unknown";
    MediaMetadataRetriever titleRetriver;
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
        String titleName = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String album = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        if (titleName == null)
            titleName = UNKNOWN_DESC;
        if (artist == null)
            artist = UNKNOWN_DESC;
        if (album == null)
            album = UNKNOWN_DESC;
        return titleName + "\n" + artist + "\n" + album;
    }

    /*
     * returns formatted track duration
     */
    public String getDuration() {
        int titleDuration = Integer.parseInt(titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        return ServiceProvider.formatPlaybackTime(titleDuration);
    }

    public String getTitleOnly() {
        String titleName = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if (titleName == null)
            return UNKNOWN_DESC;
        else return titleName;
    }

    public String getArtistOnly() {
        String titleName = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (titleName == null)
            return UNKNOWN_DESC;
        else return titleName;
    }

    public String getAlbumOnly() {
        String titleName = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        if (titleName == null)
            return UNKNOWN_DESC;
        else return titleName;
    }



}
