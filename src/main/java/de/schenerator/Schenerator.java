package de.schenerator;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Starting class of the Schedule Generator Application
 * 
 * @author sBalduin
 *
 */
public class Schenerator extends Application {

    private static ResourceBundle bundle = ResourceBundle
            .getBundle("config/string");

    private static String path;

    private static Stage primaryStage;

    private static Random random;

    @Override
    public void start(Stage primaryStage) throws Exception {
        configurePath();
        random = new Random(System.currentTimeMillis());

        Schenerator.primaryStage = primaryStage;

        primaryStage.setTitle(bundle.getString("root.title") + " " + path);

        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        double monitorWidth = rectangle2D.getWidth();
        double monitorHeight = rectangle2D.getHeight();

        AnchorPane root = (AnchorPane) new FXMLLoader(
                getClass().getResource("/fxml/Root.fxml")).load();

        Scene scene = new Scene(root, monitorWidth / 2, monitorHeight / 2);
        root.prefWidthProperty().bind(scene.heightProperty());
        root.prefHeightProperty().bind(scene.heightProperty());

        scene.getStylesheets().add("/style/style.css");
        primaryStage.setOnCloseRequest(event -> System.exit(0));

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private static void configurePath() throws URISyntaxException {
        path = Paths
                .get(Schenerator.class.getProtectionDomain().getCodeSource()
                        .getLocation().toURI())
                .getParent().toString() + File.separator;
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }

    public static String getPath() {
        return path;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static Random getRandom() {
        return random;
    }

    public static void main(String[] args) {
        launch(args);

    }
}
