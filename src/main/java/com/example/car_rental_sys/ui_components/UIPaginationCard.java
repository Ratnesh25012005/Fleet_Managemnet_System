package com.example.car_rental_sys.ui_components;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

/**
 * Authentic pagination pill button component.
 */
public class UIPaginationCard extends Button {
    private final int cardHeight = 30;
    private final int cardWidth = 31;

    public UIPaginationCard(int number) {
        super();
        this.setPrefSize(cardWidth, cardHeight);
        this.setCursor(Cursor.HAND);
        initPageNumber(String.valueOf(number));
    }

    public void initPageNumber(String number) {
        String content;
        switch (number) {
            case "-1":
                content = "...";
                this.setStyle("-fx-background-color: transparent; -fx-text-fill: #86909c;");
                break;
            case "-2":
                content = "...";
                this.setStyle("-fx-background-color: transparent; -fx-text-fill: #86909c;");
                break;
            case "-3":
                content = "<";
                this.setStyle("-fx-background-color: #f2f3f5; -fx-text-fill: #4e5969; -fx-background-radius: 4px;");
                break;
            case "-4":
                content = ">";
                this.setStyle("-fx-background-color: #f2f3f5; -fx-text-fill: #4e5969; -fx-background-radius: 4px;");
                break;
            default:
                content = number;
                this.setStyle("-fx-background-color: #f2f3f5; -fx-text-fill: #4e5969; -fx-background-radius: 4px;");
                break;
        }
        this.setText(content);
    }
}

