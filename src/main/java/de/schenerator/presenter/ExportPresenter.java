package de.schenerator.presenter;

import java.io.File;
import java.util.regex.Pattern;

import de.schenerator.Schenerator;
import de.schenerator.controller.Commander;
import de.schenerator.schedule.CSVScheduleFile;
import de.schenerator.schedule.Schedule;
import de.schenerator.schedule.ScheduleOperation;
import de.schenerator.util.CSVUtil;
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
import javafx.stage.DirectoryChooser;
import javafx.util.converter.IntegerStringConverter;

public class ExportPresenter extends WindowPresenter {

	@FXML
	private Label pathLabel;
	@FXML
	private TextField pathTextField;
	@FXML
	private Button pathButton;

	@FXML
	private Label filenameLabel;
	@FXML
	private TextField filenameTextField;

	@FXML
	private Label multiFileLabel;
	@FXML
	private CheckBox multiFileCheckBox;

	@FXML
	private Label numberSchedulesLabel;
	@FXML
	private Button numberSchedulesDecrButton;
	@FXML
	private TextField numberSchedulesTextField;
	@FXML
	private Button numberSchedulesIncrButton;

	@FXML
	private Label deviationOptionsLabel;

	@FXML
	private Label percentageLabel;
	@FXML
	private CheckBox percentageCheckBox;

	@FXML
	private Label strengthLabel;
	@FXML
	private TextField strengthTextField;

	@FXML
	private Label typeLabel;
	@FXML
	private ChoiceBox<String> typeChoiceBox;

	@FXML
	private Label headerLabel;
	@FXML
	private TextField headerTextField;

	@FXML
	private Label transposeLabel;
	@FXML
	private CheckBox transposeCheckBox;

	@FXML
	private Button cancelButton;
	@FXML
	private Button exportButton;

	private CSVScheduleFile csvFile;

	@FXML
	private void initialize() {
		localize();
		configureTextFormatter();
		configureChoices();

	}

	private void localize() {
		pathLabel.setText(Schenerator.getString("export.path"));
		filenameLabel.setText(Schenerator.getString("export.filename"));
		multiFileLabel.setText(Schenerator.getString("export.multipleFiles"));
		numberSchedulesLabel
				.setText(Schenerator.getString("export.numberSchedules"));
		deviationOptionsLabel
				.setText(Schenerator.getString("export.deviationOptions"));
		percentageLabel.setText(Schenerator.getString("export.percentage"));
		strengthLabel.setText(Schenerator.getString("export.strength"));
		typeLabel.setText(Schenerator.getString("export.type"));
		headerLabel.setText(Schenerator.getString("export.header"));
		transposeLabel.setText(Schenerator.getString("export.transpose"));
		cancelButton.setText(Schenerator.getString("export.cancelButton"));
		exportButton.setText(Schenerator.getString("export.exportButton"));
	}

	private void configureTextFormatter() {
		numberSchedulesTextField.setTextFormatter(
				new TextFormatter<>(new IntegerStringConverter(), 1,
						c -> Pattern.matches("\\d*", c.getText()) ? c : null));
		strengthTextField.setTextFormatter(
				new TextFormatter<>(new IntegerStringConverter(), 0,
						c -> Pattern.matches("\\d*", c.getText()) ? c : null));
	}

	private void configureChoices() {
		typeChoiceBox.setItems(FXCollections.observableArrayList(
				Schenerator.getString("menu.noise.uniform"),
				Schenerator.getString("menu.noise.normal")));
		typeChoiceBox.getSelectionModel().select(0);
	}

	@FXML
	private void onPathButtonAction(ActionEvent event) {

		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle(Schenerator.getString("menu.export.dialog.title"));
		File initDir = new File(Schenerator.getPath());
		if (initDir.exists()) {
			dirChooser.setInitialDirectory(initDir);
		}

		// Show save file dialog
		File file = dirChooser.showDialog(stage);
		// .showSaveDialog(Schenerator.getStage());
		if (file == null) {
			return;
		}

		pathTextField.setText(file.getAbsolutePath());
	}

