package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.util.SceneManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.net.URL;

/**
 * Controller for Payment Success Confirmation Screen.
 * Plays car animation video via WebView, then auto-navigates to orders.
 * JSObject / window.invoke bridge is replaced with a Java-side PauseTransition
 * because jdk.jsobject was removed in JDK 26.
 */
public class PaySuccessPageController extends Controller {

    @FXML public Pane mainPane;
    @FXML private WebView webview;
    @FXML private Pane navPane;

    private PauseTransition autoNavTimer;

    @FXML
    public void initialize() {
        loadPaySuccessAnimation();
    }

    private void loadPaySuccessAnimation() {
        if (webview == null) return;

        WebEngine engine = webview.getEngine();

        // Load the pay-success HTML (plays video2.mp4 automatically)
        URL resource = getClass().getResource(
                "/com/example/car_rental_sys/html/paySuccess/index.html");

        if (resource != null) {
            engine.load(resource.toExternalForm());
        } else {
            // fallback: just navigate after 3s if resource is missing
            scheduleAutoNav(3);
            return;
        }

        // After video ends (~16s) the HTML shows a "Check Payment" button.
        // The button calls window.invoke.world() which we can't bridge in JDK 26.
        // Instead: after 20s auto-navigate to orders from Java side.
        autoNavTimer = new PauseTransition(Duration.seconds(20));
        autoNavTimer.setOnFinished(e -> goToOrders());
        autoNavTimer.play();

        // Also let user press SPACE / ENTER to skip immediately
        Platform.runLater(() -> {
            if (mainPane != null) {
                mainPane.setFocusTraversable(true);
                mainPane.requestFocus();
                mainPane.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.SPACE
                            || event.getCode() == KeyCode.ENTER) {
                        skipToOrders(engine);
                    }
                });
            }
        });

        // Poll every 500 ms: if the HTML has displayed #container (video done),
        // navigate after a brief pause so user sees the success overlay.
        javafx.animation.Timeline poll = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.millis(500), ev -> {
                    try {
                        Object display = engine.executeScript(
                                "document.getElementById('container') !== null "
                                + "&& document.getElementById('container').style.display !== 'none' "
                                + "&& document.getElementById('container').style.display !== '' ? 'visible' : 'hidden'");
                        if ("visible".equals(display)) {
                            // Container is visible — wait 4s then navigate
                            if (autoNavTimer != null) {
                                autoNavTimer.stop();
                            }
                            scheduleAutoNav(4);
                        }
                    } catch (Exception ignored) { }
                })
        );
        poll.setCycleCount(60); // poll for up to 30 seconds
        poll.play();
    }

    private void scheduleAutoNav(int seconds) {
        PauseTransition t = new PauseTransition(Duration.seconds(seconds));
        t.setOnFinished(e -> goToOrders());
        t.play();
    }

    private void skipToOrders(WebEngine engine) {
        if (autoNavTimer != null) autoNavTimer.stop();
        try {
            engine.executeScript("stopVideo();");
        } catch (Exception ignored) { }
        goToOrders();
    }

    private void goToOrders() {
        Platform.runLater(() -> SceneManager.switchScene("showOrderComponent.fxml"));
    }
}
