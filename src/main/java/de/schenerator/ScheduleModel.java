package de.schenerator;

import java.util.ArrayList;
import java.util.List;

import de.schenerator.schedule.Schedule;
import de.schenerator.util.NoiseMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

public class ScheduleModel {

    private Configuration config;
    private List<Schedule> history;

    private int currentHistorySchedule = 0;

    private XYChart.Series<Number, Number> scheduleSeries;

    public ScheduleModel(Schedule schedule, Configuration configuration) {
        config = configuration;
        history = new ArrayList<>();
        history.add(schedule);

        scheduleSeries = new XYChart.Series<Number, Number>(
                FXCollections.observableArrayList());

        for (int i = 0; i < schedule.size(); i++) {
            scheduleSeries.getData()
                    .add(new XYChart.Data<Number, Number>(i, schedule.get(i)));
        }
    }

    public void addToHistory(Schedule schedule) {
        for (int i = history.size() - 1; i > currentHistorySchedule; i--) {
            history.remove(i);
        }
        
        history.add(schedule);
        currentHistorySchedule++;

        loadScheduleToSeries(schedule);
    }

    private void loadScheduleToSeries(Schedule src) {
        for (int i = 0; i < src.size(); i++) {
            scheduleSeries.getData().get(i).setYValue(src.get(i));
        }
    }

    public XYChart.Series<Number, Number> getSeries() {
        return scheduleSeries;
    }

    // public void scaleXAxis(int newResolution, Configuration.XMode mode) {
    // double currentResolution = scheduleSeries.getData().size();
    // if (newResolution == currentResolution) {
    // return;
    // }
    // List<Number> newValues = new ArrayList<>();
    // if (newResolution < currentResolution) {
    // // Verkleinern
    // double removePercentage = newResolution / currentResolution;
    // int todoRefactor = (int) currentResolution / newResolution;
    // switch (mode) {
    //
    // case IGNORE:
    // /*
    // * Delete every nth value, whereby n is currentResolution
    // * divided by newResolution
    // */
    // for (int i = 0; i < currentResolution; i++) {
    // double val = i * removePercentage;
    // if (val == Math.floor(val)) {
    // newValues.add(scheduleSeries.getData().get(i + 1)
    // .getYValue());
    // }
    // }
    //
    // break;
    //
    // case MEAN:
    // /*
    // * Replace every n values by the mean of this n values, whereby
    // * n is currentResolution divided by newResolution
    // */
    // for (int i = 0; i < currentResolution; i++) {
    // if (i % todoRefactor == (todoRefactor - 1)) {
    // double mean = 0;
    // for (int j = 0; j < todoRefactor; j++) {
    // mean += scheduleSeries.getData().get(i - j)
    // .getYValue().doubleValue();
    // }
    // newValues.add(mean / todoRefactor);
    // }
    // }
    // break;
    //
    // case ADAPT:
    // /*
    // * Delete values intelligently so that the characteristic of the
    // * schedule is preserved TODO
    // */
    // break;
    // default:
    // return;
    // }
    //
    // // Refresh the chart data
    // scheduleSeries.getData().clear();
    // for (int i = 0; i < newValues.size(); i++) {
    // scheduleSeries.getData().add(
    // new XYChart.Data<Number, Number>(i, newValues.get(i)));
    // }
    //
    // } else {
    // // Vergroessern
    // int addPercentage = newResolution / (int) currentResolution;
    // switch (mode) {
    // case IGNORE:
    // /*
    // * Double the number of data points by simply duplicate existing
    // * values in each step
    // */
    // for (int i = 0; i < currentResolution; i++) {
    // for (int j = 0; j < addPercentage; j++) {
    // newValues.add(scheduleSeries.getData().get(i)
    // .getYValue().doubleValue());
    // }
    // }
    // break;
    //
    // case MEAN:
    // /*
    // * Double the number of data points by calculation the mean of
    // * current and next value in each step
    // */
    // while (newValues.size() < currentResolution) {
    // // Handle first value separately
    // newValues.add(getY(0) + (getY(0) - getY(1)) / 2);
    //
    // for (int i = 0; i < currentResolution; i++) {
    // // add the old value
    // newValues.add(getY(i));
    //
    // if (i < (currentResolution - 1)) {
    // // new value as mean of i and i+1
    // newValues.add((getY(i) + getY(i + 1)) / 2);
    // } else {
    //
    // }
    // }
    // }
    //
    // break;
    // case ADAPT:
    // /*
    // * Add new values intelligently so that the characteristic of
    // * the schedule is preserved
    // */
    // default:
    // return;
    // }
    // scheduleSeries.getData().clear();
    // for (int i = 0; i < newValues.size(); i++) {
    // scheduleSeries.getData().add(
    // new XYChart.Data<Number, Number>(i, newValues.get(i)));
    // }
    // // configureListeners();
    // }
    //
    // }

