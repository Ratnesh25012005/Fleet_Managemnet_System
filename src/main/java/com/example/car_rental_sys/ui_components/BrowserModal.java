package com.example.car_rental_sys.ui_components;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Function;

/**
 * Authentic BrowserModal dialog matching original Java-Car-Rental-System class.
 */
public class BrowserModal extends BrowserFrame {

    public String modal = "ApplicationModal";
    public Stage stage = null;

    public BrowserModal(int width, int height, String url) {
        super(width, height, url);
        initialize();
    }

    private void initialize() {
        stage = super.stage;
    }

    @Override
    public void setModality() {
        super.setModality(Modality.APPLICATION_MODAL);
    }

    @Override
    public void setModality(Modality modal) {
        super.setModality(modal);
    }

    @Override
    public void setFunction(Function<String, Void> func) {
        super.setFunction(func);
    }
}

