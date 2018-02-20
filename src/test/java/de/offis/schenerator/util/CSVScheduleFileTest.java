package de.offis.schenerator.util;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.schenerator.schedule.CSVScheduleFile;
import de.schenerator.schedule.Schedule;
import de.schenerator.schedule.ScheduleOperation;

public class CSVScheduleFileTest {

    //
    // @Test
    // public void resizeValuesPerLine() {
    // throw new RuntimeException("Test not implemented");
    // }

    @Test
    public void testEquals() {
        CSVScheduleFile testCSVScheduleFile1 = new CSVScheduleFile();
        Assert.assertTrue(testCSVScheduleFile1.equals(testCSVScheduleFile1));

        CSVScheduleFile testCSVScheduleFile2 = null;
        Assert.assertFalse(testCSVScheduleFile1.equals(testCSVScheduleFile2));
        Assert.assertFalse(testCSVScheduleFile1.equals(new Object()));

        testCSVScheduleFile2 = new CSVScheduleFile();
        CSVScheduleFile testCSVScheduleFile3 = new CSVScheduleFile();

        Assert.assertTrue(testCSVScheduleFile1.equals(testCSVScheduleFile2));
        Assert.assertTrue(testCSVScheduleFile2.equals(testCSVScheduleFile3));
        Assert.assertTrue(testCSVScheduleFile1.equals(testCSVScheduleFile3));

        for (int i = 0; i < 2; i++) {
            testCSVScheduleFile1
                    .addSchedule(ScheduleOperation.createZeroValues(null, 96));
            testCSVScheduleFile2
                    .addSchedule(ScheduleOperation.createZeroValues(null, 96));
            Schedule testSchedule = ScheduleOperation.createZeroValues(null,
                    96);
            testSchedule.replace(0, 1);
            testCSVScheduleFile3.addSchedule(testSchedule);
        }

        Assert.assertTrue(testCSVScheduleFile1.equals(testCSVScheduleFile2));
        Assert.assertFalse(testCSVScheduleFile2.equals(testCSVScheduleFile3));
        Assert.assertFalse(testCSVScheduleFile1.equals(testCSVScheduleFile3));

        testCSVScheduleFile1.addToHeader("header");
        Assert.assertFalse(testCSVScheduleFile1.equals(testCSVScheduleFile2));

        testCSVScheduleFile2.addToHeader("header");
        Assert.assertTrue(testCSVScheduleFile1.equals(testCSVScheduleFile2));

        testCSVScheduleFile3.addToHeader("header");
        Assert.assertFalse(testCSVScheduleFile1.equals(testCSVScheduleFile3));

        testCSVScheduleFile1.addValue(0, 0);
        Assert.assertFalse(testCSVScheduleFile1.equals(testCSVScheduleFile2));

        testCSVScheduleFile2.addValue(1, 0);
        Assert.assertFalse(testCSVScheduleFile1.equals(testCSVScheduleFile2));

        testCSVScheduleFile1.addValue(1, 0);
        Assert.assertFalse(testCSVScheduleFile1.equals(testCSVScheduleFile2));

        testCSVScheduleFile2.addValue(0, 0);
        Assert.assertTrue(testCSVScheduleFile1.equals(testCSVScheduleFile2));

    }

    @Test(dataProvider = "testResizeLines")
    public void testResizeLines(CSVScheduleFile testCSVScheduleFile1,
            CSVScheduleFile testCSVScheduleFile2,
            CSVScheduleFile testCSVScheduleFile3, int oldSize, int newSize) {

        Assert.assertEquals(testCSVScheduleFile1, testCSVScheduleFile2);
        Assert.assertNotEquals(testCSVScheduleFile1, testCSVScheduleFile3);

        testCSVScheduleFile1.resizeLines(newSize);
        Assert.assertEquals(testCSVScheduleFile1, testCSVScheduleFile3);
        Assert.assertNotEquals(testCSVScheduleFile1, testCSVScheduleFile2);

        testCSVScheduleFile1.resizeLines(oldSize);
        Assert.assertEquals(testCSVScheduleFile1, testCSVScheduleFile2);
        Assert.assertNotEquals(testCSVScheduleFile1, testCSVScheduleFile3);
    }

