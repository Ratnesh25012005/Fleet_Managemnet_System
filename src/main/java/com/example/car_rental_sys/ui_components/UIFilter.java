package com.example.car_rental_sys.ui_components;

import com.example.car_rental_sys.controllers.OrderListComponentController;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Authentic filter dropdown popup for order and billing lists.
 */
public class UIFilter extends Pane {

    private Button filCancel, filDone, filDateStart, filDateEnd;
    private RadioButton rbPriceRec, rbPriceDesc, rbPriceAsc;
    private static int selectedState = 1;

    public static UIFilter instance;

    public UIFilter() {
        instance = this;
        generateFilter();
    }

    public void generateFilter() {
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setPrefSize(240, 260);
        initStyle();
        initComponents();
        initEvent();
    }

    private void initStyle() {
        this.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-color: #D9D9D9; -fx-border-width: 1px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 15, 0, 0, 4);");
    }

    private void initComponents() {
        Label lblDate = new Label("Date");
        lblDate.setStyle("-fx-font-size: 14px; -fx-text-fill: #86909C; -fx-font-weight: bold;");
        lblDate.setLayoutX(11);
        lblDate.setLayoutY(22);

        Label lblTo = new Label("To");
        lblTo.setStyle("-fx-font-size: 12px; -fx-text-fill: #86909C; -fx-font-weight: bold;");
        lblTo.setLayoutX(103);
        lblTo.setLayoutY(50);

        Label lblPricing = new Label("Pricing");
        lblPricing.setStyle("-fx-font-size: 14px; -fx-text-fill: #86909C; -fx-font-weight: bold;");
        lblPricing.setLayoutX(11);
        lblPricing.setLayoutY(91);

        Label lblRecommend = new Label("Recommended");
        lblRecommend.setStyle("-fx-font-size: 11px; -fx-text-fill: #4E5969; -fx-font-weight: bold;");
        lblRecommend.setLayoutX(45);
        lblRecommend.setLayoutY(118);

        Label lblPriceAsc = new Label("Lowest Price");
        lblPriceAsc.setStyle("-fx-font-size: 11px; -fx-text-fill: #4E5969; -fx-font-weight: bold;");
        lblPriceAsc.setLayoutX(45);
        lblPriceAsc.setLayoutY(141);

        Label lblPriceDesc = new Label("Highest Price");
        lblPriceDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #4E5969; -fx-font-weight: bold;");
        lblPriceDesc.setLayoutX(45);
        lblPriceDesc.setLayoutY(167);

        final ToggleGroup group = new ToggleGroup();
        rbPriceRec = new RadioButton();
        rbPriceRec.setToggleGroup(group);
        rbPriceRec.setSelected(selectedState == 1);
        rbPriceRec.setLayoutX(13);
        rbPriceRec.setLayoutY(118);

        rbPriceDesc = new RadioButton();
        rbPriceDesc.setToggleGroup(group);
        rbPriceDesc.setSelected(selectedState == 2);
        rbPriceDesc.setLayoutX(13);
        rbPriceDesc.setLayoutY(141);

        rbPriceAsc = new RadioButton();
        rbPriceAsc.setToggleGroup(group);
        rbPriceAsc.setSelected(selectedState == 3);
        rbPriceAsc.setLayoutX(13);
        rbPriceAsc.setLayoutY(166);

        filCancel = new Button("Cancel");
        filCancel.setStyle("-fx-font-size: 12px; -fx-background-color: #E5E6EB; -fx-background-radius: 6px; -fx-text-fill: #4E5969; -fx-font-weight: bold; -fx-cursor: hand;");
        filCancel.setLayoutX(28);
        filCancel.setLayoutY(215);
        filCancel.setPrefSize(80, 25);

        filDone = new Button("Done");
        filDone.setStyle("-fx-font-size: 12px; -fx-background-color: #4080FF; -fx-background-radius: 6px; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-cursor: hand;");
        filDone.setLayoutX(128);
        filDone.setLayoutY(215);
        filDone.setPrefSize(80, 25);

        filDateStart = new Button("Nov 01, 2026");
        filDateStart.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 6px; -fx-border-width: 1px; -fx-border-color: #D9D9D9; -fx-border-radius: 6px; -fx-font-size: 11px; -fx-text-fill: #4E5969; -fx-font-weight: bold;");
        filDateStart.setLayoutX(11);
        filDateStart.setLayoutY(45);
        filDateStart.setPrefSize(90, 25);

        filDateEnd = new Button("Nov 08, 2026");
        filDateEnd.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 6px; -fx-border-width: 1px; -fx-border-color: #D9D9D9; -fx-border-radius: 6px; -fx-font-size: 11px; -fx-text-fill: #4E5969; -fx-font-weight: bold;");
        filDateEnd.setLayoutX(118);
        filDateEnd.setLayoutY(45);
        filDateEnd.setPrefSize(90, 25);

        this.getChildren().addAll(lblDate, lblTo, lblPricing, lblRecommend, lblPriceAsc, lblPriceDesc,
                rbPriceRec, rbPriceDesc, rbPriceAsc,
                filCancel, filDone, filDateStart, filDateEnd);
    }

    private void initEvent() {
        filCancel.setOnMouseClicked(event -> {
            if (OrderListComponentController.instance != null) {
                OrderListComponentController.instance.removeFilterPane();
            }
        });

        filDone.setOnMouseClicked(event -> {
            if (OrderListComponentController.instance != null) {
                OrderListComponentController.instance.removeFilterPane();
            }
        });

        rbPriceRec.setOnAction(e -> selectedState = 1);
        rbPriceDesc.setOnAction(e -> selectedState = 2);
        rbPriceAsc.setOnAction(e -> selectedState = 3);
    }
}

