package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.concurrency.AsyncExecutor;
import com.example.car_rental_sys.model.Vehicle;
import com.example.car_rental_sys.service.ServiceFactory;
import com.example.car_rental_sys.service.VehicleService;
import com.example.car_rental_sys.ui_components.VehicleCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for Admin Fleet Overview (OverviewVehiclePage.fxml).
 * Uses authentic 233x221 VehicleCard components with specs and Edit modal buttons.
 */
public class OverviewVehiclePageController extends Controller {

    @FXML
    private ScrollPane scrollPaneVehicle;

    private FlowPane flowPane;

    private static final Map<String, String[]> MODEL_GRADIENTS = new HashMap<>();

    static {
        MODEL_GRADIENTS.put("Huracan_EVI", new String[]{"#430707", "#EE0D0D"});
        MODEL_GRADIENTS.put("Porsche_718", new String[]{"#0a1c38", "#1669e0"});
        MODEL_GRADIENTS.put("Ferrari_488", new String[]{"#360505", "#c71212"});
        MODEL_GRADIENTS.put("McLaren_720S", new String[]{"#2c1200", "#cc5803"});
        MODEL_GRADIENTS.put("BMW_M4", new String[]{"#0b2520", "#17826d"});
        MODEL_GRADIENTS.put("Mercedes_AMG_GT", new String[]{"#1a1a1a", "#6b7280"});
        MODEL_GRADIENTS.put("Audi_R8", new String[]{"#111827", "#374151"});
        MODEL_GRADIENTS.put("Aston_Martin_Vantage", new String[]{"#06251b", "#0f766e"});
        MODEL_GRADIENTS.put("Ford_Mustang_GT", new String[]{"#1e1b4b", "#4338ca"});
        MODEL_GRADIENTS.put("Nissan_GTR", new String[]{"#311026", "#9d174d"});
    }

    @FXML
    public void initialize() {
        flowPane = new FlowPane();
        flowPane.setPrefWidth(871);
        flowPane.setHgap(28);
        flowPane.setVgap(28);
        flowPane.setPadding(new Insets(20, 20, 20, 20));
        flowPane.setStyle("-fx-background-color: transparent;");

        if (scrollPaneVehicle != null) {
            scrollPaneVehicle.setContent(flowPane);
            scrollPaneVehicle.setFitToWidth(true);
            scrollPaneVehicle.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        }

        loadAdminVehicleCards();
    }

    private void loadAdminVehicleCards() {
        AsyncExecutor.getInstance().execute(() -> {
            try {
                VehicleService vehicleService = ServiceFactory.getInstance().getVehicleService();
                List<Vehicle> vehicles = vehicleService.getAllVehicles();

                Platform.runLater(() -> renderCards(vehicles));
            } catch (Exception e) {
                Platform.runLater(this::renderDefaultCards);
            }
        });
    }

    private void renderCards(List<Vehicle> vehicles) {
        flowPane.getChildren().clear();

        if (vehicles == null || vehicles.isEmpty()) {
            renderDefaultCards();
            return;
        }

        for (Vehicle v : vehicles) {
            String modelKey = v.getModel().replace(" ", "_");
            String[] gradients = MODEL_GRADIENTS.getOrDefault(modelKey, new String[]{"#0a1c38", "#1669e0"});

            String idStr = String.valueOf(v.getVehicleId());
            String priceStr = (v.getDailyRate() != null) ? v.getDailyRate().toPlainString() : "850";
            String seatsStr = (v.getSeats() != null) ? String.valueOf(v.getSeats()) : "2";
            String transStr = (v.getTransmission() != null) ? v.getTransmission().name() : "Automatic";
            String brandStr = (v.getBrand() != null) ? v.getBrand() : "Lamborghini";

            VehicleCard card = new VehicleCard(idStr, modelKey, seatsStr, priceStr, transStr, gradients[1], gradients[0], brandStr);
            flowPane.getChildren().add(card);
        }
    }

    private void renderDefaultCards() {
        flowPane.getChildren().clear();
        String[][] defaultModels = {
                {"1", "Huracan_EVI", "2", "1200", "Automatic", "Lamborghini"},
                {"2", "Porsche_718", "2", "850", "Automatic", "Porsche"},
                {"3", "Ferrari_488", "2", "1500", "Automatic", "Ferrari"},
                {"4", "McLaren_720S", "2", "1400", "Automatic", "McLaren"},
                {"5", "BMW_M4", "4", "600", "Automatic", "BMW"},
                {"6", "Mercedes_AMG_GT", "2", "1100", "Automatic", "Mercedes-Benz"},
                {"7", "Audi_R8", "2", "1300", "Automatic", "Audi"},
                {"8", "Aston_Martin_Vantage", "2", "1350", "Automatic", "Aston Martin"},
                {"9", "Ford_Mustang_GT", "4", "450", "Automatic", "Ford"},
                {"10", "Nissan_GTR", "4", "900", "Automatic", "Nissan"}
        };

        for (String[] m : defaultModels) {
            String[] gradients = MODEL_GRADIENTS.getOrDefault(m[1], new String[]{"#0a1c38", "#1669e0"});
            VehicleCard card = new VehicleCard(m[0], m[1], m[2], m[3], m[4], gradients[1], gradients[0], m[5]);
            flowPane.getChildren().add(card);
        }
    }
}