    @Test
    public void testTranspose() {
        CSVScheduleFile testCSVScheduleFile1 = new CSVScheduleFile();
        CSVScheduleFile testCSVScheduleFile2 = new CSVScheduleFile();
        CSVScheduleFile testCSVScheduleFile3 = new CSVScheduleFile();
        Schedule test1Line1 = new Schedule();
        Schedule test1Line2 = new Schedule();

        Schedule test2Line1 = new Schedule();
        Schedule test2Line2 = new Schedule();
        Schedule test2Line3 = new Schedule();
        Schedule test2Line4 = new Schedule();
        Schedule test2Line5 = new Schedule();

        Schedule test3Line1 = new Schedule();
        Schedule test3Line2 = new Schedule();

        testCSVScheduleFile2.addSchedule(test2Line1);
        testCSVScheduleFile2.addSchedule(test2Line2);
        testCSVScheduleFile2.addSchedule(test2Line3);
        testCSVScheduleFile2.addSchedule(test2Line4);
        testCSVScheduleFile2.addSchedule(test2Line5);

        for (int i = 0; i < 5; i++) {
            double testVal1 = Double.valueOf(i);
            double testVal2 = Double.valueOf(20 - i);

            test1Line1.add(testVal1);
            testCSVScheduleFile2.getSchedule(i).add(testVal1);
            test3Line1.add(testVal1);

            test1Line2.add(testVal2);
            testCSVScheduleFile2.getSchedule(i).add(testVal2);
            test3Line2.add(testVal2);

        }

        testCSVScheduleFile1.addSchedule(test1Line1);
        testCSVScheduleFile1.addSchedule(test1Line2);

        testCSVScheduleFile3.addSchedule(test3Line1);
        testCSVScheduleFile3.addSchedule(test3Line2);

        Assert.assertEquals(testCSVScheduleFile1, testCSVScheduleFile3);
        Assert.assertNotEquals(testCSVScheduleFile1, testCSVScheduleFile2);

        testCSVScheduleFile1.transpose();
        Assert.assertEquals(testCSVScheduleFile1, testCSVScheduleFile2);
        Assert.assertNotEquals(testCSVScheduleFile1, testCSVScheduleFile3);

        testCSVScheduleFile1.transpose();
        Assert.assertEquals(testCSVScheduleFile1, testCSVScheduleFile3);
        Assert.assertNotEquals(testCSVScheduleFile1, testCSVScheduleFile2);

    }


    