	@FXML
	private void onNumberSchedulesDecrButtonAction(ActionEvent event) {
		int currentValue = Integer.valueOf(numberSchedulesTextField.getText());
		currentValue = (int) Math.max(1, currentValue - 1);
		numberSchedulesTextField.setText("" + currentValue);
	}

	@FXML
	private void onNumberSchedulesIncrButtonAction(ActionEvent event) {
		numberSchedulesTextField.setText(
				"" + (Integer.valueOf(numberSchedulesTextField.getText()) + 1));
	}

	@FXML
	private void onCancelButtonAction(ActionEvent event) {
		stage.close();
	}

	@FXML
	private void onExportButtonAction(ActionEvent event) {
		String path = pathTextField.getText();
		String filePath = filenameTextField.getText();
		if (filePath.endsWith(".csv")) {
			filePath = filePath.substring(0, filePath.length() - 4);
		}
		boolean multipleFiles = multiFileCheckBox.isSelected();
		int numberSchedules = Integer
				.valueOf(numberSchedulesTextField.getText());
		NoiseMode noiseMode = NoiseMode
				.getEnum(typeChoiceBox.getSelectionModel().getSelectedItem());
		int strength = Integer.valueOf(strengthTextField.getText());
		boolean percentage = percentageCheckBox.isArmed();
		Schedule schedule = commander.getSchedule();
		String header = headerTextField.getText();

		if (multipleFiles) {
			for (int i = 0; i < numberSchedules; i++) {
				CSVScheduleFile csvFile = new CSVScheduleFile();
				csvFile.setPath(path);
				csvFile.setFilename(filePath + "_" + i + ".csv");
				csvFile.addToHeader(header);
				if (i == 0) {
					csvFile.addSchedule(schedule);
				} else {
					csvFile.addSchedule(ScheduleOperation.copyWithNoise(
							schedule, noiseMode, strength, percentage));
				}
				if (transposeCheckBox.isSelected()) {
					csvFile.transpose();
				}
				CSVUtil.writeCSVScheduleFile(csvFile);
			}

			// CSVUtil.writeScheduleToCsv(path, schedule);
			//
			// for (int i = 2; i < numberSchedules; i++) {
			// Schedule noiseSchedule =
			// ScheduleOperation.copyWithNoise(schedule, noiseMode,
			// strength, 5, percentage);
			// CSVUtil.writeScheduleToCsv(path + File.separator + filePath + "_"
			// + i +
			// ".csv", noiseSchedule);
			// }
		} else {
			CSVScheduleFile csvFile = new CSVScheduleFile();
			csvFile.setPath(path);
			csvFile.setFilename(filePath + ".csv");
			csvFile.addToHeader(header);

			for (int i = 0; i < numberSchedules; i++) {
				if (i == 0) {
					csvFile.addSchedule(schedule);
				} else {
					csvFile.addSchedule(ScheduleOperation.copyWithNoise(
							schedule, noiseMode, strength, percentage));
				}
			}

			if (transposeCheckBox.isSelected()) {
				csvFile.transpose();
			}
			CSVUtil.writeCSVScheduleFile(csvFile);
		}
		stage.close();

	}

	@Override
	public void setCommander(Commander commander) {
		this.commander = commander;

		csvFile = new CSVScheduleFile();
		csvFile.addToHeader(commander.getCurrentCSVFile().getHeader());
		csvFile.setFilename(commander.getCurrentCSVFile().getFilename());
		csvFile.setPath(commander.getCurrentCSVFile().getPath());

		// TODO: empty old csv?
		pathTextField.setText(csvFile.getPath());
		String filename = csvFile.getFilename();

		filename = filename.endsWith(".csv")
				? filename.substring(filename.length() - 4)
				: filename;

		filenameTextField.setText("copy-" + filename);
		headerTextField.setText(csvFile.getHeader());
		transposeCheckBox
				.setSelected(commander.getCurrentCSVFile().isTransposed());

	}

}
