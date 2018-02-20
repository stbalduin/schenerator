package de.schenerator.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import de.schenerator.schedule.CSVScheduleFile;
import de.schenerator.schedule.Schedule;

public class CSVUtil {
    // CSV & Schedule
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    private static final int DEFAULT_SKIP_LINES = 0;
    private static final String NEWLINE = System.lineSeparator();

    private static char separator = DEFAULT_SEPARATOR;
    private static char quote = DEFAULT_QUOTE;
    private static int skipLines = DEFAULT_SKIP_LINES;
    private static String newLine = NEWLINE;

    public static void writeScheduleToCsv(String filename, Schedule schedule) {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        try (CSVWriter writer = new CSVWriter(new FileWriter(file),
                DEFAULT_SEPARATOR, DEFAULT_QUOTE);) {

            List<String[]> content = new ArrayList<>();
            content.add(schedule.toStringArray());
            writer.writeAll(content);
        } catch (IOException io) {

        }
    }

    /**
     * 
     * @param csvFile
     */
    public static void writeCSVScheduleFile(CSVScheduleFile csvFile) {
        File file = new File(
                csvFile.getPath() + File.separator + csvFile.getFilename());
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            if (!csvFile.getHeader().equals("")) {
                String line = csvFile.getHeader();
                if (!csvFile.getHeader().endsWith(newLine)) {
                    line += newLine;
                }
                writer.write(line);
            }

            for (int i = 0; i < csvFile.countLines(); i++) {
                Schedule schedule = csvFile.getSchedule(i);
                String line = "";

                for (int j = 0; j < schedule.size(); j++) {
                    line += schedule.get(j);

                    if (j < schedule.size() - 1) {
                        line += separator;
                    } else {
                        line += newLine;
                    }
                }
                writer.write(line);
            }

        } catch (IOException io) {

        }
    }

    public static Schedule readScheduleFromCsv(String filename) {
        return readScheduleFromCsv(filename, skipLines, separator, quote);
    }

    public static Schedule readScheduleFromCsv(String filename, int skipLines,
            char separator, char quote) {

        // long[] schedule = new long[96];
        double[] schedule = new double[96];
        String[] line = new String[1];

        try (CSVReader reader = new CSVReader(new FileReader(filename),
                separator, quote);) {

            for (int i = 0; i < skipLines; i++) {
                reader.readNext();
            }

            while ((line = reader.readNext()) != null) {
                schedule = Stream.of(line).mapToDouble(Double::parseDouble)
                        .toArray();

            }
            return new Schedule(schedule);

        } catch (IOException io) {
            System.out.println("IOException occured!" + io.getMessage());
            return new Schedule(new double[96]);

        } catch (NumberFormatException nfe) {

            return readScheduleFromCsv(filename, ++skipLines, separator, quote);
        }

        // try (CSVReader reader = new CSVReader(new FileReader(filename),
        // DEFAULT_SEPARATOR, quote);) {
        //
        // for (int i = 0; i < skipLines; i++) {
        // reader.readNext();
        // }
        // while ((line = reader.readNext()) != null) {
        // if (line.length > 96) {
        // line = ArrayUtils.subarray(line, 0, 96);
        // }
        //
        // scheduleD =
        // Stream.of(line).mapToDouble(Double::parseDouble).toArray();
        //
        // }
        // return new Schedule(scheduleD);
        //
        // } catch (IOException io2) {
        // System.out.println("IOException occured!" + io2.getMessage());
        // return new Schedule(new long[96]);
        // } catch (NumberFormatException nfe2) {
        //
        // // System.out.println("Skipped line: " + line[0]);
        // return readScheduleFromCsv(filename, ++skipLines);
        // }

    }

    public static CSVScheduleFile analyseScheduleCSV(String filename) {
        return analyseScheduleCSV(filename, 0);
    }

    /**
     * 
     * @param filename
     * @param skipLines
     * @return
     */
    public static CSVScheduleFile analyseScheduleCSV(String filename,
            int skipLines) {
        System.out.println("Start CSV analysis.");

        CSVScheduleFile lines = new CSVScheduleFile();
        lines.setPath(
                filename.substring(0, filename.lastIndexOf(File.separator)));
        lines.setFilename(
                filename.substring(filename.lastIndexOf(File.separator) + 1));

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            // StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            String lineAsRead = line;
            for (int i = 0; i < skipLines; i++) {
                lines.addToHeader(line);
                line = br.readLine();
            }

            newLine: while (line != null) {
                Schedule values = new Schedule();
                // remove quotes
                line = line.replace(String.valueOf('"'), "");
                line = line.replace(String.valueOf('\''), "");

                // skip empty lines
                if (line.length() <= 0) {
                    line = br.readLine();
                    continue;
                }

                // get the first value
                // TODO: was passiert, wenn die Zeile keinen separator, sondern
                // nur ein endline hat?
                String value = line.substring(0,
                        line.indexOf(String.valueOf(separator)));

                while (value != null) {
                    try {
                        // add value
                        values.add(Double.valueOf(value));
                    } catch (NumberFormatException nfe) {

                        // add this line as header
                        if (lines.countLines() <= 0) {
                            lines.addToHeader(lineAsRead);
                        }

                        // skip line with non-numereous values
                        line = br.readLine();
                        continue newLine;
                    }
                    // prepare new iteration
                    line = line.substring(
                            line.indexOf(String.valueOf(separator)) + 1);
                    if (line.trim().equals("")) {
                        break;
                    }

                    int nextIndex = line.indexOf(String.valueOf(separator));
                    if (nextIndex < 0) {
                        values.add(Double.valueOf(line));
                        break;
                    }
                    value = line.substring(0, nextIndex);

                }

                lines.addSchedule(values);
                line = br.readLine();
            }

        } catch (IOException ex) {
            // return analyseScheduleCSV(filename, ++skipLines);
        	lines.setFileFormatError(true);
        } catch (NumberFormatException nfe) {
            // return analyseScheduleCSV(filename, ++skipLines);
        	lines.setFileFormatError(true);
        } catch (Exception e) {
        	e.printStackTrace();
        	lines.setFileFormatError(true);
        }
        return lines;
    }

    public static void main(String[] args) {
        System.out.println(quote);
        CSVUtil.analyseScheduleCSV("target/fc.csv");
    }
}
