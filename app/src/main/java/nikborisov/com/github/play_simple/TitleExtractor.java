package nikborisov.com.github.play_simple;

import android.media.MediaMetadataRetriever;
import android.net.Uri;

/**
 * Created by nikolay on 14.04.16.
 */
public class TitleExtractor {
    MediaMetadataRetriever titleRetriver;
    private Uri titleUri;
    private String titleName;
    private String artist;
    private String album;

    /**
     * create TitelExtractor instance
     *
     * @param titleUri
     */
    public TitleExtractor(Uri titleUri) {
        this.titleUri = titleUri;
        titleRetriver = new MediaMetadataRetriever();
    }

    /**
     * extract data about title. Use file uri
     */
    public String getTitleInfo() {
        titleRetriver.setDataSource(titleUri.getPath());
        this.titleName = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        this.artist = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        this.album = titleRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        return titleName + "\n" + artist + "\n" + album;
    }

}
