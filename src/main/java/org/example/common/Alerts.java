package org.example.common;

import javafx.scene.control.Alert;

public class Alerts {
    public static void showError(String windowTitle, String errorHeading, String errorDescription) {
        Alert errorPopup = new Alert(Alert.AlertType.ERROR);

        errorPopup.setTitle(windowTitle);
        errorPopup.setHeaderText(errorHeading);
        errorPopup.setContentText(errorDescription);

        errorPopup.showAndWait();
    }
}
