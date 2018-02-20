package de.schenerator.controller;

import java.util.ArrayList;
import java.util.List;

import de.schenerator.Configuration;
import de.schenerator.ScheduleChart;
import de.schenerator.ScheduleModel;
import de.schenerator.schedule.CSVScheduleFile;
import de.schenerator.schedule.Schedule;
import de.schenerator.schedule.ScheduleOperation;
import de.schenerator.util.NoiseMode;
import javafx.scene.chart.LineChart;

public class CommonCommander implements Commander {

    private List<ScheduleChart> charts;
    private List<Configuration> configs;

    private int currentChart = 0;
    private int currentConfig = 0;

    // quick and dirty
    private CSVScheduleFile currentHeader;

    public CommonCommander() {
        charts = new ArrayList<>();
        configs = new ArrayList<>();
        currentHeader = new CSVScheduleFile();
    }


    @Override
    public boolean undo(int steps) {
        return charts.get(currentChart).undo(1);
    }
    
    @Override
    public boolean redo(int steps) {
        return charts.get(currentChart).redo(1);
    }
    
    
    @Override
    public void loadSchedule(Schedule schedule) {
        ScheduleModel scheduleModel = new ScheduleModel(schedule,
                configs.get(currentConfig));

        charts.get(currentChart).addSchedule(scheduleModel);

        // TODO: parametrisieren und woanders aufrufen
        charts.get(currentChart).loadScheduleModel(0);
    }

    @Override
    public Schedule getSchedule() {
        return charts.get(currentChart).getCurrentSchedule();
    }

    @Override
    public void addChart(LineChart<Number, Number> scheduleChart) {
        charts.add(
                new ScheduleChart(scheduleChart, configs.get(currentConfig)));

    }

    @Override
    public void addConfiguration(Configuration configuration) {
        configs.add(configuration);

    }

    @Override
    public Configuration getConfiguration() {
        return configs.get(currentConfig);
    }

    @Override
    public void changeXAxisResolution(int lowerBound, int upperBound) {
        // schedules.get(currentSchedule).scaleXAxis(upperBound,
        // configs.get(currentConfig).getXMode());
        // charts.get(currentChart).setXBounds(lowerBound, upperBound);
    }

    @Override
    public void changeYAxisScale(double lowerBound, double upperBound) {
        ScheduleChart chart = charts.get(currentChart);
        switch (configs.get(currentConfig).getYMode()) {
        case SCALE:
            chart.addToHistory(ScheduleOperation.scaleYValues(
                    chart.getCurrentSchedule(), lowerBound, upperBound));
            break;
        case TRIM:
            chart.addToHistory(ScheduleOperation.trimYValues(
                    chart.getCurrentSchedule(), lowerBound, upperBound));
            break;
        default:
            break;
        }

    }

    @Override
    public void applyNoise(NoiseMode mode, int strength, boolean percentage) {
        ScheduleChart chart = charts.get(currentChart);
        chart.addToHistory(ScheduleOperation.copyWithNoise(
                chart.getCurrentSchedule(), mode, strength, percentage));

    }

    @Override
    public void smooth(int range, double strength) {
        ScheduleChart chart = charts.get(currentChart);
        chart.addToHistory(ScheduleOperation.smooth(chart.getCurrentSchedule(),
                range, strength));
    }

    @Override
    public void saveCurrentCSVFile(CSVScheduleFile csvFile) {
        currentHeader = csvFile;
    }

    @Override
    public CSVScheduleFile getCurrentCSVFile() {
        if (currentHeader == null) {
            currentHeader = new CSVScheduleFile();
        }
        return currentHeader;
    }
}
