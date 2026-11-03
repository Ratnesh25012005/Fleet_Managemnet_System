package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.StatusContainer;
import com.example.car_rental_sys.model.Role;
import com.example.car_rental_sys.model.User;
import com.example.car_rental_sys.service.ServiceFactory;
import com.example.car_rental_sys.ui_components.SwitchButton;
import com.example.car_rental_sys.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;

/**
 * Controller for Application Preferences and Settings (SettingPage.fxml).
 * Uses authentic SwitchButton iOS-style toggle components.
 */
public class SettingPageController extends Controller {

    @FXML
    private Pane settingPane;

    @FXML
    private Pane switchPaneOne;

    @FXML
    private Pane switchPaneTwo;

    @FXML
    private Pane switchPaneThree;

    @FXML
    public void initialize() {
        initTheme();

        if (switchPaneOne != null) {
            switchPaneOne.getChildren().setAll(new SwitchButton(true));
        }
        if (switchPaneTwo != null) {
            switchPaneTwo.getChildren().setAll(new SwitchButton(true));
        }
        if (switchPaneThree != null) {
            switchPaneThree.getChildren().setAll(new SwitchButton(false));
        }
    }

    private void initTheme() {
        User currentUser = ServiceFactory.getInstance().getAuthService().getCurrentUser();
        if (currentUser != null && currentUser.getRole() == Role.CUSTOMER && settingPane != null) {
            URL darkCss = getClass().getResource("/com/example/car_rental_sys/style/settingComponentDark.css");
            if (darkCss != null) {
                settingPane.getStylesheets().add(darkCss.toExternalForm());
            }
        }
    }

    @FXML
    private void backToDashboardClicked(MouseEvent event) {
        User currentUser = ServiceFactory.getInstance().getAuthService().getCurrentUser();
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            SceneManager.switchScene("adminServicePage.fxml");
        } else if (currentUser != null && currentUser.getRole() == Role.DRIVER) {
            SceneManager.switchScene("driverServicePage.fxml");
        } else {
            SceneManager.switchScene("customerServicePage.fxml");
        }
    }
}
