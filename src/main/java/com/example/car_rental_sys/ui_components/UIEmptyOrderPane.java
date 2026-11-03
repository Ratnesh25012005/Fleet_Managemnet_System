package com.example.car_rental_sys.ui_components;

import com.example.car_rental_sys.StatusContainer;
import com.example.car_rental_sys.model.Role;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Authentic empty state placeholder pane for order inspection.
 */
public class UIEmptyOrderPane extends Pane {

    private String text;
    private Label label;

    public UIEmptyOrderPane(String text) {
        this.text = text != null ? text : "Select an order to view full details";
        init();
        initTheme();
    }

    private void init() {
        this.setPrefSize(320, 670);
        this.setLayoutX(18);
        this.setLayoutY(28);

        label = new Label(text);
        label.setLayoutX(24);
        label.setLayoutY(304);
        label.setWrapText(true);
        label.setMaxWidth(272);

        this.getChildren().add(label);
    }

    private void initTheme() {
        boolean isAdmin = (StatusContainer.currentUser != null && StatusContainer.currentUser.getRole() == Role.ADMIN);
        if (isAdmin) {
            label.setStyle("-fx-text-fill: #4E5969; -fx-font-size: 15px; -fx-font-weight: bold;");
            this.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10px; -fx-border-color: #e5e6eb; -fx-border-radius: 10px;");
        } else {
            label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15px; -fx-font-weight: bold;");
            this.setStyle("-fx-background-color: #111111; -fx-background-radius: 10px; -fx-border-color: #222222; -fx-border-radius: 10px;");
        }
    }
}

