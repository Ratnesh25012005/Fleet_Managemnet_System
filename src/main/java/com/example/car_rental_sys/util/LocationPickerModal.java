package com.example.car_rental_sys.util;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Interactive Map Modal for choosing vehicle pickup & drop-off locations.
 * Matches the original 800x600 dark map popup with search bar and hub pins.
 */
public class LocationPickerModal {

    private final Stage modalStage;
    private double xOffset = 0;
    private double yOffset = 0;

    public LocationPickerModal(Stage ownerStage, Consumer<String> onLocationSelected) {
        modalStage = new Stage();
        modalStage.initStyle(StageStyle.UNDECORATED);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        if (ownerStage != null) {
            modalStage.initOwner(ownerStage);
        }

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e2430; -fx-background-radius: 12px; -fx-border-color: #3c4858; -fx-border-radius: 12px; -fx-border-width: 1.5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.65), 30, 0, 0, 10);");

        // Top Header Bar
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(45);
        header.setStyle("-fx-background-color: #141820; -fx-background-radius: 12px 12px 0 0; -fx-padding: 0 16px 0 18px;");

        Label title = new Label("Choose Rental Location (Fleet Hub Map)");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #e5e6eb;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ImageView closeIcon = new ImageView();
        try (InputStream is = getClass().getResourceAsStream("/com/example/car_rental_sys/image/UI/closeIcon.png")) {
            if (is != null) closeIcon.setImage(new Image(is));
        } catch (Exception ignored) {}
        closeIcon.setFitWidth(18);
        closeIcon.setFitHeight(18);
        closeIcon.setCursor(javafx.scene.Cursor.HAND);
        closeIcon.setOnMouseClicked(e -> modalStage.close());

        header.getChildren().addAll(title, spacer, closeIcon);

        // Window Dragging from Header
        header.setOnMousePressed(event -> {
            xOffset = modalStage.getX() - event.getScreenX();
            yOffset = modalStage.getY() - event.getScreenY();
        });
        header.setOnMouseDragged(event -> {
            modalStage.setX(event.getScreenX() + xOffset);
            modalStage.setY(event.getScreenY() + yOffset);
        });

        // WebView with Interactive Leaflet / CartoDB Map
        WebView webView = new WebView();
        webView.setPrefSize(800, 555);
        WebEngine engine = webView.getEngine();
        EnvConfig.injectGoogleMapsKey(engine);

        // Listen for JavaScript selection via alert
        engine.setOnAlert(event -> {
            String selectedLocation = event.getData();
            if (selectedLocation != null && !selectedLocation.isBlank()) {
                if (onLocationSelected != null) {
                    onLocationSelected.accept(selectedLocation);
                }
                modalStage.close();
            }
        });

        URL mapUrl = getClass().getResource("/com/example/car_rental_sys/html/googleMap.html");
        if (mapUrl != null) {
            engine.load(mapUrl.toExternalForm());
        }

        root.setTop(header);
        root.setCenter(webView);

        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.TRANSPARENT);
        modalStage.setScene(scene);

        // Center relative to primary window
        if (ownerStage != null && ownerStage.isShowing()) {
            modalStage.setX(ownerStage.getX() + Math.max(0, (ownerStage.getWidth() - 800) / 2));
            modalStage.setY(ownerStage.getY() + Math.max(0, (ownerStage.getHeight() - 600) / 2));
        }
    }

    public void show() {
        modalStage.showAndWait();
    }
}
