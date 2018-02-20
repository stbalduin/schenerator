package de.schenerator.schedule;

import java.util.ArrayList;
import java.util.List;

import de.schenerator.Schenerator;

public class CSVScheduleFile {

	private List<Schedule> content;
	private String header;
	private String filename;
	private String path;
	private boolean isTransposed;
	private boolean fileFormatError;

	public CSVScheduleFile(List<Schedule> setContent) {
		content = setContent;
		header = "";
		filename = "sample.csv";
		path = Schenerator.getPath();
	}

	public CSVScheduleFile() {
		content = new ArrayList<>();
		header = "";
		filename = "sample.csv";
		path = Schenerator.getPath();
	}

	public void addToHeader(String line) {
		header += line;
	}

	public String getHeader() {
		return header;
	}

	public void addValue(int scheduleNo, Number value) {
		content.get(scheduleNo).add(value);
	}

	public void addSchedule(Schedule schedule) {
		content.add(schedule);
	}

	public int countValues() {
		int count = 0;
		for (Schedule schedule : content) {
			count += schedule.size();
		}
		return count;
	}

	public int countLines() {
		return content.size();
	}

	public int getLineSize(int lineNumber) {
		if (lineNumber >= content.size()) {
			return -1;
		}
		return content.get(lineNumber).size();
	}

	public int getMaxLineSize() {
		int maxLineSize = 0;
		for (Schedule schedule : content) {
			maxLineSize = (int) Math.max(maxLineSize, schedule.size());
		}
		return maxLineSize;
	}

	public void changeRowCount(int newRowCount) {
		List<Number> flattened = new ArrayList<>();
		for (Schedule schedule : content) {
			for (int i = 0; i < schedule.size(); i++) {
				flattened.add(schedule.get(i));
			}
		}
		clear();

		int lineLength = 0;
		if (flattened.size() % newRowCount == 0) {
			lineLength = flattened.size() / newRowCount;
		} else {

			lineLength = flattened.size() / (newRowCount) + 1;
		}

		for (int i = 0; i < flattened.size(); i++) {
			if (i % lineLength == 0) {
				content.add(new Schedule());
			}
			addValue(content.size() - 1, flattened.get(i));
		}

	}

	public void resizeLines(int newLineSize) {

		List<Number> flattened = new ArrayList<>();
		for (Schedule schedule : content) {
			for (int i = 0; i < schedule.size(); i++) {
				flattened.add(schedule.get(i));
			}
		}
		clear();
		// int lineLength = (int) Math.ceil(flattened.size() / (double)
		// newLineSize);
		for (int i = 0; i < flattened.size(); i++) {
			if (i % newLineSize == 0) {
				content.add(new Schedule());
			}
			addValue(content.size() - 1, flattened.get(i));
		}

	}

	public void resizeValuesPerLine(int newValueSize) {
		List<Number> flattened = new ArrayList<>();
		for (Schedule schedule : content) {
			for (int i = 0; i < schedule.size(); i++) {
				flattened.add(schedule.get(i));
			}
		}
		clear();
		for (int i = 0; i < flattened.size(); i++) {
			if (i % newValueSize == 0) {
				content.add(new Schedule());
			}
			addValue(content.size() - 1, flattened.get(i));
		}
	}

	public void transpose() {
		List<Schedule> transposed = new ArrayList<>();

		for (int i = 0; i < content.size(); i++) {
			for (int j = 0; j < content.get(i).size(); j++) {
				if (i == 0) {
					transposed.add(new Schedule());
				}

				transposed.get(j).add(content.get(i).get(j));

			}
		}
		clear();
		content = transposed;
		isTransposed = !isTransposed;
	}

	public void clear() {
		content.forEach(schedule -> schedule.clear());
		content.clear();
	}

	public Schedule getSchedule(int index) {
		return content.get(index);
	}

	// public Schedule getSchedule(int index) {
	// double[] powerValues = new double[content.get(index).size()];
	// for (int i = 0; i < powerValues.length; i++) {
	// powerValues[i] = content.get(index).get(i);
	// }
	// return new Schedule(powerValues);
	// }

	@Override
	public String toString() {
		String s = "[";
		for (Schedule schedule : content) {
			s += "[";
			for (int i = 0; i < schedule.size(); i++) {
				s += String.format("%.3f, ", schedule.get(i).doubleValue());
			}
			s = s.substring(0, s.length() - 2);
			s += "],\n";
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
		CSVScheduleFile other = (CSVScheduleFile) obj;
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (other.content == null) {
			return false;
		} else {
			if (content.size() != other.content.size()) {
				return false;
			}
			if (!header.equals(other.header)) {
				return false;
			}

			for (int i = 0; i < content.size(); i++) {
				if (!content.get(i).equals(other.content.get(i))) {
					return false;
				}
			}
		}

		return true;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setTransposed(boolean setTransposed) {
		isTransposed = setTransposed;
	}

	public boolean isTransposed() {
		return isTransposed;
	}

	public void setFileFormatError(boolean b) {
		fileFormatError = b;
	}

	public boolean isFileFormatWrong() {
		return fileFormatError;
	}

	public static void main(String[] args) {
		// List<List<Double>> matrix = new ArrayList<>();
		// for (int i = 0; i < 5; i++) {
		// List<Double> row = new ArrayList<>();
		// row.add(1.);
		// row.add(2.);
		// row.add(3.);
		// matrix.add(row);
		// }
		// CSVScheduleFile schedules = new CSVScheduleFile(matrix);
		// System.out.println(schedules.toString());
		// schedules.transpose();
		// System.out.println(schedules.toString());
	}

}
