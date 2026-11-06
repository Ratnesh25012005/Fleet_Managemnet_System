package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.util.AlertHelper;
import com.example.car_rental_sys.util.EnvConfig;
import com.example.car_rental_sys.util.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;

/**
 * Controller for the Driver Mission Control & Active Trip Operations.
 */
public class DriverMainPageController extends Controller {

    public static DriverMainPageController driverMainPageInstance;

    @FXML
    public Pane mainPane;

    @FXML
    private Pane optionPane;

    @FXML
    private Pane middlePanel;

    @FXML
    private Pane settingMainPanel;

    @FXML
    private Pane workplacePane;

    @FXML
    private WebView webview;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane processTipPane;

    @FXML
    private Pane processImagePane;

    @FXML
    private Label processTipLabel;

    @FXML
    private Pane sidePane;

    @FXML
    private ImageView renterAvatar;

    @FXML
    private Label renterName;

    @FXML
    private Label renterPost;

    @FXML
    private Pane messagePane;

    @FXML
    private Pane phonePane;

    @FXML
    private Pane closeSideBarPane;

    @FXML
    private ImageView carImageView;

    @FXML
    private Label seatsLabel;

    @FXML
    private Label powerLabel;

    @FXML
    private Label speedLabel;

    @FXML
    private Label startLabel;

    @FXML
    private Label destinationLabel;

    @FXML
    private Label carNumberLabel;

    @FXML
    public void initialize() {
        driverMainPageInstance = this;
        showWorkplace();
        initWebview();
        initProcessTipEvent();
        initInteractions();
        populateActiveTrip();
    }

    private void initWebview() {
        if (webview == null) return;
        WebEngine engine = webview.getEngine();
        engine.setJavaScriptEnabled(true);
        EnvConfig.injectGoogleMapsKey(engine);
        URL url = getClass().getResource("/com/example/car_rental_sys/html/directions.html");
        if (url != null) {
            engine.load(url.toExternalForm());
        }
    }

    private void initProcessTipEvent() {
        if (processImagePane != null) {
            processImagePane.setStyle(processImagePane.getStyle() + "; -fx-cursor: hand;");
            processImagePane.setOnMouseClicked(event -> {
                SceneManager.switchScene("drivingModePage.fxml");
            });
        }
    }

    private void initInteractions() {
        if (messagePane != null) {
            messagePane.setOnMouseClicked(event -> SceneManager.switchScene("messagePage.fxml"));
        }
        if (phonePane != null) {
            phonePane.setOnMouseClicked(this::phoneClick);
        }
        if (closeSideBarPane != null) {
            closeSideBarPane.setOnMouseClicked(event -> SceneManager.switchScene("mainPage.fxml"));
        }
    }

    public void showWorkplace() {
        if (optionPane != null) optionPane.setVisible(false);
        if (workplacePane != null) workplacePane.setVisible(true);
    }

    public void showSettingsView(String fxmlName) {
        if (workplacePane != null) workplacePane.setVisible(false);
        if (optionPane != null) {
            optionPane.setVisible(true);
            if (settingMainPanel != null) {
                loadSubView(settingMainPanel, fxmlName);
            }
        }
    }

    public void showSettingPage() {
        showSettingsView("SettingPage.fxml");
        loadSubView(middlePanel, "MiddleBar.fxml");
    }

    public void showAccSecurity() {
        showSettingsView("AccountSecurityPage.fxml");
        loadSubView(middlePanel, "MiddleBar.fxml");
    }

    public void showPasswordPage() {
        showSettingsView("PasswordPage.fxml");
        loadSubView(middlePanel, "MiddleBar.fxml");
    }

    public void showProfilePage() {
        showSettingsView("PersonalInfoPage.fxml");
        loadSubView(middlePanel, "InfoMiddleBar.fxml");
    }

    private void loadSubView(Pane targetPane, String fxmlName) {
        if (targetPane == null) return;
        try {
            java.net.URL resource = getClass().getResource("/com/example/car_rental_sys/fxml/" + fxmlName);
            if (resource != null) {
                javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(resource);
                targetPane.getChildren().setAll(root);
            }
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(DriverMainPageController.class.getName())
                    .log(java.util.logging.Level.SEVERE, "Failed to load sub-view: " + fxmlName, e);
        }
    }

    private void populateActiveTrip() {
        if (renterName != null) renterName.setText("Alexander Wright");
        if (renterPost != null) renterPost.setText("Gold VIP Client");
        if (carNumberLabel != null) carNumberLabel.setText("AP 39 FM 8899");
        if (startLabel != null) startLabel.setText("SRM University-AP, Amaravati");
        if (destinationLabel != null) destinationLabel.setText("Vijayawada Junction (BZA Hub)");
        if (seatsLabel != null) seatsLabel.setText("4 Seats");
        if (powerLabel != null) powerLabel.setText("6.2 L V8");
        if (speedLabel != null) speedLabel.setText("330 km/h");
        if (processTipLabel != null) processTipLabel.setText("Trip in progress: Heading to Pickup");
    }

    @FXML
    private void phoneClick(MouseEvent event) {
        AlertHelper.showInfo("Client Direct Contact", "Customer Phone: +91 98765 00003\nClient: Alexander Wright\nConnecting VoIP / WhatsApp dispatch relay...");
    }

    @FXML
    private void closeSideBarPaneClick(MouseEvent event) {
        if (sidePane != null) {
            sidePane.setVisible(!sidePane.isVisible());
        }
    }
}
