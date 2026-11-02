package com.example.car_rental_sys.ui_components;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Authentic animated iOS-style toggle switch for SettingPage.fxml.
 */
public class SwitchButton extends Pane {

    private Pane circle;
    private boolean isOn = true;

    public SwitchButton() {
        initStyle();
        initComponent();
        initEvent();
    }

    public SwitchButton(boolean initialState) {
        this.isOn = initialState;
        initStyle();
        initComponent();
        initEvent();
    }

    private void initStyle() {
        this.setPrefSize(30, 16);
        if (isOn) {
            this.setStyle("-fx-background-radius: 20; -fx-background-color: #4080ff; -fx-cursor: hand;");
        } else {
            this.setStyle("-fx-background-radius: 20; -fx-background-color: #D9D9D9; -fx-cursor: hand;");
        }
    }

    private void initComponent() {
        circle = new Pane();
        circle.setPrefSize(8, 8);
        circle.setStyle("-fx-background-radius: 4; -fx-background-color: #ffffff;");
        circle.setLayoutX(isOn ? 18 : 4);
        circle.setLayoutY(4);

        this.getChildren().add(circle);
    }

    private void initEvent() {
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (!isOn) {
                isOn = true;
                circle.setLayoutX(18);
                this.setStyle("-fx-background-radius: 20; -fx-background-color: #4080ff; -fx-cursor: hand;");
            } else {
                isOn = false;
                circle.setLayoutX(4);
                this.setStyle("-fx-background-radius: 20; -fx-background-color: #D9D9D9; -fx-cursor: hand;");
            }
        });
    }

    public boolean isSwitchedOn() {
        return isOn;
    }
}

