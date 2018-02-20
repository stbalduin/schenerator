package de.schenerator;

import java.io.IOException;

import de.schenerator.controller.Commander;
import de.schenerator.controller.CommonCommander;
import de.schenerator.presenter.EditPresenter;
import de.schenerator.presenter.FilePresenter;
import de.schenerator.schedule.ScheduleOperation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class RootController {

    private FilePresenter filePresenter;
    private EditPresenter editPresenter;

    @FXML
    private AnchorPane root;

    @FXML
    private Tab fileTab;

    @FXML
    private AnchorPane fileOptAnchor;

    @FXML
    private Tab editTab;

    @FXML
    private AnchorPane editOptAnchor;

    @FXML
    private LineChart<Number, Number> scheduleChart;

    @FXML
    private Label xLabel;

    @FXML
    private Label yLabel;

    @FXML
    private Label action;

    private Commander commander;
    private Configuration config;

    @FXML
    public void initialize() throws Exception {
        commander = new CommonCommander();
        config = new Configuration();
        commander.addConfiguration(config);
        commander.addChart(scheduleChart);

        fileTab.setText(Schenerator.getString("menu.file.title"));
        editTab.setText(Schenerator.getString("menu.edit.title"));

        initFileOptions();
        initEditOptions();

        // Chart config
        scheduleChart.prefWidthProperty().bind(root.widthProperty());
        scheduleChart.prefHeightProperty()
                .bind(root.heightProperty().multiply(0.7));
        final NumberAxis xAxis = ((NumberAxis) scheduleChart.getXAxis());
        final NumberAxis yAxis = ((NumberAxis) scheduleChart.getYAxis());

        scheduleChart.setOnMouseMoved(e -> {

            Point2D pointInScene = new Point2D(e.getSceneX(), e.getSceneY());

            xLabel.setText(String.format("%.1f", xAxis.getValueForDisplay(
                    xAxis.sceneToLocal(pointInScene).getX())));
            yLabel.setText(String.format("%.2f", yAxis.getValueForDisplay(
                    yAxis.sceneToLocal(pointInScene).getY())));
        });

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(config.getSteps());

        yAxis.setForceZeroInRange(true);

        commander.loadSchedule(ScheduleOperation.createDummyValues(null, 96));

        scheduleChart.setCursor(Cursor.HAND);
        scheduleChart.setAnimated(false);
    }

    private void initFileOptions() {
        try {
            FXMLLoader fileOptLoader = new FXMLLoader(
                    getClass().getResource("/fxml/FileOptions.fxml"));
            AnchorPane pane = (AnchorPane) fileOptLoader.load();
            filePresenter = (FilePresenter) fileOptLoader.getController();
            fileOptAnchor.getChildren().clear();
            fileOptAnchor.getChildren().add(pane);
            // TODO: width & height property
            filePresenter.setCommander(commander);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initEditOptions() {
        try {
            FXMLLoader fileOptLoader = new FXMLLoader(
                    getClass().getResource("/fxml/EditOptions.fxml"));
            AnchorPane pane = (AnchorPane) fileOptLoader.load();
            editPresenter = (EditPresenter) fileOptLoader.getController();
            editOptAnchor.getChildren().clear();
            editOptAnchor.getChildren().add(pane);
            // TODO: width & height property
            editPresenter.setCommander(commander);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onMouseMoved(MouseEvent event) {

    }
}
