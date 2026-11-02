package com.example.car_rental_sys.controllers;

import com.example.car_rental_sys.StatusContainer;
import com.example.car_rental_sys.ToolsLib.DataTools;
import com.example.car_rental_sys.ToolsLib.DateTools;
import com.example.car_rental_sys.ToolsLib.FXTools;
import com.example.car_rental_sys.ToolsLib.ImageTools;
import com.example.car_rental_sys.ui_components.BrowserModal;
import com.example.car_rental_sys.ui_components.MessageFrame;
import com.example.car_rental_sys.ui_components.MessageFrameType;
import com.example.car_rental_sys.util.DatePickerModal;
import com.example.car_rental_sys.util.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * Controller for the Main Public Landing Page / Catalog Launchpad.
 * Restored with authentic hero car animations, green/blue themes, interactive map modal, and date pickers.
 */
public class MainPageController extends Controller {

    public static MainPageController mainPageController;

    @FXML
    public Pane mainPane;

    @FXML
    private ImageView carImage;

    @FXML
    private Button searchBtn;

    @FXML
    private Button openCatalogBtn;

    @FXML
    private Label pickUpDateLabel;

    @FXML
    private Label returnDateLabel;

    @FXML
    private Label introLabel;

    @FXML
    private Label locationBtn;

    @FXML
    private Label pickUpDateBtn;

    @FXML
    private Label returnDateBtn;

    @FXML
    public Label locationText;

    @FXML
    private Pane operationBar;

    private final String imagefolderRoot = "/com/example/car_rental_sys/image/UI/";

    private final String[] carImageList = new String[]{
            "600LT-Spider-rg1.png",
            "grearcar.png",
            "Koenigsel.png",
            "Tuatara_Striker.png"
    };

    private int currentCarImageIndex = 0;

    @FXML
    public void initialize() {
        mainPageController = this;
        StatusContainer.currentPageController = this;

        initIntroLabel();
        addScrollEvent();
        addKeyPressEvent();
        initDateTimeLabel();

        if (StatusContainer.pickUpLocation != null && locationText != null) {
            locationText.setText(StatusContainer.pickUpLocation);
        }
    }

    private void initIntroLabel() {
        if (introLabel != null) {
            introLabel.setWrapText(true);
            String text = "We want you to have a stress-free\n" +
                    "rental experience, so we make it easy to\n" +
                    "hire a car - by providing simple car\n" +
                    "rental app with customer reviews and\n" +
                    "plenty of pick-up locations across the \ncities.\n";
            introLabel.setText(text);
        }
    }

