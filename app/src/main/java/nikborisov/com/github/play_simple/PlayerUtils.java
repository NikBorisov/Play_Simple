package nikborisov.com.github.play_simple;

import java.util.concurrent.TimeUnit;

/**
 * Created by nikolay on 12.04.16.
 * class for audio player utils
 */
public class PlayerUtils {

    public static String formatPlaybackTime(int millsTime) {
        return String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(millsTime),
                TimeUnit.MILLISECONDS.toSeconds(millsTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millsTime)));
    }
}
