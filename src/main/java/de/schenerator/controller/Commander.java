package de.schenerator.controller;

import de.schenerator.Configuration;
import de.schenerator.schedule.CSVScheduleFile;
import de.schenerator.schedule.Schedule;
import de.schenerator.util.NoiseMode;
import javafx.scene.chart.LineChart;

public interface Commander {

    boolean undo(int steps);
    
    boolean redo(int steps);
    
    void loadSchedule(Schedule schedule);

    Schedule getSchedule();

    void addChart(LineChart<Number, Number> scheduleChart);

    void addConfiguration(Configuration configuration);

    Configuration getConfiguration();

    void changeXAxisResolution(int lowerBound, int upperBound);

    void changeYAxisScale(double lowerBound, double upperBound);

    void applyNoise(NoiseMode mode, int strength, boolean percentage);

    void smooth(int range, double strength);

    void saveCurrentCSVFile(CSVScheduleFile csvFile);

    CSVScheduleFile getCurrentCSVFile();
}