    @DataProvider(name = "testResizeLines") 
    public static Object[][] testResizeLinesData() {
        int oldSize1 = 10;
        int newSize1 = 5;
        CSVScheduleFile test1CSVScheduleFile1 = new CSVScheduleFile();
        CSVScheduleFile test1BeforeOperationObj = new CSVScheduleFile();
        CSVScheduleFile test1AfterOperationObj = new CSVScheduleFile();
        Schedule test1Line11 = ScheduleOperation.createZeroValues(null,
                oldSize1);
        Schedule test1Line12 = ScheduleOperation.createZeroValues(null,
                oldSize1);

        Schedule test1Line21 = ScheduleOperation.createZeroValues(null,
                oldSize1);
        Schedule test1Line22 = ScheduleOperation.createZeroValues(null,
                oldSize1);

        Schedule test1Line31 = ScheduleOperation.createZeroValues(null,
                newSize1);
        Schedule test1Line32 = ScheduleOperation.createZeroValues(null,
                newSize1);
        Schedule test1Line33 = ScheduleOperation.createZeroValues(null,
                newSize1);
        Schedule test1Line34 = ScheduleOperation.createZeroValues(null,
                newSize1);

        /*
         * test1CSVScheduleFile1+3: [[0,1,2,3,4,5,6,7,8,9],
         * [0,1,2,3,4,5,6,7,8,9]] test1CSVScheduleFile2:
         * [[0,1,2,3,4],[5,6,7,8,9],[0,1,2,3,4],[5,6,7,8,9]]
         */
        for (int i = 0; i < oldSize1; i++) {
            test1Line11.replace(i, i);
            test1Line12.replace(i, i);
            test1Line21.replace(i, i);
            test1Line22.replace(i, i);
            if (i < newSize1) {
                test1Line31.replace(i, i);
                test1Line33.replace(i, i);
            } else {
                test1Line32.replace(i - newSize1, i);
                test1Line34.replace(i - newSize1, i);
            }
        }

        test1CSVScheduleFile1.addSchedule(test1Line11);
        test1CSVScheduleFile1.addSchedule(test1Line12);
        test1BeforeOperationObj.addSchedule(test1Line21);
        test1BeforeOperationObj.addSchedule(test1Line22);
        test1AfterOperationObj.addSchedule(test1Line31);
        test1AfterOperationObj.addSchedule(test1Line32);
        test1AfterOperationObj.addSchedule(test1Line33);
        test1AfterOperationObj.addSchedule(test1Line34);

        int oldSize2 = 10;
        int newSize2 = 4;
        CSVScheduleFile test2CSVScheduleFile1 = new CSVScheduleFile();
        CSVScheduleFile test2BeforeOperationObj = new CSVScheduleFile();
        CSVScheduleFile test2AfterOperationObj = new CSVScheduleFile();
        Schedule test2Line11 = ScheduleOperation.createZeroValues(null,
                oldSize2);
        Schedule test2Line12 = ScheduleOperation.createZeroValues(null,
                oldSize2);

        Schedule test2Line21 = ScheduleOperation.createZeroValues(null,
                oldSize2);
        Schedule test2Line22 = ScheduleOperation.createZeroValues(null,
                oldSize2);

        Schedule test2Line31 = ScheduleOperation.createZeroValues(null,
                newSize2);
        Schedule test2Line32 = ScheduleOperation.createZeroValues(null,
                newSize2);
        Schedule test2Line33 = ScheduleOperation.createZeroValues(null,
                newSize2);
        Schedule test2Line34 = ScheduleOperation.createZeroValues(null,
                newSize2);
        Schedule test2Line35 = ScheduleOperation.createZeroValues(null,
                newSize2);

        /*
         * before: [[0,1,2,3,4,5,6,7,8,9], [0,1,2,3,4,5,6,7,8,9]] after:
         * [[0,1,2,3],[4, 5,6,7],[8,9, 0,1],[2,3,4,5],[6,7,8,9]]
         */
        for (int i = 0; i < 10; i++) {
            test2Line11.replace(i, i);
            test2Line12.replace(i, i);
            test2Line21.replace(i, i);
            test2Line22.replace(i, i);
        }

        test2Line31.replace(0, 0);
        test2Line31.replace(1, 1);
        test2Line31.replace(2, 2);
        test2Line31.replace(3, 3);
        test2Line32.replace(0, 4);
        test2Line32.replace(1, 5);
        test2Line32.replace(2, 6);
        test2Line32.replace(3, 7);
        test2Line33.replace(0, 8);
        test2Line33.replace(1, 9);
        test2Line33.replace(2, 0);
        test2Line33.replace(3, 1);
        test2Line34.replace(0, 2);
        test2Line34.replace(1, 3);
        test2Line34.replace(2, 4);
        test2Line34.replace(3, 5);
        test2Line35.replace(0, 6);
        test2Line35.replace(1, 7);
        test2Line35.replace(2, 8);
        test2Line35.replace(3, 9);

        test2CSVScheduleFile1.addSchedule(test2Line11);
        test2CSVScheduleFile1.addSchedule(test2Line12);
        test2BeforeOperationObj.addSchedule(test2Line21);
        test2BeforeOperationObj.addSchedule(test2Line22);
        test2AfterOperationObj.addSchedule(test2Line31);
        test2AfterOperationObj.addSchedule(test2Line32);
        test2AfterOperationObj.addSchedule(test2Line33);
        test2AfterOperationObj.addSchedule(test2Line34);
        test2AfterOperationObj.addSchedule(test2Line35);

        int oldSize3 = 10;
        int newSize3 = 6;
        CSVScheduleFile test3CSVScheduleFile1 = new CSVScheduleFile();
        CSVScheduleFile test3BeforeOperationObj = new CSVScheduleFile();
        CSVScheduleFile test3AfterOperationObj = new CSVScheduleFile();
        Schedule test3Line11 = ScheduleOperation.createZeroValues(null,
                oldSize3);
        Schedule test3Line12 = ScheduleOperation.createZeroValues(null,
                oldSize3);

        Schedule test3Line21 = ScheduleOperation.createZeroValues(null,
                oldSize3);
        Schedule test3Line22 = ScheduleOperation.createZeroValues(null,
                oldSize3);

        Schedule test3Line31 = ScheduleOperation.createZeroValues(null,
                newSize3);
        Schedule test3Line32 = ScheduleOperation.createZeroValues(null,
                newSize3);
        Schedule test3Line33 = ScheduleOperation.createZeroValues(null,
                newSize3);
        Schedule test3Line34 = ScheduleOperation.createZeroValues(null,
                (oldSize3 * 2) % newSize3);

        /*
         * before: [[0,1,2,3,4,5,6,7,8,9], [0,1,2,3,4,5,6,7,8,9]] after:
         * [[0,1,2,3,4,5],[6,7,8,9,0,1],[2,3,4,5,6,7][8,9]]
         */
        for (int i = 0; i < oldSize3; i++) {
            test3Line11.replace(i, i);
            test3Line12.replace(i, i);
            test3Line21.replace(i, i);
            test3Line22.replace(i, i);
        }

        test3Line31.replace(0, 0);
        test3Line31.replace(1, 1);
        test3Line31.replace(2, 2);
        test3Line31.replace(3, 3);
        test3Line31.replace(4, 4);
        test3Line31.replace(5, 5);
        test3Line32.replace(0, 6);
        test3Line32.replace(1, 7);
        test3Line32.replace(2, 8);
        test3Line32.replace(3, 9);
        test3Line32.replace(4, 0);
        test3Line32.replace(5, 1);
        test3Line33.replace(0, 2);
        test3Line33.replace(1, 3);
        test3Line33.replace(2, 4);
        test3Line33.replace(3, 5);
        test3Line33.replace(4, 6);
        test3Line33.replace(5, 7);
        test3Line34.replace(0, 8);
        test3Line34.replace(1, 9);

        test3CSVScheduleFile1.addSchedule(test3Line11);
        test3CSVScheduleFile1.addSchedule(test3Line12);
        test3BeforeOperationObj.addSchedule(test3Line21);
        test3BeforeOperationObj.addSchedule(test3Line22);
        test3AfterOperationObj.addSchedule(test3Line31);
        test3AfterOperationObj.addSchedule(test3Line32);
        test3AfterOperationObj.addSchedule(test3Line33);
        test3AfterOperationObj.addSchedule(test3Line34);

        int oldSize4 = 11;
        int newSize4 = 6;
        CSVScheduleFile test4CSVScheduleFile1 = new CSVScheduleFile();
        CSVScheduleFile test4BeforeOperationObj = new CSVScheduleFile();
        CSVScheduleFile test4AfterOperationObj = new CSVScheduleFile();
        Schedule test4Line11 = ScheduleOperation.createZeroValues(null,
                oldSize4);
        Schedule test4Line12 = ScheduleOperation.createZeroValues(null,
                oldSize4);

        Schedule test4Line21 = ScheduleOperation.createZeroValues(null,
                oldSize4);
        Schedule test4Line22 = ScheduleOperation.createZeroValues(null,
                oldSize4);

        Schedule test4Line31 = ScheduleOperation.createZeroValues(null,
                newSize4);
        Schedule test4Line32 = ScheduleOperation.createZeroValues(null,
                newSize4);
        Schedule test4Line33 = ScheduleOperation.createZeroValues(null,
                newSize4);
        Schedule test4Line34 = ScheduleOperation.createZeroValues(null,
                (oldSize4 * 2) % newSize4);

        /*
         * before: [[0,1,2,3,4,5,6,7,8,9,10], [0,1,2,3,4,5,6,7,8,9,10]] after:
         * [[0,1,2,3,4,5], [6,7,8,9,10,0], [1,2,3,4,5,6], [7,8,9,10]]
         */
        for (int i = 0; i < oldSize4; i++) {
            test4Line11.replace(i, i);
            test4Line12.replace(i, i);
            test4Line21.replace(i, i);
            test4Line22.replace(i, i);
        }

        test4Line31.replace(0, 0);
        test4Line31.replace(1, 1);
        test4Line31.replace(2, 2);
        test4Line31.replace(3, 3);
        test4Line31.replace(4, 4);
        test4Line31.replace(5, 5);
        test4Line32.replace(0, 6);
        test4Line32.replace(1, 7);
        test4Line32.replace(2, 8);
        test4Line32.replace(3, 9);
        test4Line32.replace(4, 10);
        test4Line32.replace(5, 0);
        test4Line33.replace(0, 1);
        test4Line33.replace(1, 2);
        test4Line33.replace(2, 3);
        test4Line33.replace(3, 4);
        test4Line33.replace(4, 5);
        test4Line33.replace(5, 6);
        test4Line34.replace(0, 7);
        test4Line34.replace(1, 8);
        test4Line34.replace(2, 9);
        test4Line34.replace(3, 10);

        test4CSVScheduleFile1.addSchedule(test4Line11);
        test4CSVScheduleFile1.addSchedule(test4Line12);
        test4BeforeOperationObj.addSchedule(test4Line21);
        test4BeforeOperationObj.addSchedule(test4Line22);
        test4AfterOperationObj.addSchedule(test4Line31);
        test4AfterOperationObj.addSchedule(test4Line32);
        test4AfterOperationObj.addSchedule(test4Line33);
        test4AfterOperationObj.addSchedule(test4Line34);

        int oldSize5 = 11;
        int newSize5 = 5;
        CSVScheduleFile test5CSVScheduleFile1 = new CSVScheduleFile();
        CSVScheduleFile test5BeforeOperationObj = new CSVScheduleFile();
        CSVScheduleFile test5AfterOperationObj = new CSVScheduleFile();
        Schedule test5Line11 = ScheduleOperation.createZeroValues(null,
                oldSize5);
        Schedule test5Line12 = ScheduleOperation.createZeroValues(null,
                oldSize5);

        Schedule test5Line21 = ScheduleOperation.createZeroValues(null,
                oldSize5);
        Schedule test5Line22 = ScheduleOperation.createZeroValues(null,
                oldSize5);

        Schedule test5Line31 = ScheduleOperation.createZeroValues(null,
                newSize5);
        Schedule test5Line32 = ScheduleOperation.createZeroValues(null,
                newSize5);
        Schedule test5Line33 = ScheduleOperation.createZeroValues(null,
                newSize5);
        Schedule test5Line34 = ScheduleOperation.createZeroValues(null,
                newSize5);
        Schedule test5Line35 = ScheduleOperation.createZeroValues(null,
                (oldSize5 * 2) % newSize5);

        /*
         * before: [[0,1,2,3,4,5,6,7,8,9,10], [0,1,2,3,4,5,6,7,8,9,10]] after:
         * [[0,1,2,3,4], [5,6,7,8,9], [10,0,1,2,3], [4,5,6,7,8], [9,10]]
         */
        for (int i = 0; i < oldSize5; i++) {
            test5Line11.replace(i, i);
            test5Line12.replace(i, i);
            test5Line21.replace(i, i);
            test5Line22.replace(i, i);
        }

        test5Line31.replace(0, 0);
        test5Line31.replace(1, 1);
        test5Line31.replace(2, 2);
        test5Line31.replace(3, 3);
        test5Line31.replace(4, 4);
        test5Line32.replace(0, 5);
        test5Line32.replace(1, 6);
        test5Line32.replace(2, 7);
        test5Line32.replace(3, 8);
        test5Line32.replace(4, 9);
        test5Line33.replace(0, 10);
        test5Line33.replace(1, 0);
        test5Line33.replace(2, 1);
        test5Line33.replace(3, 2);
        test5Line33.replace(4, 3);
        test5Line34.replace(0, 4);
        test5Line34.replace(1, 5);
        test5Line34.replace(2, 6);
        test5Line34.replace(3, 7);
        test5Line34.replace(4, 8);
        test5Line35.replace(0, 9);
        test5Line35.replace(1, 10);

        test5CSVScheduleFile1.addSchedule(test5Line11);
        test5CSVScheduleFile1.addSchedule(test5Line12);
        test5BeforeOperationObj.addSchedule(test5Line21);
        test5BeforeOperationObj.addSchedule(test5Line22);
        test5AfterOperationObj.addSchedule(test5Line31);
        test5AfterOperationObj.addSchedule(test5Line32);
        test5AfterOperationObj.addSchedule(test5Line33);
        test5AfterOperationObj.addSchedule(test5Line34);
        test5AfterOperationObj.addSchedule(test5Line35);

        int oldSize6 = 11;
        int newSize6 = 5;
        CSVScheduleFile test6CSVScheduleFile1 = new CSVScheduleFile();
        CSVScheduleFile test6BeforeOperationObj = new CSVScheduleFile();
        CSVScheduleFile test6AfterOperationObj = new CSVScheduleFile();
        Schedule test6Line11 = ScheduleOperation.createZeroValues(null,
                oldSize6);
        Schedule test6Line12 = ScheduleOperation.createZeroValues(null,
                oldSize6);
        Schedule test6Line13 = ScheduleOperation.createZeroValues(null,
                oldSize6);

        Schedule test6Line21 = ScheduleOperation.createZeroValues(null,
                oldSize6);
        Schedule test6Line22 = ScheduleOperation.createZeroValues(null,
                oldSize6);
        Schedule test6Line23 = ScheduleOperation.createZeroValues(null,
                oldSize6);

        Schedule test6Line31 = ScheduleOperation.createZeroValues(null,
                newSize6);
        Schedule test6Line32 = ScheduleOperation.createZeroValues(null,
                newSize6);
        Schedule test6Line33 = ScheduleOperation.createZeroValues(null,
                newSize6);
        Schedule test6Line34 = ScheduleOperation.createZeroValues(null,
                newSize6);
        Schedule test6Line35 = ScheduleOperation.createZeroValues(null,
                newSize6);
        Schedule test6Line36 = ScheduleOperation.createZeroValues(null,
                newSize6);
        Schedule test6Line37 = ScheduleOperation.createZeroValues(null,
                (oldSize6 * 3) % newSize6);

        /*
         * before: [[0,1,2,3,4,5,6,7,8,9,10], [0,1,2,3,4,5,6,7,8,9,10],
         * [0,1,2,3,4,5,6,7,8,9,10]] after: [[0,1,2,3,4], [5,6,7,8,9],
         * [10,0,1,2,3], [4,5,6,7,8], [9,10,0,1,2], [3,4,5,6,7],[8,9,10]]
         */
        for (int i = 0; i < oldSize6; i++) {
            test6Line11.replace(i, i);
            test6Line12.replace(i, i);
            test6Line13.replace(i, i);
            test6Line21.replace(i, i);
            test6Line22.replace(i, i);
            test6Line23.replace(i, i);
        }

        test6Line31.replace(0, 0);
        test6Line31.replace(1, 1);
        test6Line31.replace(2, 2);
        test6Line31.replace(3, 3);
        test6Line31.replace(4, 4);
        test6Line32.replace(0, 5);
        test6Line32.replace(1, 6);
        test6Line32.replace(2, 7);
        test6Line32.replace(3, 8);
        test6Line32.replace(4, 9);
        test6Line33.replace(0, 10);
        test6Line33.replace(1, 0);
        test6Line33.replace(2, 1);
        test6Line33.replace(3, 2);
        test6Line33.replace(4, 3);
        test6Line34.replace(0, 4);
        test6Line34.replace(1, 5);
        test6Line34.replace(2, 6);
        test6Line34.replace(3, 7);
        test6Line34.replace(4, 8);
        test6Line35.replace(0, 9);
        test6Line35.replace(1, 10);
        test6Line35.replace(2, 0);
        test6Line35.replace(3, 1);
        test6Line35.replace(4, 2);
        test6Line36.replace(0, 3);
        test6Line36.replace(1, 4);
        test6Line36.replace(2, 5);
        test6Line36.replace(3, 6);
        test6Line36.replace(4, 7);
        test6Line37.replace(0, 8);
        test6Line37.replace(1, 9);
        test6Line37.replace(2, 10);

        test6CSVScheduleFile1.addSchedule(test6Line11);
        test6CSVScheduleFile1.addSchedule(test6Line12);
        test6CSVScheduleFile1.addSchedule(test6Line13);
        test6BeforeOperationObj.addSchedule(test6Line21);
        test6BeforeOperationObj.addSchedule(test6Line22);
        test6BeforeOperationObj.addSchedule(test6Line23);
        test6AfterOperationObj.addSchedule(test6Line31);
        test6AfterOperationObj.addSchedule(test6Line32);
        test6AfterOperationObj.addSchedule(test6Line33);
        test6AfterOperationObj.addSchedule(test6Line34);
        test6AfterOperationObj.addSchedule(test6Line35);
        test6AfterOperationObj.addSchedule(test6Line36);
        test6AfterOperationObj.addSchedule(test6Line37);

        return new Object[][] {
                {
                        test1CSVScheduleFile1, test1BeforeOperationObj,
                        test1AfterOperationObj, oldSize1, newSize1
                }, {
                        test2CSVScheduleFile1, test2BeforeOperationObj,
                        test2AfterOperationObj, oldSize2, newSize2
                }, {
                        test3CSVScheduleFile1, test3BeforeOperationObj,
                        test3AfterOperationObj, oldSize3, newSize3
                }, {
                        test4CSVScheduleFile1, test4BeforeOperationObj,
                        test4AfterOperationObj, oldSize4, newSize4
                }, {
                        test5CSVScheduleFile1, test5BeforeOperationObj,
                        test5AfterOperationObj, oldSize5, newSize5
                }, {
                        test6CSVScheduleFile1, test6BeforeOperationObj,
                        test6AfterOperationObj, oldSize6, newSize6
                }

        };
    }

}
