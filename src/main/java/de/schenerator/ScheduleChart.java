package de.schenerator;

import java.util.ArrayList;
import java.util.List;

import de.schenerator.schedule.Schedule;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ScheduleChart {
    private LineChart<Number, Number> scheduleChart;
    private List<ScheduleModel> schedules;
    private Configuration config;

    private int maxSchedules = 1;
    private int currentSchedule = -1;

    public ScheduleChart(LineChart<Number, Number> scheduleChart,
            Configuration configuration) {
        this.scheduleChart = scheduleChart;
        this.config = configuration;
        this.schedules = new ArrayList<>();
    }

    public void addSchedule(ScheduleModel scheduleModel) {
        if (schedules.size() >= maxSchedules) {
            scheduleChart.getData().clear();
            schedules.clear();
            currentSchedule = -1;
        }

        scheduleChart.getData().add(scheduleModel.getSeries());
        schedules.add(scheduleModel);
        currentSchedule++;

        setXBounds(0, scheduleModel.size());
    }

    public void loadScheduleModel(int index) {
        final NumberAxis yAxis = ((NumberAxis) scheduleChart.getYAxis());
        ScheduleModel scheduleModel = schedules.get(index);
        for (XYChart.Data<Number, Number> data : scheduleModel.getData()) {
            Node node = data.getNode();

            final List<Node> leftNodes = new ArrayList<>();
            final List<Node> rightNodes = new ArrayList<>();
            final int nodeIndex = data.getXValue().intValue();

            node.setCursor(Cursor.OPEN_HAND);

            node.setOnMousePressed(event -> {

                leftNodes.clear();
                rightNodes.clear();
                for (int i = 0; i < scheduleModel.getConfiguration()
                        .getLeftChangeRange(); i++) {
                    if (nodeIndex - (i + 1) >= 0) {
                        leftNodes.add(scheduleModel.getData()
                                .get(nodeIndex - (i + 1)).getNode());
                    }
                }

                for (int i = 0; i < scheduleModel.getConfiguration()
                        .getRightChangeRange(); i++) {
                    if (nodeIndex + (i + 1) < scheduleModel.getConfiguration()
                            .getSteps()) {
                        rightNodes.add(scheduleModel.getData()
                                .get(nodeIndex + (i + 1)).getNode());
                    }
                }

                leftNodes.forEach(n -> n.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, #00be23, 10, 0.3, 0, 0);"));
                rightNodes.forEach(n -> n.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, #00be23, 10, 0.3, 0, 0);"));
                node.setStyle(
                        "-fx-effect: dropshadow(three-pass-box, #ed9900, 10, 0.8, 0, 0);");

                node.setCursor(Cursor.CLOSED_HAND);

            });

            node.setOnMouseDragged(event -> {
                Point2D pointInScene = new Point2D(event.getSceneX(),
                        event.getSceneY());
                double yAxisLoc = yAxis.sceneToLocal(pointInScene).getY();
                Number y = yAxis.getValueForDisplay(yAxisLoc);
                data.setYValue(y);
            });

            node.setOnMouseReleased(event -> {
                leftNodes.forEach(n -> n.setStyle("-fx-effect: None;"));
                rightNodes.forEach(n -> n.setStyle("-fx-effect: None;"));
                node.setStyle("-fx-effect: None;");
                node.setCursor(Cursor.OPEN_HAND);
                scheduleChart.setAnimated(true);
                Number newY = data.getYValue();

                for (int i = 0; i < leftNodes.size(); i++) {
                    XYChart.Data<Number, Number> left = scheduleModel.getData()
                            .get(nodeIndex - (i + 1));
                    double newValue = left.getYValue().doubleValue()
                            + (leftNodes.size() - i) * ((newY.doubleValue()
                                    - left.getYValue().doubleValue())
                                    / (leftNodes.size() + 1));
                    left.setYValue(newValue);

                }

                for (int i = 0; i < rightNodes.size(); i++) {
                    XYChart.Data<Number, Number> right = scheduleModel.getData()
                            .get(nodeIndex + (i + 1));
                    double newValue = right.getYValue().doubleValue()
                            + (rightNodes.size() - i) * ((newY.doubleValue()
                                    - right.getYValue().doubleValue())
                                    / (rightNodes.size() + 1));
                    right.setYValue(newValue);

                }

                scheduleChart.setAnimated(false);

                scheduleModel.addToHistory(scheduleModel.getSchedule());
            });

            scheduleChart.setLegendVisible(false);

        }
    }

    public Schedule getCurrentSchedule() {
        return schedules.get(currentSchedule).getSchedule();
    }

    public void setXBounds(int lowerBound, int upperBound) {
        for (ScheduleModel scheduleModel : schedules) {
            upperBound = (int) Math.max(upperBound, scheduleModel.size());
        }
        ((NumberAxis) scheduleChart.getXAxis()).setUpperBound(upperBound);
    }

    public void addToHistory(Schedule schedule) {
        schedules.get(currentSchedule).addToHistory(schedule);
        loadScheduleModel(currentSchedule);
    }

    public boolean undo(int steps) {
        boolean success = schedules.get(currentSchedule).undo(1);
        if (success) {
            loadScheduleModel(currentSchedule);
            return true;
        }
        return false;
    }
    
    public boolean redo(int steps) {
        boolean success = schedules.get(currentSchedule).redo(1);
        if (success) {
            loadScheduleModel(currentSchedule);
            return true;
        }
        return false;
    }

}
