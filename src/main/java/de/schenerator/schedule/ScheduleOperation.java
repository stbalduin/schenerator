package de.schenerator.schedule;

import de.schenerator.Configuration;
import de.schenerator.Schenerator;
import de.schenerator.util.NoiseMode;

/**
 * This class provides operations which can be executed to manipulate the
 * schedule
 * 
 * @author sBalduin
 *
 */
public class ScheduleOperation {

    /**
     * 
     * @param src
     * @param minX
     * @param minY
     * @param xMode
     * @return
     */
    public static Schedule scaleXValues(Schedule src, double minX, double minY,
            Configuration.XMode xMode) {
        Schedule copy = new Schedule();

        // TODO: insert code from ScheduleModel

        return copy;
    }

    /**
     * Creates a copy of the given schedule and scales the copy to fit specified
     * thresholds
     * 
     * @param src
     *            the {@link Schedule} to copy scaled
     * @param setMinY
     *            the new minimum value for y-axis
     * @param setMaxY
     *            the new maximum value for y-axis
     * @return the copied and scaled {@link Schedule}
     */
    public static Schedule scaleYValues(Schedule src, double setMinY,
            double setMaxY) {
        Schedule copy = new Schedule(src.size());

        // get current min value
        double min = src
                .getData().stream().min((d1, d2) -> (Double
                        .compare(d1.doubleValue(), d2.doubleValue())))
                .get().doubleValue();

        // get current max value
        double max = src
                .getData().stream().max((d1, d2) -> (Double
                        .compare(d1.doubleValue(), d2.doubleValue())))
                .get().doubleValue();

        // scale all values and save into copy
        for (Number number : src.getData()) {
            copy.add((number.doubleValue() - min) * (setMaxY - setMinY)
                    / (max - min) + setMinY);
        }

        return copy;
    }

    /**
     * Creates a copy of the given schedule and trims the copy to fit specified
     * thresholds. Values above this threshold will be set to the threshold
     * (e.g. if minY = 0, each value lower than 0 will be set to 0)
     * 
     * @param src
     *            the {@link Schedule} to copy trimmed
     * @param minY
     *            the new minimum value for y-axis
     * @param maxY
     *            the new maximum value for y-axis
     * @return the copied and trimmed {@link Schedule}
     */
    public static Schedule trimYValues(Schedule src, double minY, double maxY) {
        Schedule copy = new Schedule(src.size());

        for (Number number : src.getData()) {
            copy.add(Math.max(minY, Math.min(maxY, number.doubleValue())));
        }

        return copy;
    }

    /**
     * Creates a copy of the given schedule adds a noise specified by the
     * parameters
     * 
     * @param src
     *            the {@link Schedule} to copy with noise
     * @param noiseType
     *            {@link NoiseMode} either UNIFORM or NORMAL
     * @param strength
     *            indicator for range or standard deviation of the noise
     * @param percentage
     *            if percentage is set to true, the strength will be interpreted
     *            as percentage
     * @return the copied {@link Schedule} with noise
     */
    public static Schedule copyWithNoise(Schedule src, NoiseMode noiseType,
            int strength, boolean percentage) {

        Schedule copy = new Schedule(src.size());

        for (Number number : src.getData()) {
            double noisedValue = getNewValue(number.doubleValue(), noiseType,
                    strength, percentage);

            copy.add(noisedValue);
        }

        return copy;
    }

    /**
     * 
     * @param src
     * @param range
     * @param strength
     * @return
     */
    public static Schedule smooth(Schedule src, int range, double strength) {
        Schedule copy = new Schedule(src.size());

        double left = 0.0;
        double right = 0.0;
        for (int i = 0; i < src.size(); i++) {
            if (i % range == 0) {
                left = src.get(i).doubleValue();
                right = src.get((int) Math.min(i + range, src.size() - 1))
                        .doubleValue();
                copy.add(left);
            } else {
                strength *= 2;
                double distance = i % range;
                double difference = left - right;
                double offset = strength * distance * difference / (range + 1);
                copy.add(left + offset);
            }
        }
        return copy;
    }

    private static double getNewValue(double current, NoiseMode noiseMode,
            int strength, boolean percentage) {
        double offset = 0;

        // currentYValue = currentYValue +/- difference
        switch (noiseMode) {
        case UNIFORM:
            // offset = +/- uniform-random*deviation
            offset = (Schenerator.getRandom().nextDouble() * strength * 2)
                    - strength;
            break;
        case NORMAL:
            // offset = +/- normal-random*deviation
            offset = Schenerator.getRandom().nextGaussian() * strength;
            break;
        default:
            break;
        }

        // 1/100 * currentYValue * offset
        if (percentage) {
            offset *= current / 100.0;
        }

        return current + offset;
    }

    public static Schedule createDummyValues(Schedule schedule, int size) {
        if (schedule == null) {
            schedule = new Schedule();
        }

        for (int i = 0; i < size; i++) {
            schedule.add(Math.cos(i / Math.PI) * 100);
        }

        return schedule;
    }

    public static Schedule createZeroValues(Schedule schedule, int size) {
        if (schedule == null) {
            schedule = new Schedule();
        }

        for (int i = 0; i < size; i++) {
            schedule.add(0);
        }
        return schedule;
    }
}