    // public void scaleYValues(double setMinY, double setMaxY) {
    // double min = scheduleSeries.getData().stream()
    // .min((d1,
    // d2) -> (Double.compare(d1.getYValue().doubleValue(),
    // d2.getYValue().doubleValue())))
    // .get().getYValue().doubleValue();
    //
    // double max = scheduleSeries.getData().stream()
    // .max((d1,
    // d2) -> (Double.compare(d1.getYValue().doubleValue(),
    // d2.getYValue().doubleValue())))
    // .get().getYValue().doubleValue();
    //
    // for (XYChart.Data<Number, Number> data : scheduleSeries.getData()) {
    // data.setYValue((data.getYValue().doubleValue() - min)
    // * (setMaxY - setMinY) / (max - min) + setMinY);
    // }
    // }

    // public void trimYValues(double setMinY, double setMaxY) {
    // for (XYChart.Data<Number, Number> data : scheduleSeries.getData()) {
    // data.setYValue(Math.max(setMinY,
    // Math.min(setMaxY, data.getYValue().doubleValue())));
    // }
    // }
    //
    // public void randomize(NoiseMode noiseMode, double strength,
    // boolean percentage, int smoothLevel) {
    //
    // for (int i = 0; i < size(); i = i + 1) {
    //
    // double newYValue = getNewValue(getY(i), strength, noiseMode,
    // percentage);
    //
    // if (smoothLevel > 0) {
    // double lastSmoothedValue = getY(i);
    // double currentSmoothedValue = 0;
    //
    // if (i > 0) {
    // for (int j = 1; j < smoothLevel; j++) {
    // if (i - j < 0) {
    // break;
    // }
    //
    // currentSmoothedValue = getSmoothedValue(
    // lastSmoothedValue, getY(i - j), j);
    //
    // setY(i - j, currentSmoothedValue);
    // lastSmoothedValue = currentSmoothedValue;
    //
    // }
    // }
    //
    // if (i < size() - 1) {
    // for (int j = 1; j < smoothLevel; j++) {
    // if (i + j >= size()) {
    // break;
    // }
    //
    // currentSmoothedValue = getSmoothedValue(
    // lastSmoothedValue, getY(i + j), j);
    //
    // setY(i + j, currentSmoothedValue);
    // lastSmoothedValue = currentSmoothedValue;
    // }
    // }
    // }
    // scheduleSeries.getData().get(i).setYValue(newYValue);
    // }
    //
    // }

    // /**
    // * Calculates a new value for the currentYValue depending on deviation,
    // * noiseMode and percentage flag. Returns the new value.
    // *
    // * @param currentYValue
    // * @param deviation
    // * @param noiseMode
    // * @param percentage
    // * @return
    // */
    // private double getNewValue(double currentYValue, double deviation,
    // NoiseMode noiseMode, boolean percentage) {
    // double offset = 0;
    //
    // // currentYValue = currentYValue +/- difference
    // switch (noiseMode) {
    // case UNIFORM:
    // // offset = +/- uniform-random*deviation
    // offset = (Schenerator.getRandom().nextDouble() * deviation * 2)
    // - deviation;
    // break;
    // case NORMAL:
    // // offset = +/- normal-random*deviation
    // offset = Schenerator.getRandom().nextGaussian() * deviation;
    // break;
    // default:
    // break;
    // }
    //
    // // 1/100 * currentYValue * offset
    // if (percentage) {
    // offset *= currentYValue / 100.0;
    // }
    //
    // return currentYValue + offset;
    //
    // }

    // private double getSmoothedValue(double last, double current, int
    // distance) {
    // double difference = last - current;
    // current += difference * 1 / (5.0 + distance);
    //
    // return current;
    // }
    //
    // public void loadDummyValues() {
    // for (int i = 0; i < 96; i++) {
    // XYChart.Data<Number, Number> data = new XYChart.Data<>(i,
    // Math.cos(i / Math.PI) * 100);
    // scheduleSeries.getData().add(data);
    // }
    // }

    public int size() {
        return scheduleSeries.getData().size();
    }

    public Schedule getSchedule() {
        return new Schedule(scheduleSeries.getData());
    }

    public Configuration getConfiguration() {
        return config;
    }

    public ObservableList<Data<Number, Number>> getData() {
        return scheduleSeries.getData();
    }

    @Override
    public String toString() {
        String s = "[";
        for (XYChart.Data<Number, Number> data : scheduleSeries.getData()) {
            s += data.getYValue().doubleValue() + ",";
        }
        s = s.substring(0, s.length() - 2);
        return s;
    }

    // public double getY(int index) {
    // return scheduleSeries.getData().get(index).getYValue().doubleValue();
    // }
    //
    // public void setY(int index, double newY) {
    // scheduleSeries.getData().get(index).setYValue(newY);
    // }

    public boolean undo(int steps) {

        // check if undo is possible
        if (currentHistorySchedule - steps < 0) {
            return false;
        }

        // decrement index counter and load schedule to series
        currentHistorySchedule -= steps;
        loadScheduleToSeries(history.get(currentHistorySchedule));
        return true;
    }

    public boolean redo(int steps) {

        // check if redo is possible
        if (currentHistorySchedule + steps >= history.size()) {
            return false;
        }

        // increment counter and load schedule to series
        currentHistorySchedule += steps;
        loadScheduleToSeries(history.get(currentHistorySchedule));
        return true;
    }
}
