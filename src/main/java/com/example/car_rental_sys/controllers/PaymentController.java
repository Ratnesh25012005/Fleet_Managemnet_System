package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.concurrency.AsyncExecutor;
import com.example.car_rental_sys.model.Order;
import com.example.car_rental_sys.model.Payment;
import com.example.car_rental_sys.model.PaymentStatus;
import com.example.car_rental_sys.model.User;
import com.example.car_rental_sys.model.Vehicle;
import com.example.car_rental_sys.service.PaymentService;
import com.example.car_rental_sys.service.RentalService;
import com.example.car_rental_sys.service.ServiceFactory;
import com.example.car_rental_sys.util.AlertHelper;
import com.example.car_rental_sys.util.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Controller for Rental Checkout and Payment Processing.
 */
public class PaymentController extends Controller {

    @FXML
    public Pane mainPane;

    @FXML
    private ImageView avatarImageView;

    @FXML
    private Label customerName;

    @FXML
    private Label price;

    @FXML
    private Label duration;

    @FXML
    private Label discount;

    @FXML
    private Label tax;

    @FXML
    private Label totalPrice;

    @FXML
    private Label pickUpTime;

    @FXML
    private Label returnTime;

    @FXML
    private Button carChooseBtn;

    @FXML
    private Label carChooseText;

    @FXML
    private Label modelText;

    @FXML
    private Label priceText;

    @FXML
    private ImageView carImageView;

    @FXML
    private Label balanceText;

    @FXML
    private RadioButton radioBtn1;

    @FXML
    private RadioButton radioBtn2;

    @FXML
    private Label cardTypeAndNum;

    @FXML
    private Label expiresDate;

    @FXML
    private ImageView bankcardImage;

    @FXML
    private Button downIconBtn;

    @FXML
    private ScrollPane bankCardsContainer;

    private Vehicle currentVehicle;
    private BigDecimal calculatedTotal;

    @FXML
    public void initialize() {
        currentVehicle = CarDetailsPageController.getSelectedVehicle();
        if (currentVehicle == null) {
            currentVehicle = com.example.car_rental_sys.StatusContainer.currentVehicle;
        }
        if (currentVehicle == null) {
            currentVehicle = CarDetailsPageController.resolveVehicle(com.example.car_rental_sys.StatusContainer.currentCarChose);
        }
        if (currentVehicle != null) {
            CarDetailsPageController.setSelectedVehicle(currentVehicle);
            com.example.car_rental_sys.StatusContainer.currentVehicle = currentVehicle;
        }

        setupPaymentMethod();
        populateCustomerData();
        populatePricingSummary();
        if (bankCardsContainer != null) {
            bankCardsContainer.setVisible(false);
        }
    }

    private void setupPaymentMethod() {
        if (radioBtn2 != null) radioBtn2.setSelected(true);
        if (radioBtn1 != null) radioBtn1.setSelected(false);
    }

    private void populateCustomerData() {
        User user = ServiceFactory.getInstance().getAuthService().getCurrentUser();
        if (user == null) {
            user = com.example.car_rental_sys.StatusContainer.currentUser;
        }
        if (user != null) {
            if (customerName != null) {
                customerName.setText(user.getFullName() != null ? user.getFullName() : user.getUsername());
            }
            if (balanceText != null) {
                balanceText.setText("$5,000.00");
            }
        }
    }

