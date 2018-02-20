package de.offis.schenerator.schedule;

import org.testng.Assert;
import org.testng.annotations.Test;

import de.schenerator.schedule.Schedule;
import de.schenerator.schedule.ScheduleOperation;

public class ScheduleTest {

    @Test
    public void testEquals() {
        Schedule testSchedule1 = ScheduleOperation.createZeroValues(null, 96);
        Schedule testSchedule2 = ScheduleOperation.createZeroValues(null, 96);
        Schedule testSchedule3 = ScheduleOperation.createZeroValues(null, 100);

        Assert.assertTrue(testSchedule1.equals(testSchedule2));
        Assert.assertTrue(testSchedule2.equals(testSchedule1));
        
        Assert.assertFalse(testSchedule1.equals(testSchedule3));
        Assert.assertFalse(testSchedule3.equals(testSchedule1));
        
        testSchedule2.replace(48, 1);
        Assert.assertFalse(testSchedule1.equals(testSchedule2));
        Assert.assertFalse(testSchedule2.equals(testSchedule3));
    }

}
