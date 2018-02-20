package de.schenerator.schedule;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Data;

/**
 * Container class for one schedule
 * 
 * @author sBalduin
 *
 */
public class Schedule {

    // the schedule values saved as list of number objects
    private List<Number> powerValues;

    /**
     * Default constructor, creates an empty initialized schedule
     */
    public Schedule() {
        powerValues = new ArrayList<>();
        // for (int i = 0; i < 96; i++) {
        // powerValues.add(Math.cos(i / Math.PI) * 100);
        // }
    }

    /**
     * Creates an empty initialized schedule and allocates space for given size
     * of elements
     * 
     * @param size
     *            of the empty schedule
     */
    public Schedule(int size) {
        powerValues = new ArrayList<>(size);
    }

    /**
     * Creates a schedule based on the given array of double values
     * 
     * @param powerDoubles
     *            array of doubles which will be used to fill the schedule
     */
    public Schedule(double[] powerDoubles) {
        powerValues = new ArrayList<>();
        for (int i = 0; i < powerDoubles.length; i++) {
            this.powerValues.add(powerDoubles[i]);
        }
    }

    /**
     * Creates a schedule based on the given array of long values
     * 
     * @param powerLongs
     *            array of longs which will be used to fill the schedule
     */
    public Schedule(long[] powerLongs) {
        powerValues = new ArrayList<>();
        for (int i = 0; i < powerLongs.length; i++) {
            this.powerValues.add(powerLongs[i]);
        }
    }

    /**
     * Creates a schedule based on the given {@link ObservableList} of values
     * 
     * @param dataList
     *            values which will be used to fill the schedule
     */
    public Schedule(ObservableList<Data<Number, Number>> dataList) {
        powerValues = new ArrayList<>();
        for (Data<Number, Number> data : dataList) {
            powerValues.add(data.getXValue().intValue(), data.getYValue());
        }
    }

    /**
     * Adds the value add the end of the schedule
     * 
     * @param value
     */
    public void add(Number value) {
        powerValues.add(value);
    }

    /**
     * Adds the value at the specified position of the schedule. The old value
     * will be shifted to the right.
     * 
     * @param index
     * @param value
     */
    public void put(int index, Number value) {
        powerValues.add(index, value);
    }

    /**
     * Replaces the value at the specified position with the given value
     * 
     * @param index
     * @param value
     */
    public void replace(int index, Number value) {
        powerValues.add(index, value);
        powerValues.remove(index + 1);
    }

    /**
     * Returns the value at the specified position
     * 
     * @param index
     * @return
     */
    public Number get(int index) {
        return powerValues.get(index);
    }

    /**
     * Returns the size of the schedule
     * 
     * @return
     */
    public int size() {
        return powerValues.size();
    }

    /**
     * Invokes the clear operation of the underlying list object
     */
    public void clear() {
        powerValues.clear();
    }

    public List<Number> getData() {
        return powerValues;
    }

    public String[] toStringArray() {
        String[] arr = new String[powerValues.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = powerValues.get(i) + "";
        }
        return arr;
    }

    @Override
    public String toString() {
        String s = "[";
        for (Number value : powerValues) {
            s += String.format("%.3f, ", value.doubleValue());
        }
        s = s.substring(0, s.length() - 2);
        s += "]";
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Schedule other = (Schedule) obj;
        if (powerValues == null) {
            if (other.powerValues != null) {
                return false;
            }
        } else {
            if (powerValues.size() != other.powerValues.size()) {
                return false;
            }
            for (int i = 0; i < powerValues.size(); i++) {
                if (powerValues.get(i).doubleValue() != other.powerValues.get(i)
                        .doubleValue()) {
                    return false;
                }
            }
        }

        return true;
    }
}