    private void populatePricingSummary() {
        if (currentVehicle == null) {
            currentVehicle = CarDetailsPageController.resolveVehicle(com.example.car_rental_sys.StatusContainer.currentCarChose);
        }

        BigDecimal dailyRate = (currentVehicle != null && currentVehicle.getDailyRate() != null)
                ? currentVehicle.getDailyRate()
                : new BigDecimal("350.00");

        String brand = currentVehicle != null ? currentVehicle.getBrand() : "Lamborghini";
        String model = currentVehicle != null ? currentVehicle.getModel() : "Huracan EVO";

        long pTs = com.example.car_rental_sys.StatusContainer.pickUpTimeStamp > 0
                ? com.example.car_rental_sys.StatusContainer.pickUpTimeStamp
                : System.currentTimeMillis() + 3600_000L;
        long rTs = com.example.car_rental_sys.StatusContainer.returnTimeStamp > pTs
                ? com.example.car_rental_sys.StatusContainer.returnTimeStamp
                : pTs + 48L * 3600_000L;

        long hoursDiff = Math.max(1, (rTs - pTs) / (1000 * 3600));
        long daysDiff = Math.max(1, (hoursDiff + 23) / 24);

        if (modelText != null) modelText.setText(brand + " " + model);
        if (priceText != null) priceText.setText("$" + dailyRate.toPlainString() + "/D");
        if (price != null) price.setText("$" + dailyRate.divide(new BigDecimal("24"), 2, RoundingMode.HALF_UP) + "/H");
        if (duration != null) duration.setText(hoursDiff + " Hours (" + daysDiff + " Days)");
        if (discount != null) discount.setText("-$20.00");
        if (tax != null) tax.setText("6%");

        BigDecimal base = dailyRate.multiply(BigDecimal.valueOf(daysDiff));
        BigDecimal taxAmount = base.multiply(new BigDecimal("0.06"));
        calculatedTotal = base.add(taxAmount).subtract(new BigDecimal("20.00"));
        if (calculatedTotal.compareTo(BigDecimal.ZERO) < 0) {
            calculatedTotal = base;
        }

        if (totalPrice != null) {
            totalPrice.setText("$" + calculatedTotal.setScale(2, RoundingMode.HALF_UP).toPlainString());
        }

        String pStr = com.example.car_rental_sys.ToolsLib.DateTools.timeStampToDateTime(pTs);
        String rStr = com.example.car_rental_sys.ToolsLib.DateTools.timeStampToDateTime(rTs);
        if (pickUpTime != null) pickUpTime.setText(pStr.length() >= 16 ? pStr.substring(0, 16) : pStr);
        if (returnTime != null) returnTime.setText(rStr.length() >= 16 ? rStr.substring(0, 16) : rStr);

        loadCarImage(carImageView);
    }

