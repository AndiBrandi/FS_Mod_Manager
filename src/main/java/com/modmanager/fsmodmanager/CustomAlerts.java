package com.modmanager.fsmodmanager;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

public class CustomAlerts {

    private static Alert alert = new Alert(Alert.AlertType.NONE);
    private static Stage stage;

    public static void errorAlert(String headerText, String contentText) {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.show();


    }

    public static void warningAlert(String headerText, String contentText) {
        alert.setAlertType(Alert.AlertType.WARNING);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
