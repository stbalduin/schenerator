package de.schenerator.presenter;

import java.util.regex.Pattern;

import de.schenerator.Configuration;
import de.schenerator.Schenerator;
import de.schenerator.util.NoiseMode;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class EditPresenter extends Presenter {

    // @FXML
    // private ChoiceBox<Number> xResolutionSelector;

    // @FXML
    // private Label xAxisLabel;

    @FXML
    private Label yAxisLabel;

    @FXML
    private Label yAxisScaleToLabel;

    @FXML
    private TextField minYTextField;

    @FXML
    private TextField maxYTextField;

    @FXML
    private Button scaleYButton;

    @FXML
    private Button trimYButton;

    @FXML
    private Label changeRangeLabel;

    @FXML
    private TextField changeLeftRangeTextField;

    @FXML
    private CheckBox sameRangeCheckBox;

    @FXML
    private TextField changeRightRangeTextField;

    @FXML
    private Label noiseLabel;

    @FXML
    private TextField noiseStrengthTextField;

    @FXML
    private CheckBox percentageCheckBox;

    @FXML
    private ChoiceBox<String> noiseSelector;

    @FXML
    private Button applyNoiseButton;

    @FXML
    public void initialize() {
        localize();
        // initXAxisOptions();
        initYAxisOptions();
        initChangeRangeOptions();
        initNoiseOptions();

    }

    private void localize() {
        // xAxisLabel.setText(Schenerator.getString("menu.xAxis.title"));
        yAxisLabel.setText(Schenerator.getString("menu.yAxis.title"));
        yAxisScaleToLabel.setText(Schenerator.getString("menu.yAxis.scaleTo"));
        scaleYButton.setText(Schenerator.getString("menu.yAxis.scaleButton"));
        trimYButton.setText(Schenerator.getString("menu.yAxis.trimButton"));
        changeRangeLabel.setText(Schenerator.getString("menu.changeRange"));
        sameRangeCheckBox.setText(Schenerator.getString("menu.sameRange"));
        noiseLabel.setText(Schenerator.getString("menu.noise.title"));
        percentageCheckBox
                .setText(Schenerator.getString("menu.noise.percentage"));
        applyNoiseButton
                .setText(Schenerator.getString("menu.noise.applyNoiseButton"));
    }

    // private void initXAxisOptions() {
    // xResolutionSelector.setItems(
    // FXCollections.observableArrayList(6, 12, 24, 48, 96, 192));
    // xResolutionSelector.getSelectionModel().select(4);
    // xResolutionSelector.getSelectionModel().selectedItemProperty()
    // .addListener((change, oldVal, newVal) -> {
    // System.out.println("Change: " + newVal.intValue());
    // commander.getConfiguration().setSteps(newVal.intValue());
    // commander.changeXAxisResolution(0, newVal.intValue());
    // });
    // }

    private void initYAxisOptions() {
        minYTextField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, c -> {
                    return Pattern.matches("-?\\d*", c.getText()) ? c : null;
                }));

        maxYTextField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, c -> {
                    return Pattern.matches("-?\\d*", c.getText()) ? c : null;
                }));
    }

    private void initChangeRangeOptions() {
        changeLeftRangeTextField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, c -> {
                    return Pattern.matches("\\d*", c.getText()) ? c : null;

                }));
        changeRightRangeTextField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0,
                        c -> Pattern.matches("\\d*", c.getText()) ? c : null));

        changeLeftRangeTextField.textProperty()
                .addListener((change, oldVal, newVal) -> {
                    if (newVal.equals("")) {
                        return;
                    }
                    commander.getConfiguration()
                            .setLeftChangeRange(Integer.parseInt(newVal));
                    if (sameRangeCheckBox.isSelected()) {
                        changeRightRangeTextField.setText(newVal);

                    }
                });

        changeRightRangeTextField.textProperty()
                .addListener((change, oldVal, newVal) -> {
                    if (newVal.equals("")) {
                        return;
                    }
                    commander.getConfiguration()
                            .setRightChangeRange(Integer.parseInt(newVal));
                });

        sameRangeCheckBox.selectedProperty()
                .addListener((change, oldVal, newVal) -> {

                    changeRightRangeTextField.setDisable(newVal);
                    // changeRightRangeSelector.setDisable(newVal);
                    if (newVal) {
                        changeRightRangeTextField
                                .setText(changeLeftRangeTextField.getText());
                    }

                });
    }

    private void initNoiseOptions() {
        noiseStrengthTextField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, c -> {
                    return Pattern.matches("-?\\d*", c.getText()) ? c : null;
                }));

        noiseStrengthTextField.setText("10");
        noiseStrengthTextField.textProperty()
                .addListener((change, oldVal, newVal) -> {
                    if (newVal.equals("")) {
                        return;
                    }
                    commander.getConfiguration()
                            .setNoiseStrength(Integer.parseInt(newVal));
                });

        percentageCheckBox.selectedProperty()
                .addListener((change, oldVal, newVal) -> {
                    commander.getConfiguration().setNoisePercentage(newVal);
                });

        noiseSelector.setItems(FXCollections.observableArrayList(
                Schenerator.getString("menu.noise.uniform"),
                Schenerator.getString("menu.noise.normal")));
        noiseSelector.getSelectionModel().select(0);
        noiseSelector.getSelectionModel().selectedItemProperty()
                .addListener((change, oldVal, newVal) -> {
                    commander.getConfiguration().setNoiseMode(newVal);
                });
    }

    @FXML
    public void onScaleYAction(ActionEvent event) {
        try {
            double setMinY = Double.parseDouble(minYTextField.getText());
            double setMaxY = Double.parseDouble(maxYTextField.getText());

            commander.getConfiguration().setYMode(Configuration.YMode.SCALE);
            commander.changeYAxisScale(setMinY, setMaxY);
            // scheduleModel.scaleYValues(setMinY, setMaxY);

        } catch (NumberFormatException e) {

        }
    }

    @FXML
    public void onTrimYAction(ActionEvent event) {
        try {
            double setMinY = Double.parseDouble(minYTextField.getText());
            double setMaxY = Double.parseDouble(maxYTextField.getText());
            commander.getConfiguration().setYMode(Configuration.YMode.TRIM);
            commander.changeYAxisScale(setMinY, setMaxY);

        } catch (NumberFormatException e) {

        }
    }

    @FXML
    public void onNoiseApplyAction(ActionEvent event) {
        String modeText = noiseSelector.getSelectionModel().getSelectedItem();
        NoiseMode mode = NoiseMode.NORMAL;

        if (modeText.equals(Schenerator.getString("menu.noise.uniform"))) {
            mode = NoiseMode.UNIFORM;
        }

        int noiseStrength = Integer.parseInt(noiseStrengthTextField.getText());
        boolean percentage = percentageCheckBox.isSelected();
        commander.applyNoise(mode, noiseStrength, percentage);
    }

    public void onSmoothAction(ActionEvent event) {

        commander.smooth(5, 0.5);
    }
}
