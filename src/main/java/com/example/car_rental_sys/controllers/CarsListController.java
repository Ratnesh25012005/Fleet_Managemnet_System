package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.concurrency.AsyncExecutor;
import com.example.car_rental_sys.model.Vehicle;
import com.example.car_rental_sys.service.ServiceFactory;
import com.example.car_rental_sys.service.VehicleService;
import com.example.car_rental_sys.ui_components.CarCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the public Vehicle Catalog (carsListPage.fxml).
 * Uses authentic CarCard components with linear gradients, cutout PNGs, and specs.
 */
public class CarsListController extends Controller {

    @FXML
    private ScrollPane scrollPane;

    private FlowPane flowPane;

    // Authentic brand gradient lookup table
    private static final Map<String, String[]> MODEL_GRADIENTS = new HashMap<>();

    static {
        MODEL_GRADIENTS.put("Huracan_EVO", new String[]{"#430707", "#EE0D0D"});
        MODEL_GRADIENTS.put("Huracan_EVI", new String[]{"#430707", "#EE0D0D"});
        MODEL_GRADIENTS.put("Model_3", new String[]{"#061d36", "#0077b6"});
        MODEL_GRADIENTS.put("Tesla_Model_3", new String[]{"#061d36", "#0077b6"});
        MODEL_GRADIENTS.put("718_Cayman", new String[]{"#0a1c38", "#1669e0"});
        MODEL_GRADIENTS.put("Porsche_718", new String[]{"#0a1c38", "#1669e0"});
        MODEL_GRADIENTS.put("488_Spider", new String[]{"#360505", "#c71212"});
        MODEL_GRADIENTS.put("Ferrari_488", new String[]{"#360505", "#c71212"});
        MODEL_GRADIENTS.put("Ferrari_SF90_Spider", new String[]{"#360505", "#c71212"});
        MODEL_GRADIENTS.put("720S", new String[]{"#2c1200", "#cc5803"});
        MODEL_GRADIENTS.put("McLaren_720S", new String[]{"#2c1200", "#cc5803"});
        MODEL_GRADIENTS.put("Mclaren_720s", new String[]{"#2c1200", "#cc5803"});
        MODEL_GRADIENTS.put("M4_Competition", new String[]{"#0b2520", "#17826d"});
        MODEL_GRADIENTS.put("BMW_M4", new String[]{"#0b2520", "#17826d"});
        MODEL_GRADIENTS.put("Mercedes_AMG_GT", new String[]{"#1a1a1a", "#6b7280"});
        MODEL_GRADIENTS.put("Audi_R8", new String[]{"#111827", "#374151"});
        MODEL_GRADIENTS.put("Aston_Martin_Vantage", new String[]{"#06251b", "#0f766e"});
        MODEL_GRADIENTS.put("Ford_Mustang_GT", new String[]{"#1e1b4b", "#4338ca"});
        MODEL_GRADIENTS.put("Nissan_GTR", new String[]{"#311026", "#9d174d"});
        MODEL_GRADIENTS.put("Koenigsegg", new String[]{"#1e1b4b", "#4338ca"});
    }

    @FXML
    public void initialize() {
        flowPane = new FlowPane();
        flowPane.setPrefWidth(1270);
        flowPane.setHgap(20);
        flowPane.setVgap(20);
        flowPane.setPadding(new Insets(20, 20, 20, 20));
        flowPane.setStyle("-fx-background-color: transparent;");

        if (scrollPane != null) {
            scrollPane.setContent(flowPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        }

        loadVehicleCards();
    }

    private void loadVehicleCards() {
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
            String modelKey = v.getModel() != null ? v.getModel().replace(" ", "_") : "Vehicle";
            String[] gradients = MODEL_GRADIENTS.getOrDefault(modelKey, new String[]{"#0a1c38", "#1669e0"});

            String seats = (v.getSeats() != null) ? String.valueOf(v.getSeats()) : "2";
            String price = (v.getDailyRate() != null) ? v.getDailyRate().toPlainString() : "850";
            String transmission = (v.getTransmission() != null) ? v.getTransmission().name() : "Automatic";

            CarCard card = new CarCard(v, modelKey, seats, price, transmission, gradients[0], gradients[1]);
            flowPane.getChildren().add(card);
        }
    }

    private void renderDefaultCards() {
        flowPane.getChildren().clear();
        String[][] defaultModels = {
                {"Huracan_EVI", "2", "350", "Automatic"},
                {"Porsche_718", "2", "250", "Automatic"},
                {"Ferrari_488", "2", "450", "Automatic"},
                {"McLaren_720S", "2", "420", "Automatic"},
                {"BMW_M4", "4", "180", "Automatic"},
                {"Mercedes_AMG_GT", "2", "320", "Automatic"},
                {"Audi_R8", "2", "380", "Automatic"},
                {"Aston_Martin_Vantage", "2", "390", "Automatic"},
                {"Ford_Mustang_GT", "4", "220", "Automatic"},
                {"Nissan_GTR", "4", "260", "Automatic"}
        };

        for (int i = 0; i < defaultModels.length; i++) {
            String[] m = defaultModels[i];
            String[] gradients = MODEL_GRADIENTS.getOrDefault(m[0], new String[]{"#0a1c38", "#1669e0"});
            Vehicle mockV = new Vehicle();
            mockV.setVehicleId(i + 1);
            mockV.setBrand(m[0].split("_")[0]);
            mockV.setModel(m[0].replace("_", " "));
            mockV.setSeats(Integer.parseInt(m[1]));
            mockV.setDailyRate(new java.math.BigDecimal(m[2]));
            mockV.setImagePath("/com/example/car_rental_sys/image/cars/" + m[0] + ".png");

            CarCard card = new CarCard(mockV, m[0], m[1], m[2], m[3], gradients[0], gradients[1]);
            flowPane.getChildren().add(card);
        }
    }
}
