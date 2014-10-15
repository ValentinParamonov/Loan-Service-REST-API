package paramonov.valentine.loan_service.util;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Time extends Date {
    private static final String HOURS = "[01]?\\d|2[0-3]";
    private static final String MINUTES_SECONDS = "[0-5]?\\d";
    private static final String DELIMITER = "[:-]";
    private static final String TIME_PATTERN = String.format(
        "^(((%s)%3$s)?(%s)%3$s)?(%2$s)$", HOURS, MINUTES_SECONDS, DELIMITER);
    private static final Pattern timePattern = Pattern.compile(TIME_PATTERN);
    private static final int HOUR_GROUP = 3;
    private static final int MINUTE_GROUP = 4;
    private static final int SECOND_GROUP = 5;

    private final Date date;

    public Time() {
        date = new Date();
    }

    public Time(Date date) {
        this.date = date;
    }

    public static Time valueOf(String timeString) {
        final Matcher matcher = timePattern.matcher(timeString);
        if(!matcher.matches()) {
            throw new IllegalTimeFormatException("Invalid time format");
        }

        final int hours = getGroupValue(matcher, HOUR_GROUP);
        final int minutes = getGroupValue(matcher, MINUTE_GROUP);
        final int seconds = getGroupValue(matcher, SECOND_GROUP);
        final Date time = getDate(hours, minutes, seconds);

        return new Time(time);
    }

    @Override
    public boolean after(Date that) {
        final Calendar thisCalendar = getCalendar(date);
        final Calendar thatCalendar = getCalendar(that);

        final int thisHours = getHours(thisCalendar);
        final int thatHours = getHours(thatCalendar);
        if(thisHours == thatHours) {
            return compareMinutes(thisCalendar, thatCalendar);
        }

        return thisHours > thatHours;
    }

    @Override
    public boolean before(Date that) {
        final Calendar thisCalendar = getCalendar(date);
        final Calendar thatCalendar = getCalendar(that);

        final int thisHours = getHours(thisCalendar);
        final int thatHours = getHours(thatCalendar);
        if(thisHours == thatHours) {
            return compareMinutes(thatCalendar, thisCalendar);
        }

        return thisHours < thatHours;
    }

    public boolean between(Date before, Date after) {
        return after(before) && before(after);
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof Date)) {
            return false;
        }

        final Date date = (Date) object;

        return !(after(date) || before(date));
    }

    @Override
    public String toString() {
        return String.format("%tT", date);
    }

    @Override
    public long getTime() {
        return date.getTime();
    }

    private static int getGroupValue(Matcher matcher, int group) {
        final String groupValue = matcher.group(group);
        if(groupValue == null) {
            return 0;
        }

        return Integer.valueOf(groupValue);
    }

    private static Date getDate(int hours, int minutes, int seconds) {
        final Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        return calendar.getTime();
    }

    boolean compareMinutes(Calendar thisCalendar, Calendar thatCalendar) {
        final int thisMinutes = getMinutes(thisCalendar);
        final int thatMinutes = getMinutes(thatCalendar);
        if(thisMinutes == thatMinutes) {
            return compareSeconds(thisCalendar, thatCalendar);
        }

        return thisMinutes > thatMinutes;
    }

    boolean compareSeconds(Calendar thisCalendar, Calendar thatCalendar) {
        final int thisSeconds = getSeconds(thisCalendar);
        final int thatSeconds = getSeconds(thatCalendar);

        return thisSeconds > thatSeconds;
    }

    private int getSeconds(Calendar calendar) {
        return calendar.get(Calendar.SECOND);
    }

    private int getHours(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int getMinutes(Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    private Calendar getCalendar(Date date) {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        return calendar;
    }
}
