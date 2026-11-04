package com.example.car_rental_sys.ui_components;

import com.example.car_rental_sys.ToolsLib.ImageTools;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Authentic billing row component for customer BillingComponent.fxml.
 */
public class UICusBillRow extends Pane {

    private int status = 1;
    private int amount = 150;
    private String time = "14:30";
    private String date = "2026-11-01";
    private String type = "rental";

    private Label billStatus, billAmount, billTime, billDate, billType;

    public UICusBillRow() {
        initStyle();
        initComponents();
    }

    public UICusBillRow(String type, String date, String time, int amount, int status) {
        this.type = type != null ? type : "rental";
        this.date = date != null ? date : "2026-11-01";
        this.time = time != null ? time : "14:30";
        this.amount = amount;
        this.status = status;
        initStyle();
        initComponents();
    }

    private void initStyle() {
        this.setPrefHeight(35);
        this.setPrefWidth(470);
        this.setStyle("-fx-background-color: #202020; -fx-background-radius: 4px;");
    }

    private void initComponents() {
        Pane imgPane = new Pane();
        imgPane.setPrefSize(24, 24);
        imgPane.setLayoutX(15);
        imgPane.setLayoutY(6);
        imgPane.setStyle("-fx-border-radius: 4; -fx-border-color: #555555;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        imageView.setLayoutX(3);
        imageView.setLayoutY(3);

        String arrowPath = Objects.equals(type, "rental")
                ? "/com/example/car_rental_sys/image/UI/downArrow.png"
                : "/com/example/car_rental_sys/image/UI/upArrow.png";
        Image arrowImg = ImageTools.getImageObjFromPath(arrowPath);
        if (arrowImg != null) imageView.setImage(arrowImg);

        imgPane.getChildren().add(imageView);

        String typeText = Objects.equals(type, "rental") ? "Rental Fee" : "Top-up wallet";
        String amountText = Objects.equals(type, "rental") ? "-₹" + amount : "+₹" + amount;

        billType = new Label(typeText);
        billType.setLayoutX(52);
        billType.setLayoutY(10);
        billType.setStyle("-fx-font-size: 12px; -fx-text-fill: #2971cf; -fx-font-weight: bold;");

        billDate = new Label(this.date);
        billDate.setLayoutX(151);
        billDate.setLayoutY(10);
        billDate.setStyle("-fx-font-size: 12px; -fx-text-fill: GREY; -fx-font-weight: bold;");

        billTime = new Label(this.time);
        billTime.setLayoutX(248);
        billTime.setLayoutY(10);
        billTime.setStyle("-fx-font-size: 12px; -fx-text-fill: GREY; -fx-font-weight: bold;");

        billAmount = new Label(amountText);
        billAmount.setLayoutX(321);
        billAmount.setLayoutY(10);
        billAmount.setStyle("-fx-font-size: 12px; -fx-text-fill: #e0e0e0; -fx-font-weight: bold;");

        String textColor;
        String textStatus;
        if (status >= 1) {
            textColor = "#1db440";
            textStatus = "Completed";
        } else if (status == -1) {
            textColor = "#f54e4e";
            textStatus = "Canceled";
        } else {
            textColor = "#ff8d1f";
            textStatus = "Pending";
        }

        billStatus = new Label(textStatus);
        billStatus.setLayoutX(397);
        billStatus.setLayoutY(10);
        billStatus.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill:" + textColor + ";");

        this.getChildren().addAll(imgPane, billType, billDate, billTime, billAmount, billStatus);
    }
}

