package com.example.car_rental_sys.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.BiConsumer;

/**
 * Interactive Schedule Modal for selecting rental Pick-up & Return dates and times.
 */
public class DatePickerModal {

    private final Stage modalStage;
    private double xOffset = 0;
    private double yOffset = 0;

    private final DatePicker pickUpDatePicker = new DatePicker(LocalDate.now());
    private final ComboBox<String> pickUpTimeBox = new ComboBox<>();

    private final DatePicker returnDatePicker = new DatePicker(LocalDate.now().plusDays(7));
    private final ComboBox<String> returnTimeBox = new ComboBox<>();

    private final Label durationLabel = new Label("Rental Duration: 7 Days");

    public DatePickerModal(Stage ownerStage, BiConsumer<String, String> onScheduleConfirmed) {
        modalStage = new Stage();
        modalStage.initStyle(StageStyle.UNDECORATED);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        if (ownerStage != null) {
            modalStage.initOwner(ownerStage);
        }

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e2430; -fx-background-radius: 12px; -fx-border-color: #3c4858; -fx-border-radius: 12px; -fx-border-width: 1.5px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.65), 30, 0, 0, 10);");

        // Top Header
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(45);
        header.setStyle("-fx-background-color: #141820; -fx-background-radius: 12px 12px 0 0; -fx-padding: 0 16px 0 18px;");

        Label title = new Label("Select Rental Period (Pick-up & Return)");
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

        header.setOnMousePressed(event -> {
            xOffset = modalStage.getX() - event.getScreenX();
            yOffset = modalStage.getY() - event.getScreenY();
        });
        header.setOnMouseDragged(event -> {
            modalStage.setX(event.getScreenX() + xOffset);
            modalStage.setY(event.getScreenY() + yOffset);
        });

        // Body Content
        HBox columns = new HBox(30);
        columns.setAlignment(Pos.CENTER);
        columns.setPadding(new Insets(30, 40, 20, 40));

        // Times list
        String[] times = {"08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM", "06:00 PM", "07:00 PM", "08:00 PM"};
        pickUpTimeBox.getItems().addAll(times);
        pickUpTimeBox.setValue("10:00 AM");
        pickUpTimeBox.setMaxWidth(Double.MAX_VALUE);

        returnTimeBox.getItems().addAll(times);
        returnTimeBox.setValue("10:00 AM");
        returnTimeBox.setMaxWidth(Double.MAX_VALUE);

        pickUpDatePicker.setMaxWidth(Double.MAX_VALUE);
        returnDatePicker.setMaxWidth(Double.MAX_VALUE);

        // Update duration listener
        pickUpDatePicker.valueProperty().addListener((obs, oldV, newV) -> updateDuration());
        returnDatePicker.valueProperty().addListener((obs, oldV, newV) -> updateDuration());

        // Left Column (Pick-up)
        VBox leftCol = new VBox(12);
        leftCol.setPrefWidth(240);
        Label lblPickHeader = new Label("📅 Pick-up Schedule");
        lblPickHeader.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #00d26a;");

        Label lblPickDate = new Label("Select Date:");
        lblPickDate.setStyle("-fx-font-size: 12px; -fx-text-fill: #a4b0be;");

        Label lblPickTime = new Label("Select Time:");
        lblPickTime.setStyle("-fx-font-size: 12px; -fx-text-fill: #a4b0be;");

        leftCol.getChildren().addAll(lblPickHeader, lblPickDate, pickUpDatePicker, lblPickTime, pickUpTimeBox);

        // Right Column (Return)
        VBox rightCol = new VBox(12);
        rightCol.setPrefWidth(240);
        Label lblRetHeader = new Label("🏁 Return Schedule");
        lblRetHeader.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #165dff;");

        Label lblRetDate = new Label("Select Date:");
        lblRetDate.setStyle("-fx-font-size: 12px; -fx-text-fill: #a4b0be;");

        Label lblRetTime = new Label("Select Time:");
        lblRetTime.setStyle("-fx-font-size: 12px; -fx-text-fill: #a4b0be;");

        rightCol.getChildren().addAll(lblRetHeader, lblRetDate, returnDatePicker, lblRetTime, returnTimeBox);

        columns.getChildren().addAll(leftCol, rightCol);

        // Bottom Action Bar
        VBox bottomBox = new VBox(14);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 40, 25, 40));

        durationLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        Button btnConfirm = new Button("Confirm Rental Schedule");
        btnConfirm.setPrefWidth(300);
        btnConfirm.setPrefHeight(42);
        btnConfirm.setStyle("-fx-background-color: #00b42a; -fx-background-radius: 8px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");

        btnConfirm.setOnAction(e -> {
            LocalDate pDate = pickUpDatePicker.getValue() != null ? pickUpDatePicker.getValue() : LocalDate.now();
            LocalDate rDate = returnDatePicker.getValue() != null ? returnDatePicker.getValue() : pDate.plusDays(1);
            if (rDate.isBefore(pDate)) {
                rDate = pDate.plusDays(1);
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd");
            String pStr = pDate.format(fmt) + ", " + pickUpTimeBox.getValue();
            String rStr = rDate.format(fmt) + ", " + returnTimeBox.getValue();

            if (onScheduleConfirmed != null) {
                onScheduleConfirmed.accept(pStr, rStr);
            }
            modalStage.close();
        });

        bottomBox.getChildren().addAll(durationLabel, btnConfirm);

        root.setTop(header);
        root.setCenter(columns);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 600, 380);
        scene.setFill(Color.TRANSPARENT);
        modalStage.setScene(scene);

        if (ownerStage != null && ownerStage.isShowing()) {
            modalStage.setX(ownerStage.getX() + Math.max(0, (ownerStage.getWidth() - 600) / 2));
            modalStage.setY(ownerStage.getY() + Math.max(0, (ownerStage.getHeight() - 380) / 2));
        }
    }

    private void updateDuration() {
        LocalDate p = pickUpDatePicker.getValue();
        LocalDate r = returnDatePicker.getValue();
        if (p != null && r != null) {
            long days = ChronoUnit.DAYS.between(p, r);
            if (days < 0) days = 0;
            durationLabel.setText("Rental Duration: " + days + " Day" + (days == 1 ? "" : "s"));
        }
    }

    public void show() {
        modalStage.showAndWait();
    }
}
