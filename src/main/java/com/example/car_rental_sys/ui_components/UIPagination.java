package com.example.car_rental_sys.ui_components;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Authentic pagination bar component for tables and order lists.
 */
public class UIPagination extends Pane {

    private int totalNumber = 10;
    private int perPage = 5;
    private int currentPageNumber = 1;
    private int totalPageNumber = 2;

    public UIPagination() {
        initUI();
    }

    public UIPagination(int total, int perPage) {
        this.totalNumber = total;
        this.perPage = perPage;
        this.totalPageNumber = Math.max(1, (int) Math.ceil((double) total / perPage));
        initUI();
    }

    private void initUI() {
        HBox box = new HBox(6);
        box.setAlignment(Pos.CENTER);

        UIPaginationCard left = new UIPaginationCard(-3);
        UIPaginationCard p1 = new UIPaginationCard(1);
        p1.setStyle("-fx-background-color: #165dff; -fx-text-fill: white; -fx-background-radius: 4px;");

        box.getChildren().addAll(left, p1);

        if (totalPageNumber > 1) {
            UIPaginationCard p2 = new UIPaginationCard(2);
            box.getChildren().add(p2);
        }

        UIPaginationCard right = new UIPaginationCard(-4);
        box.getChildren().add(right);

        this.getChildren().add(box);
    }
}
