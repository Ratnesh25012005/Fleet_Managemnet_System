package com.example.car_rental_sys.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;

/**
 * Controller for Admin Analytics Dashboard (dashboard.fxml).
 * Restored with authentic animated web charts and financial metrics loaded via JavaFX WebView.
 */
public class dashboardController extends Controller {

    public static dashboardController instance;

    @FXML
    private Pane browserPane;

    @FXML
    public void initialize() {
        instance = this;
        loadAuthenticDashboard();
    }

    private void loadAuthenticDashboard() {
        if (browserPane == null) return;

        WebView webView = new WebView();
        webView.setPrefSize(1000, 750);
        WebEngine engine = webView.getEngine();
        engine.setJavaScriptEnabled(true);

        URL url = getClass().getResource("/com/example/car_rental_sys/html/dashboard/index.html");
        if (url != null) {
            engine.load(url.toExternalForm());
        }

        browserPane.getChildren().setAll(webView);
    }
}
