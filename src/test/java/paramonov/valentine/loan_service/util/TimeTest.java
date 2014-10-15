package paramonov.valentine.loan_service.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertTrue;

public final class TimeTest {
    private Time time;

    @Before
    public void setUp() {
        time = new Time();
    }

    @Test
    public void testValueOf_When00x00x00_ToStringShouldBeEqualToInputString() {
        final String inTimeString = "00:00:00";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(inTimeString));
    }

    @Test
    public void testValueOf_When23x15x49_ToStringShouldBeEqualToInputString() {
        final String inTimeString = "23:15:49";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(inTimeString));
    }

    @Test
    public void testValueOf_When00x00x00WithDashes_ToStringShouldBeEqualToExpectedString() {
        final String inTimeString = "00-00-00";
        final String expectedString = "00:00:00";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(expectedString));
    }

    @Test
    public void testValueOf_When15x49_ToStringShouldBeEqualToExpectedString() {
        final String inTimeString = "15:49";
        final String expectedString = "00:15:49";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(expectedString));
    }

    @Test
    public void testValueOf_When49_ToStringShouldBeEqualToExpectedString() {
        final String inTimeString = "49";
        final String expectedString = "00:00:49";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(expectedString));
    }

    @Test
    public void testValueOf_When1x00x00_ToStringShouldBeEqualToExpectedString() {
        final String inTimeString = "1:00:00";
        final String expectedString = "01:00:00";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(expectedString));
    }

    @Test
    public void testValueOf_When00x01x00_ToStringShouldBeEqualToExpectedString() {
        final String inTimeString = "00:1:00";
        final String expectedString = "00:01:00";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(expectedString));
    }

    @Test
    public void testValueOf_When00x00x01_ToStringShouldBeEqualToExpectedString() {
        final String inTimeString = "00:00:1";
        final String expectedString = "00:00:01";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(expectedString));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_When24x15x49_ShouldThrowException() {
        final String inTimeString = "24:15:49";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(inTimeString));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_When20x66x49_ShouldThrowException() {
        final String inTimeString = "20:66:49";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(inTimeString));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_When20x01x70_ShouldThrowException() {
        final String inTimeString = "20:01:70";

        final Time time = Time.valueOf(inTimeString);
        final String timeString = time.toString();

        assertThat(timeString, equalTo(inTimeString));
    }

    @Test
    public void testBefore_TimeOneBeforeTimeTwo_ShouldBeTrue() {
        final Time timeOne = Time.valueOf("00:01:00");
        final Time timeTwo = Time.valueOf("00:02:00");

        assertTrue(timeOne.before(timeTwo));
    }

    @Test
    public void testBefore_TimeTwoBeforeTimeOne_ShouldBeFalse() {
        final Time timeOne = Time.valueOf("02:00:00");
        final Time timeTwo = Time.valueOf("01:01:01");

        assertFalse(timeOne.before(timeTwo));
    }

    @Test
    public void testBefore_TimeOneIsTimeTwo_ShouldBeFalse() {
        final Time timeOne = Time.valueOf("01:01:01");
        final Time timeTwo = Time.valueOf("01:01:01");

        assertFalse(timeOne.before(timeTwo));
    }

    @Test
    public void testAfter_TimeOneAfterTimeTwo_ShouldBeTrue() {
        final Time timeOne = Time.valueOf("00:15:00");
        final Time timeTwo = Time.valueOf("00:02:00");

        assertTrue(timeOne.after(timeTwo));
    }

    @Test
    public void testAfter_TimeTwoAfterTimeOne_ShouldBeFalse() {
        final Time timeOne = Time.valueOf("02:00:00");
        final Time timeTwo = Time.valueOf("03:01:01");

        assertFalse(timeOne.after(timeTwo));
    }

    @Test
    public void testAfter_TimeOneIsTimeTwo_ShouldBeFalse() {
        final Time timeOne = Time.valueOf("01:01:01");
        final Time timeTwo = Time.valueOf("01:01:01");

        assertFalse(timeOne.after(timeTwo));
    }

    @Test
    public void testEquals_TimeOneIsTimeTwo_ShouldBeEqual() {
        final Time timeOne = Time.valueOf("01:01:01");
        final Time timeTwo = Time.valueOf("01:01:01");

        assertThat(timeOne, equalTo(timeTwo));
    }

    @Test
    public void testEquals_TimeOneIsNotTimeTwo_ShouldNotBeEqual() {
        final Time timeOne = Time.valueOf("01:01:01");
        final Time timeTwo = Time.valueOf("02:01:01");

        assertThat(timeOne, not(equalTo(timeTwo)));
    }

    @Test
    public void testCompareSeconds_TimeOneBeforeTimeTwo_ShouldBeFalse() {
        final Calendar timeOne = getCalendarWithTime(0, 0, 20);
        final Calendar timeTwo = getCalendarWithTime(0, 0, 30);

        final boolean before = time.compareSeconds(timeOne, timeTwo);

        assertFalse(before);
    }

    @Test
    public void testCompareMinutes_TimeOneBeforeTimeTwo_ShouldBeFalse() {
        final Calendar timeOne = getCalendarWithTime(0, 20, 0);
        final Calendar timeTwo = getCalendarWithTime(0, 30, 0);

        final boolean before = time.compareMinutes(timeOne, timeTwo);

        assertFalse(before);
    }

    @Test
    public void testBetween_TimeAfterTimeOneAndBeforeTimeTwo_ShouldBeTrue() {
        final Time timeOne = Time.valueOf("1");
        final Time timeTwo = Time.valueOf("3");
        final Time timeThree = Time.valueOf("2");

        assertTrue(timeThree.between(timeOne, timeTwo));
    }

    @Test
    public void testBetween_TimeAfterTimeOneAndAfterTimeTwo_ShouldBeFalse() {
        final Time timeOne = Time.valueOf("15:59:33");
        final Time timeTwo = Time.valueOf("16:00:32");
        final Time timeThree = Time.valueOf("17:00:00");

        assertFalse(timeThree.between(timeOne, timeTwo));
    }

    @Test
    public void testBetween_TimeBeforeTimeOneAndBeforeTimeTwo_ShouldBeFalse() {
        final Time timeOne = Time.valueOf("15:59:33");
        final Time timeTwo = Time.valueOf("16:00:32");
        final Time timeThree = Time.valueOf("14:00:00");

        assertFalse(timeThree.between(timeOne, timeTwo));
    }

    @Test
    public void testBetween_TimeBeforeTimeOneAndAfterTimeTwoWhenTimeOneAfterTimeTwo_ShouldBeFalse() {
        final Time timeOne = Time.valueOf("16:00:32");
        final Time timeTwo = Time.valueOf("15:59:33");
        final Time timeThree = Time.valueOf("15:59:34");

        assertFalse(timeThree.between(timeOne, timeTwo));
    }

    private Calendar getCalendarWithTime(int hours, int minutes, int seconds) {
        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        return calendar;
    }
}
