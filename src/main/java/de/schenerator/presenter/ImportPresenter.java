package de.schenerator.presenter;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import de.schenerator.Schenerator;
import de.schenerator.schedule.CSVScheduleFile;
import de.schenerator.schedule.Schedule;
import de.schenerator.util.CSVUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;

public class ImportPresenter extends WindowPresenter {

	@FXML
	private Label pathLabel;

	@FXML
	private TextField scheduleFileTextField;

	@FXML
	private Button scheduleFileButton;

	@FXML
	private Label dataLabel;

	@FXML
	private Label valueTextLabel;

	@FXML
	private Label valueCountLabel;

	@FXML
	private TableView<CSVRow> csvContentTableView;

	@FXML
	private Label reshapeLabel;

	@FXML
	private Label rowCountLabel;
	@FXML
	private TextField rowCountTextField;

	@FXML
	private Label columnCountLabel;
	@FXML
	private TextField columnCountTextField;

	@FXML
	private Label transposeLabel;
	@FXML
	private CheckBox transposeCheckbox;

	@FXML
	private Label previewLabel;

	@FXML
	private LineChart<String, Number> previewChart;

	@FXML
	private Label selectedRowLabel;

	@FXML
	private TextField selectedRowTextField;

	@FXML
	private Button importButton;

	@FXML
	private Button cancelButton;

	private CSVScheduleFile csvFile;
	private volatile boolean operationRunning = false;

	@FXML
	private void initialize() {
		System.out.println("initalized!");
		localize();
		initRowCount();
		initColumnCount();
		initTranspose();
		initSelectedRow();
		initDataPreview();
	}

	private void localize() {
		pathLabel.setText(Schenerator.getString("import.path"));
		dataLabel.setText(Schenerator.getString("import.data"));
		valueTextLabel.setText(Schenerator.getString("import.valueCount"));
		reshapeLabel.setText(Schenerator.getString("import.reshape"));
		rowCountLabel.setText(Schenerator.getString("import.rowCount"));
		columnCountLabel.setText(Schenerator.getString("import.columnCount"));
		transposeLabel.setText(Schenerator.getString("import.transpose"));
		previewLabel.setText(Schenerator.getString("import.preview"));
		selectedRowLabel.setText(Schenerator.getString("import.selectedRow"));
		importButton.setText(Schenerator.getString("import.importButton"));
		cancelButton.setText(Schenerator.getString("import.cancelButton"));
	}

	private void initRowCount() {
		rowCountTextField.setTextFormatter(
				new TextFormatter<>(new IntegerStringConverter(), 0,
						c -> Pattern.matches("\\d*", c.getText()) ? c : null));

		rowCountTextField.textProperty()
				.addListener((change, oldVal, newVal) -> {
					if (newVal.equals("") || newVal.equals(oldVal)) {
						return;
					}

					if (!operationRunning) {
						operationRunning = true;
						csvFile.changeRowCount(Integer.valueOf(newVal));
						loadToTable(csvFile);
						csvContentTableView.refresh();
						columnCountTextField
								.setText("" + csvFile.getMaxLineSize());
						selectedRowTextField.setText("0");

						operationRunning = false;
					}
				});
	}

	private void initColumnCount() {
		columnCountTextField.setTextFormatter(
				new TextFormatter<>(new IntegerStringConverter(), 0,
						c -> Pattern.matches("\\d*", c.getText()) ? c : null));

		columnCountTextField.textProperty()
				.addListener((change, oldVal, newVal) -> {
					if (newVal.equals("") || newVal.equals(oldVal)) {
						return;
					}

					if (!operationRunning) {
						operationRunning = true;
						csvFile.resizeLines(Integer.valueOf(newVal));
						loadToTable(csvFile);
						csvContentTableView.refresh();
						rowCountTextField.setText("" + csvFile.countLines());
						selectedRowTextField.setText("-1");
						operationRunning = false;
					}
				});
	}

	private void initTranspose() {
		transposeCheckbox.selectedProperty()
				.addListener((change, oldVal, newVal) -> {
					if (oldVal == newVal) {
						return;
					}
					if (!operationRunning) {
						operationRunning = true;
						csvFile.transpose();
						loadToTable(csvFile);
						csvContentTableView.refresh();
						// rowCountTextField.setText("" + csvFile.countLines());
						// selectedRowTextField.setText("-1");
						operationRunning = false;
					}

				});
	}

	private void initSelectedRow() {
		selectedRowTextField.setTextFormatter(
				new TextFormatter<>(new IntegerStringConverter(), 0,
						c -> Pattern.matches("\\d*", c.getText()) ? c : null));

	}

