package org.chorusbdd.chorus.tools.xml.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
        String result = "0";
        if ( timeTakenMillis > 0) {
            float timeSeconds = timeTakenMillis / 1000f;
            result = getSecondsFormatter().format(timeSeconds);
        }
        return result;
    }

    public static String getAsCsv(String[] usesHandlers) {
        StringBuilder sb = new StringBuilder();
        List<String> l = Arrays.asList(usesHandlers);
        Iterator<String> i = l.iterator();
        while(i.hasNext()) {
            sb.append(i.next());
            if ( i.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
