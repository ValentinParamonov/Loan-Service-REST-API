package paramonov.valentine.loan_service.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationUtilsTest {
    @Test
    public void testIsBetweenIncluding_1Between0And2_ShouldBeTrue() throws Exception {
        final int c = 1;
        final int from = 0;
        final int to = 2;

        final boolean isBetween = ValidationUtils.isBetweenIncluding(c, from, to);

        assertTrue(isBetween);
    }

    @Test
    public void testIsBetweenIncluding_0Between0And2_ShouldBeTrue() throws Exception {
        final int c = 0;
        final int from = 0;
        final int to = 2;

        final boolean isBetween = ValidationUtils.isBetweenIncluding(c, from, to);

        assertTrue(isBetween);
    }

    @Test
    public void testIsBetweenIncluding_2Between0And2_ShouldBeTrue() throws Exception {
        final int c = 2;
        final int from = 0;
        final int to = 2;

        final boolean isBetween = ValidationUtils.isBetweenIncluding(c, from, to);

        assertTrue(isBetween);
    }

    @Test
    public void testIsBetweenIncluding_Minus1Between0And2_ShouldBeFalse() throws Exception {
        final int c = -1;
        final int from = 0;
        final int to = 2;

        final boolean isBetween = ValidationUtils.isBetweenIncluding(c, from, to);

        assertFalse(isBetween);
    }

    @Test
    public void testIsBetweenIncluding_3Between0And2_ShouldBeFalse() throws Exception {
        final int c = 3;
        final int from = 0;
        final int to = 2;

        final boolean isBetween = ValidationUtils.isBetweenIncluding(c, from, to);

        assertFalse(isBetween);
    }
}