	private void initDataPreview() {
		csvContentTableView.getSelectionModel().selectedIndexProperty()
				.addListener((change, oldVal, newVal) -> {
					selectedRowTextField.setText("" + csvContentTableView
							.getSelectionModel().getSelectedIndex());
				});

		csvContentTableView.getSelectionModel().selectedItemProperty()
				.addListener((change, oldVal, newVal) -> {
					if (oldVal == newVal || newVal == null) {
						return;
					}

					// clean old chart
					previewChart.setAnimated(false);
					previewChart.getData().clear();

					// populate chart with new data
					XYChart.Series<String, Number> series = new XYChart.Series<>(
							FXCollections.observableArrayList());
					for (int i = 0; i < newVal.size(); i++) {
						series.getData().add(new XYChart.Data<String, Number>(
								"" + i, newVal.getValue(i)));
					}
					previewChart.getData().add(series);
				});
	}

	@FXML
	public void onScheduleFileButtonAction(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Schenerator.getString("menu.import.dialog.title"));

		File initDir = new File(Schenerator.getPath());
		if (initDir.exists()) {
			fileChooser.setInitialDirectory(initDir);
		}

		File file = fileChooser.showOpenDialog(stage);

		if (file == null) {
			// TODO: message
			return;
		}

		scheduleFileTextField.setText(file.getAbsolutePath());

		csvFile = CSVUtil.analyseScheduleCSV(file.getAbsolutePath());

		if (!csvFile.isFileFormatWrong()) {
			loadToTable(csvFile);
		}
	}

	private void loadToTable(CSVScheduleFile csvFile) {

		// clean old table
		csvContentTableView.getColumns().clear();
		csvContentTableView.getItems().clear();

		// add empty columns to table
		for (int i = 0; i < csvFile.getMaxLineSize(); i++) {
			final int index = i;
			TableColumn<CSVRow, String> column = new TableColumn<CSVRow, String>(
					"" + i);
			column.setCellValueFactory(cellData -> new SimpleStringProperty(
					cellData.getValue().getValueString(index)));

			csvContentTableView.getColumns().add(column);
		}

		// fill table with data
		for (int i = 0; i < csvFile.countLines(); i++) {
			csvContentTableView.getItems()
					.add(new CSVRow(csvFile.getSchedule(i)));
		}

		initDataPreview();

		operationRunning = true;
		// display some meta data
		selectedRowTextField.setText(""
				+ csvContentTableView.getSelectionModel().getSelectedIndex());
		rowCountTextField.setText("" + csvFile.countLines());
		columnCountTextField.setText("" + csvFile.getMaxLineSize());
		valueCountLabel.setText("" + csvFile.countValues());
		operationRunning = false;

	}

	@FXML
	public void onImportButtonAction(ActionEvent event) {
		// scheduleFileTextField.getText();
		// CSVScheduleFile lines = CSVUtil
		// .analyseScheduleCSV(scheduleFileTextField.getText());
		// lines.resizeLines(Integer.valueOf(rowCountTextField.getText()));
		commander.loadSchedule(csvFile
				.getSchedule(Integer.valueOf(selectedRowTextField.getText())));
		commander.saveCurrentCSVFile(csvFile);
		stage.close();
	}

	@FXML
	public void onCancelButtonAction(ActionEvent event) {
		stage.close();
	}

	public void setFile(File file) {
		scheduleFileTextField.setText(file.getAbsolutePath());

		csvFile = CSVUtil.analyseScheduleCSV(file.getAbsolutePath());
		if (!csvFile.isFileFormatWrong()) {
			loadToTable(csvFile);
		}
	}
}

class CSVRow {

	Double[] rowValues;

	public CSVRow(List<Double> values) {

		rowValues = new Double[values.size()];

		for (int i = 0; i < rowValues.length; i++) {
			rowValues[i] = values.get(i);
		}

	}

	public CSVRow(Schedule schedule) {
		rowValues = new Double[schedule.size()];

		for (int i = 0; i < rowValues.length; i++) {
			rowValues[i] = schedule.get(i).doubleValue();
		}
	}

	public CSVRow(Double... values) {
		rowValues = values;
	}

	public Double getValue(int index) {
		return rowValues[index];
	}

	public String getValueString(int index) {
		if (index >= rowValues.length) {
			return "";
		}
		return String.format("%.3f", rowValues[index].doubleValue());
	}

	public int size() {
		return rowValues.length;
	}
}
