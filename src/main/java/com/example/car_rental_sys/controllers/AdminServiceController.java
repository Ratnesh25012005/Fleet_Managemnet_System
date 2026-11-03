package com.example.car_rental_sys.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the Main Administrator Service Shell & Dashboard Portal.
 */
public class AdminServiceController extends Controller {

    private static final Logger LOGGER = Logger.getLogger(AdminServiceController.class.getName());

    public static AdminServiceController instance;

    @FXML
    public AnchorPane mainPane;

    @FXML
    private Pane centerContainer;

    @FXML
    private Pane rightContainer;

    @FXML
    private Pane hugeContainer;

    @FXML
    private Pane middlePanel;

    @FXML
    private Pane settingMainPanel;

    @FXML
    private Pane dashboardContainer;

    @FXML
    private Pane toRightPane;

    @FXML
    private ImageView toRightBtn;

    @FXML
    public void initialize() {
        instance = this;
        showDashboardPage();

        if (toRightPane != null) {
            toRightPane.setOnMouseClicked(event -> toggleContainers());
        }
    }

    private void toggleContainers() {
        if (dashboardContainer != null && hugeContainer != null) {
            boolean showDash = !dashboardContainer.isVisible();
            dashboardContainer.setVisible(showDash);
            hugeContainer.setVisible(!showDash);
        }
    }

    public void showDashboardPage() {
        if (dashboardContainer != null) dashboardContainer.setVisible(true);
        if (hugeContainer != null) hugeContainer.setVisible(false);
        if (centerContainer != null) centerContainer.setVisible(false);
        if (rightContainer != null) rightContainer.setVisible(false);
    }

    public void showVehiclePage() {
        if (dashboardContainer != null) dashboardContainer.setVisible(false);
        if (centerContainer != null) centerContainer.setVisible(false);
        if (rightContainer != null) rightContainer.setVisible(false);
        if (hugeContainer != null) {
            hugeContainer.setVisible(true);
            loadSubView(hugeContainer, "AdminVehiclePage.fxml");
        }
    }

    public void showOrderPage() {
        if (dashboardContainer != null) dashboardContainer.setVisible(false);
        if (hugeContainer != null) hugeContainer.setVisible(false);
        if (centerContainer != null) {
            centerContainer.setVisible(true);
            loadSubView(centerContainer, "showOrderComponent.fxml");
        }
        if (rightContainer != null) {
            rightContainer.setVisible(true);
            loadSubView(rightContainer, "OrderDetailsComponent.fxml");
        }
    }

    public void showOrderDetails() {
        if (rightContainer != null) {
            rightContainer.setVisible(true);
            loadSubView(rightContainer, "OrderDetailsComponent.fxml");
        }
    }

    public void closeTrackOrder() {
        showOrderDetails();
    }

    public void showTrackOrder() {
        if (rightContainer != null) {
            rightContainer.setVisible(true);
            loadSubView(rightContainer, "TrackOrderComponent.fxml");
        }
    }

    private void initHugeSettingsContainer() {
        if (dashboardContainer != null) dashboardContainer.setVisible(false);
        if (centerContainer != null) centerContainer.setVisible(false);
        if (rightContainer != null) rightContainer.setVisible(false);
        if (hugeContainer != null) {
            hugeContainer.setVisible(true);
            hugeContainer.getChildren().clear();
            if (middlePanel != null && settingMainPanel != null) {
                hugeContainer.getChildren().addAll(middlePanel, settingMainPanel);
            }
        }
    }

    public void showSettingPage() {
        initHugeSettingsContainer();
        if (middlePanel != null) loadSubView(middlePanel, "MiddleBar.fxml");
        if (settingMainPanel != null) loadSubView(settingMainPanel, "SettingPage.fxml");
    }

    public void showAccSecurity() {
        initHugeSettingsContainer();
        if (middlePanel != null) loadSubView(middlePanel, "MiddleBar.fxml");
        if (settingMainPanel != null) loadSubView(settingMainPanel, "AccountSecurityPage.fxml");
    }

    public void showPasswordPage() {
        initHugeSettingsContainer();
        if (middlePanel != null) loadSubView(middlePanel, "MiddleBar.fxml");
        if (settingMainPanel != null) loadSubView(settingMainPanel, "PasswordPage.fxml");
    }

    public void showProfilePage() {
        initHugeSettingsContainer();
        if (middlePanel != null) loadSubView(middlePanel, "InfoMiddleBar.fxml");
        if (settingMainPanel != null) loadSubView(settingMainPanel, "PersonalInfoPage.fxml");
    }

    private void loadSubView(Pane targetPane, String fxmlName) {
        if (targetPane == null) return;
        try {
            URL resource = getClass().getResource("/com/example/car_rental_sys/fxml/" + fxmlName);
            if (resource != null) {
                Parent root = FXMLLoader.load(resource);
                targetPane.getChildren().setAll(root);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load sub-view: " + fxmlName, e);
        }
    }
}