    @FXML
    void addScrollEvent() {
        if (mainPane == null) return;
        mainPane.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();
            if (deltaY < 0) {
                changeCardImage(1);
            } else {
                changeCardImage(-1);
            }
        });
    }

    void addKeyPressEvent() {
        if (mainPane == null) return;
        mainPane.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case DOWN:
                case LEFT:
                    changeCardImage(1);
                    break;
                case UP:
                case RIGHT:
                    changeCardImage(-1);
                    break;
                default:
                    break;
            }
        });
    }

    void changeCardImage(int direction) {
        int nextCarImageIndex;
        if (direction < 0) {
            nextCarImageIndex = currentCarImageIndex + 1;
            if (nextCarImageIndex >= carImageList.length) {
                nextCarImageIndex = 0;
            }
        } else {
            nextCarImageIndex = currentCarImageIndex - 1;
            if (nextCarImageIndex < 0) {
                nextCarImageIndex = carImageList.length - 1;
            }
        }
        changeImage(imagefolderRoot + carImageList[nextCarImageIndex], carImage);
        currentCarImageIndex = nextCarImageIndex;
        changeStyleAccordingIndex(currentCarImageIndex);
    }

    @FXML
    void initDateTimeLabel() {
        Date currentDate = new Date();
        Date dateAfterOneWeek = new Date(currentDate.getTime() + 7L * 24 * 60 * 60 * 1000);

        if (StatusContainer.pickDateTime == null) {
            String pStr = formatDateTime(currentDate);
            String rStr = formatDateTime(dateAfterOneWeek);
            if (pickUpDateLabel != null) pickUpDateLabel.setText(pStr);
            if (returnDateLabel != null) returnDateLabel.setText(rStr);
            StatusContainer.pickDateTime = pStr;
            StatusContainer.returnDateTime = rStr;
            StatusContainer.pickUpTimeStamp = currentDate.getTime();
            StatusContainer.returnTimeStamp = dateAfterOneWeek.getTime();
        } else {
            if (pickUpDateLabel != null) pickUpDateLabel.setText(StatusContainer.pickDateTime);
            if (returnDateLabel != null) returnDateLabel.setText(StatusContainer.returnDateTime);
        }
    }

    String formatDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        Month month = localDate.getMonth();
        String shortMonth = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        String pickUpDate = dateFormat.format(date).substring(8, 10);
        String pickUpTime = dateFormat.format(date).substring(12, 17);
        String ampm = (Integer.parseInt(pickUpTime.substring(0, 2)) >= 12) ? "PM" : "AM";

        return shortMonth + " " + pickUpDate + ", " + pickUpTime + " " + ampm;
    }

    @FXML
    void changeImage(String fileUrl, ImageView imageView) {
        if (imageView == null) return;
        Image image = ImageTools.getImageObjFromPath(fileUrl);
        if (image != null) {
            imageView.setImage(image);
        }
    }

    @FXML
    void changeClass(Button button, String className) {
        if (button == null) return;
        button.getStyleClass().clear();
        button.getStyleClass().add(className);
    }

    void changeClass(Label button, String className) {
        if (button == null) return;
        button.getStyleClass().clear();
        button.getStyleClass().add(className);
    }

    @FXML
    public void changeStyle(String styleType) {
        if (styleType.equalsIgnoreCase("green")) {
            if (NavigationBarController.navigationBarControllerInstance != null) {
                NavigationBarController.navigationBarControllerInstance.changeLogoImage("green");
            }
            changeClass(searchBtn, "searchBtnGreen");
            changeClass(openCatalogBtn, "openCatalogBtnGreen");
            changeClass(locationBtn, "operaBtnGreen");
            changeClass(pickUpDateBtn, "operaBtnGreen");
            changeClass(returnDateBtn, "operaBtnGreen");
        } else if (styleType.equalsIgnoreCase("blue")) {
            if (NavigationBarController.navigationBarControllerInstance != null) {
                NavigationBarController.navigationBarControllerInstance.changeLogoImage("blue");
            }
            changeClass(searchBtn, "searchBtnBlue");
            changeClass(openCatalogBtn, "openCatalogBtnBlue");
            changeClass(locationBtn, "operaBtnBlue");
            changeClass(pickUpDateBtn, "operaBtnBlue");
            changeClass(returnDateBtn, "operaBtnBlue");
        }
    }

    @FXML
    void changeStyleAccordingIndex(int index) {
        if (index % 2 == 0) {
            changeStyle("green");
        } else {
            changeStyle("blue");
        }
    }

    @FXML
    void openCatalogBtnClick(ActionEvent actionEvent) {
        StatusContainer.carDetailsEntrance = "catalog";
        FXTools.changeScene("carsListPage.fxml");
    }

    @FXML
    public void locationBtnClick() {
        String url = "/com/example/car_rental_sys/html/googleMap.html";
        BrowserModal browserModal = new BrowserModal(800, 600, url);
        browserModal.setModality();
        Function<String, Void> func = (message) -> {
            if (message != null && !message.isBlank()) {
                Platform.runLater(() -> {
                    if (locationText != null) {
                        locationText.setText(message);
                    }
                });
                StatusContainer.pickUpLocation = message;
                StatusContainer.locationChose = message;
            }
            return null;
        };

        browserModal.setFunction(func);
        browserModal.show();
    }

    @FXML
    public void pickUpBtnClick() {
        showDatePicker();
    }

    @FXML
    public void returnBtnClick() {
        showDatePicker();
    }

    @FXML
    void searchBtnClick() {
        StatusContainer.carDetailsEntrance = "search";
        if (StatusContainer.pickUpTimeStamp == 0 || StatusContainer.returnTimeStamp == 0) {
            MessageFrame messageFrame = new MessageFrame(MessageFrameType.WARNING, "Please choose pick up date and return date first");
            messageFrame.show();
        } else if (DataTools.getAvailableCars().isEmpty()) {
            MessageFrame messageFrame = new MessageFrame(MessageFrameType.WARNING, "No available cars");
            messageFrame.show();
        } else {
            FXTools.changeScene("carsListPage.fxml");
        }
    }

    public void showDatePicker() {
        DatePickerModal modal = new DatePickerModal(
                SceneManager.getPrimaryStage(),
                (pickStr, retStr) -> {
                    StatusContainer.pickDateTime = pickStr;
                    StatusContainer.returnDateTime = retStr;
                    StatusContainer.pickUpTimeStamp = DateTools.dateTimeToTimestamp(pickStr);
                    StatusContainer.returnTimeStamp = DateTools.dateTimeToTimestamp(retStr);

                    if (pickUpDateLabel != null) pickUpDateLabel.setText(pickStr);
                    if (returnDateLabel != null) returnDateLabel.setText(retStr);
                }
        );
        modal.show();
    }
}
