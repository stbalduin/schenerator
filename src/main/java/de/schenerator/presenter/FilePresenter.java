package de.schenerator.presenter;

import java.io.File;

import de.schenerator.Schenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FilePresenter extends Presenter {

    @FXML
    private Label importScheduleOption;

    @FXML
    private Label exportScheduleOption;

    @FXML
    private Label undoOption;

    @FXML
    private Label redoOption;

    @FXML
    private void initialize() {
        // Set localized strings
        importScheduleOption.setText(Schenerator.getString("menu.import"));
        exportScheduleOption.setText(Schenerator.getString("menu.export"));
        undoOption.setText(Schenerator.getString("menu.undo"));
        redoOption.setText(Schenerator.getString("menu.redo"));

    }

    @FXML
    protected void onImportScheduleOption(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Schenerator.getString("menu.import.dialog.title"));
        File initDir = new File(Schenerator.getPath());
        if (initDir.exists()) {
            fileChooser.setInitialDirectory(initDir);
        }
        File file = fileChooser.showOpenDialog(Schenerator.getStage());

        if (file == null) {
            return;
        }

        launchImport(file);

    }

    @FXML
    protected void onExportScheduleOption(MouseEvent event) {
        launchExport();
    }

    @FXML
    protected void onUndoOption(MouseEvent event) {
        commander.undo(1);
    }

    @FXML
    protected void onRedoOption(MouseEvent event) {
        commander.redo(1);
    }

    public void launchImport(File file) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ImportConfig.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            ImportPresenter presenter = (ImportPresenter) loader
                    .getController();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 640, 480);
            scene.getStylesheets().add("/style/style.css");

            stage.setScene(scene);

            presenter.setStage(stage);
            presenter.setCommander(commander);
            presenter.setFile(file);

            // Fill stage with content
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void launchExport() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/ExportConfig.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            ExportPresenter presenter = (ExportPresenter) loader
                    .getController();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 640, 480);
            scene.getStylesheets().add("/style/style.css");

            stage.setScene(scene);

            presenter.setStage(stage);
            presenter.setCommander(commander);

            // Fill stage with content
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