    @FXML
    private void makePaymentBtnClickEvent(ActionEvent event) {
        User user = ServiceFactory.getInstance().getAuthService().getCurrentUser();
        if (user == null) {
            user = com.example.car_rental_sys.StatusContainer.currentUser;
        }
        if (user == null) {
            AlertHelper.showWarning("Session Expired", "Please log in to complete the transaction.");
            SceneManager.switchScene("loginPage.fxml");
            return;
        }

        if (currentVehicle == null) {
            currentVehicle = CarDetailsPageController.getSelectedVehicle();
        }
        if (currentVehicle == null) {
            currentVehicle = com.example.car_rental_sys.StatusContainer.currentVehicle;
        }
        if (currentVehicle == null) {
            currentVehicle = CarDetailsPageController.resolveVehicle(com.example.car_rental_sys.StatusContainer.currentCarChose);
        }

        long pTs = com.example.car_rental_sys.StatusContainer.pickUpTimeStamp > 0
                ? com.example.car_rental_sys.StatusContainer.pickUpTimeStamp
                : System.currentTimeMillis() + 3600_000L;
        long rTs = com.example.car_rental_sys.StatusContainer.returnTimeStamp > pTs
                ? com.example.car_rental_sys.StatusContainer.returnTimeStamp
                : pTs + 48L * 3600_000L;

        java.time.Instant startInstant = java.time.Instant.ofEpochMilli(pTs);
        java.time.Instant endInstant = java.time.Instant.ofEpochMilli(rTs);
        java.time.ZoneId zone = java.time.ZoneId.systemDefault();

        LocalDateTime start = LocalDateTime.ofInstant(startInstant, zone);
        LocalDateTime end = LocalDateTime.ofInstant(endInstant, zone);
        if (!end.isAfter(start)) {
            end = start.plusDays(2);
        }

        final Vehicle finalVehicle = currentVehicle;
        final User finalUser = user;
        final LocalDateTime finalStart = start;
        final LocalDateTime finalEnd = end;
        final String pickupLoc = (com.example.car_rental_sys.StatusContainer.pickUpLocation != null && !com.example.car_rental_sys.StatusContainer.pickUpLocation.isBlank())
                ? com.example.car_rental_sys.StatusContainer.pickUpLocation
                : "Central Terminal";

        AsyncExecutor.getInstance().execute(() -> {
            try {
                RentalService rentalService = ServiceFactory.getInstance().getRentalService();
                PaymentService paymentService = ServiceFactory.getInstance().getPaymentService();

                // 1. Create reservation (with conflict handling)
                Order order;
                try {
                    order = rentalService.createReservation(
                            finalUser.getUserId(),
                            finalVehicle.getVehicleId(),
                            finalStart,
                            finalEnd,
                            pickupLoc,
                            pickupLoc
                    );
                } catch (com.example.car_rental_sys.service.BookingConflictException bce) {
                    // Collision fallback: Shift timeframe to next available slot
                    LocalDateTime altStart = LocalDateTime.now().plusDays(2);
                    LocalDateTime altEnd = altStart.plusDays(2);
                    order = rentalService.createReservation(
                            finalUser.getUserId(),
                            finalVehicle.getVehicleId(),
                            altStart,
                            altEnd,
                            pickupLoc,
                            pickupLoc
                    );
                }

                // 2. Process atomic payment
                Payment payment = new Payment();
                payment.setOrderId(order.getOrderId());
                payment.setAmount(calculatedTotal != null ? calculatedTotal : (order.getTotalPrice() != null ? order.getTotalPrice() : new BigDecimal("280.00")));
                payment.setPaymentMethod(radioBtn1 != null && radioBtn1.isSelected() ? "PayPal" : "CreditCard");
                payment.setPaymentStatus(PaymentStatus.SUCCESS);

                paymentService.processPayment(payment);

                // 3. Confirm reservation
                rentalService.confirmBooking(order.getOrderId());

                // 4. Update status container
                com.example.car_rental_sys.StatusContainer.currentOrder = order;
                com.example.car_rental_sys.StatusContainer.currentOrderID = order.getOrderId();

                final Order confirmedOrder = order;
                Platform.runLater(() -> {
                    OrderDetailsComponentController.setSelectedOrder(confirmedOrder);
                    SceneManager.switchScene("paySuccessPage.fxml");
                });
            } catch (Exception e) {
                Platform.runLater(() -> AlertHelper.showError("Payment Failed", e.getMessage()));
            }
        });
    }

    @FXML
    private void goToCarListPage(MouseEvent event) {
        SceneManager.switchScene("carsListPage.fxml");
    }

    @FXML
    private void viewCarDetailBtnClickEvent(MouseEvent event) {
        SceneManager.switchScene("carDetailsPage.fxml");
    }

    @FXML
    private void selectRadioBtn1(MouseEvent event) {
        if (radioBtn1 != null) radioBtn1.setSelected(true);
        if (radioBtn2 != null) radioBtn2.setSelected(false);
    }

    @FXML
    private void selectRadioBtn2(MouseEvent event) {
        if (radioBtn2 != null) radioBtn2.setSelected(true);
        if (radioBtn1 != null) radioBtn1.setSelected(false);
    }

    @FXML
    private void showBankCardChooser(MouseEvent event) {
        if (bankCardsContainer != null) {
            bankCardsContainer.setVisible(!bankCardsContainer.isVisible());
        }
    }

    private void loadCarImage(ImageView imageView) {
        if (imageView == null) return;
        String modelKey = (currentVehicle != null && currentVehicle.getModel() != null)
                ? currentVehicle.getModel().replace(" ", "_")
                : (com.example.car_rental_sys.StatusContainer.currentCarChose != null
                        ? com.example.car_rental_sys.StatusContainer.currentCarChose
                        : "Huracan_EVI");

        String path = "/com/example/car_rental_sys/image/cars/" + modelKey + ".png";
        Image img = com.example.car_rental_sys.ToolsLib.ImageTools.getImageObjFromPath(path);
        if (img == null) {
            img = com.example.car_rental_sys.ToolsLib.ImageTools.getImageObjFromPath("/com/example/car_rental_sys/image/cars/Huracan_EVI.png");
        }
        if (img != null) {
            imageView.setImage(img);
        }
    }
}

