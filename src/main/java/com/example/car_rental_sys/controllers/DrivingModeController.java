package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.util.AlertHelper;
import com.example.car_rental_sys.util.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Controller for the Fullscreen In-Cabin Driver Cockpit Navigation Mode.
 */
public class DrivingModeController extends Controller {

    @FXML
    public Pane mainPane;

    @FXML
    private ImageView userAvatar;

    @FXML
    private Pane sideIconLogo;

    @FXML
    private Pane sideIconHome;

    @FXML
    private Pane sideIconVolume;

    @FXML
    private Pane sideIconPhone;

    @FXML
    private Pane sideIconSetting;

    @FXML
    private Pane browserContainer;

    @FXML
    private WebView cockpitWebView;

    @FXML
    private Pane distanceDataPane;

    @FXML
    private Pane progressBarTrack;

    @FXML
    private Pane progressBarFill;

    @FXML
    private ImageView progressIndicator;

    @FXML
    private Label distanceLeftLabel;

    @FXML
    private Label timeLeftLabel;

    @FXML
    public void initialize() {
        if (distanceLeftLabel != null) distanceLeftLabel.setText("21.6 km left");
        if (timeLeftLabel != null) timeLeftLabel.setText("28 mins");
        updateProgressBar(0.0);
        initCockpitMap();
    }

    private void initCockpitMap() {
        if (cockpitWebView == null) return;

        // Clip WebView with smooth rounded corners matching cockpit panel
        Rectangle clip = new Rectangle(300, 490);
        clip.setArcWidth(24);
        clip.setArcHeight(24);
        cockpitWebView.setClip(clip);

        WebEngine engine = cockpitWebView.getEngine();
        engine.setJavaScriptEnabled(true);

        double startLat = 16.4632;
        double startLng = 80.5078;
        String currentLoc = "SRM University-AP, Amaravati";

        var cached = com.example.car_rental_sys.service.ServiceFactory.getInstance().getGeoLocationService().getCachedLocation();
        if (cached != null && cached.success() && cached.latitude() != 0 && cached.longitude() != 0) {
            currentLoc = cached.city() + ", " + cached.regionName();
            startLat = cached.latitude();
            startLng = cached.longitude();
        }

        final double destLat = 16.5175;
        final double destLng = 80.6200;
        final String destination = "Vijayawada Junction (BZA Hub)";

        // Telemetry bridge listener from in-map car drive events
        engine.setOnAlert(event -> {
            String data = event.getData();
            if (data == null) return;

            if (data.startsWith("STEP:")) {
                // Format: STEP:18.4 km:22:15
                String[] parts = data.split(":");
                if (parts.length >= 4) {
                    final String dStr = parts[1];
                    final String tStr = parts[2];
                    final String pctStr = parts[3];
                    Platform.runLater(() -> {
                        if (distanceLeftLabel != null) distanceLeftLabel.setText(dStr + " left");
                        if (timeLeftLabel != null) timeLeftLabel.setText(tStr + " mins");
                        try {
                            double pct = Double.parseDouble(pctStr) / 100.0;
                            updateProgressBar(pct);
                        } catch (Exception ignored) {}
                    });
                }
            } else if ("ARRIVED".equals(data)) {
                Platform.runLater(() -> {
                    if (distanceLeftLabel != null) distanceLeftLabel.setText("0 km left");
                    if (timeLeftLabel != null) timeLeftLabel.setText("Arrived");
                    updateProgressBar(1.0);
                    AlertHelper.showInfo("Destination Reached", "You have arrived at the passenger pickup point:\n" + destination);
                });
            }
        });

        loadCockpitUrl(engine, startLat, startLng, destLat, destLng, currentLoc, destination);

        // Fetch live GPS in background if not already cached
        if (cached == null) {
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    var geo = com.example.car_rental_sys.service.ServiceFactory.getInstance().getGeoLocationService().fetchCurrentLocation();
                    if (geo != null && geo.success() && geo.latitude() != 0 && geo.longitude() != 0) {
                        Platform.runLater(() -> {
                            loadCockpitUrl(engine, geo.latitude(), geo.longitude(), destLat, destLng, geo.city(), destination);
                        });
                    }
                } catch (Exception ignored) {}
            });
        }
    }

    private void loadCockpitUrl(WebEngine engine, double sLat, double sLng, double dLat, double dLng, String sName, String dName) {
        URL resource = getClass().getResource("/com/example/car_rental_sys/html/cockpitMap.html");
        if (resource != null) {
            String url = resource.toExternalForm() + String.format(
                    Locale.US,
                    "?startLat=%.4f&startLng=%.4f&destLat=%.4f&destLng=%.4f&startName=%s&destName=%s",
                    sLat, sLng, dLat, dLng,
                    URLEncoder.encode(sName, StandardCharsets.UTF_8),
                    URLEncoder.encode(dName, StandardCharsets.UTF_8)
            );
            engine.load(url);
        }
    }

    private void updateProgressBar(double ratio) {
        if (progressBarFill != null && progressBarTrack != null) {
            double maxWidth = progressBarTrack.getPrefWidth() > 0 ? progressBarTrack.getPrefWidth() : 222.0;
            double fillWidth = Math.max(12.0, Math.min(maxWidth, maxWidth * ratio));
            progressBarFill.setPrefWidth(fillWidth);
            if (progressIndicator != null) {
                progressIndicator.setLayoutX(fillWidth - 14.0);
            }
        }
    }

    @FXML
    private void userAvatarClicked(MouseEvent event) {
        SceneManager.switchScene("PersonalInfoPage.fxml");
    }

    @FXML
    private void backToMainPage(MouseEvent event) {
        SceneManager.switchScene("mainPage.fxml");
    }

    @FXML
    private void backToDriverMainPage(MouseEvent event) {
        SceneManager.switchScene("driverServicePage.fxml");
    }

    @FXML
    private void phoneIconClicked(MouseEvent event) {
        AlertHelper.showInfo("Fleet Dispatch", "Connecting to Fleet Operations Dispatch Center...");
    }

    @FXML
    private void backToSettingPage(MouseEvent event) {
        SceneManager.switchScene("SettingPage.fxml");
    }
}
