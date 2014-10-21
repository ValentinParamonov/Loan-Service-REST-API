package paramonov.valentine.loan_service.util;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DateUtilsTest {
    private static final long MILLISECONDS_IN_HOUR = 1000 * 60 * 60;
    private static final long MILLISECONDS_IN_DAY = MILLISECONDS_IN_HOUR * 24;

    @Test
    public void testGetDateAfterDays_WhenRequestingDateAfter7Days_DifferenceShouldBeEqualTo7Days() {
        final Date currentDate = new Date();
        final long days = 7;

        final Date dateAfter7Days = DateUtils.getDateAfterDays((int) days);
        final long timeDiff = dateAfter7Days.getTime() - currentDate.getTime();
        final long diffDays = timeDiff / MILLISECONDS_IN_DAY;

        assertThat(diffDays, equalTo(days));
    }

    @Test
    public void testGetDateAfterDays_WhenRequestingDateAfter7DaysFromADate_DifferenceShouldBeEqualTo7Days() {
        final Date date = new Date(0);
        final long days = 7;

        final Date dateAfter7Days = DateUtils.getDateAfterDays(date, (int) days);
        final long diffDays = dateAfter7Days.getTime() / MILLISECONDS_IN_DAY;

        assertThat(diffDays, equalTo(days));
    }

    @Test
    public void testGetDate24HoursAgo_WhenTheDifferenceIsCalculated_ShouldBeEqualTo24Hours() {
        final Date date24HoursAgo = DateUtils.getDate24HoursAgo();
        final long millisNow = new Date().getTime();
        final long millis24HoursAgo = date24HoursAgo.getTime();
        final long diffHours = (millisNow - millis24HoursAgo) / MILLISECONDS_IN_HOUR;

        assertThat(diffHours, equalTo(24L));
    }
}
