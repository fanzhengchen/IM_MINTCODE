package mintcode.com.workhub_im.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JulyYu on 2016/6/23.
 */
public class TimeFormatlUtil {



    public static String formatTime(Long time) {
        if(time == null){
            return "";
        }
        Calendar ct = Calendar.getInstance();
        ct.setTimeInMillis(time);
        int day = ct.get(Calendar.DAY_OF_YEAR);
        int year = ct.get(Calendar.YEAR);
        ct.setTimeInMillis(System.currentTimeMillis());
        int now_day = ct.get(Calendar.DAY_OF_YEAR);
        int now_year = ct.get(Calendar.YEAR);
        SimpleDateFormat sf = null;
        String timeStr = null;
        if (day == now_day && year == now_year) {
            sf = new SimpleDateFormat("HH:mm");
            timeStr = sf.format(new Date(time));
        } else if (year == now_year) {
            sf = new SimpleDateFormat("MM/dd HH:mm");
            timeStr = sf.format(new Date(time));
        } else {
            sf = new SimpleDateFormat("yyyy MM/dd");
            timeStr = sf.format(new Date(time));
        }

        return timeStr;
    }
}
