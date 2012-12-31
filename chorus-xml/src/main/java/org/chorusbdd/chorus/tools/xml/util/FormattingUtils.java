package org.chorusbdd.chorus.tools.xml.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * User: nick
 * Date: 31/12/12
 * Time: 00:44
 */
public class FormattingUtils {

    private static final ThreadLocal<SimpleDateFormat> startTimeFormatter = new ThreadLocal<SimpleDateFormat>() {
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("dd MMM yyyy HH:mm:ss zzz");
        }
    };

    private static ThreadLocal<DecimalFormat> secondsFormatter = new ThreadLocal<DecimalFormat>() {
        public DecimalFormat initialValue() {
            return new DecimalFormat("##########.#");
        }
    };

    public static SimpleDateFormat getStartTimeFormatter() {
        return startTimeFormatter.get();
    }

    public static DecimalFormat getSecondsFormatter() {
        return secondsFormatter.get();
    }

    public static String getTimeTakenAsSecondsString(long timeTakenMillis) {
        float timeSeconds = timeTakenMillis / 1000f;
        return getSecondsFormatter().format(timeSeconds);
    }
}
