package de.schenerator.presenter;

import javafx.stage.Stage;

public abstract class WindowPresenter extends Presenter {

    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
