package paramonov.valentine.loan_service.util;

import java.util.Calendar;
import java.util.Date;

public final class DateUtils {
    private DateUtils() {
    }

    public static Date getDateAfterDays(int days) {
        final Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, days);

        return calendar.getTime();
    }

    public static Date getDateAfterDays(Date date, Integer days) {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);

        return calendar.getTime();
    }
}
