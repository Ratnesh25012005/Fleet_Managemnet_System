package com.example.car_rental_sys;

import com.example.car_rental_sys.controllers.Controller;
import com.example.car_rental_sys.model.Order;
import com.example.car_rental_sys.model.User;
import com.example.car_rental_sys.model.Vehicle;
import javafx.stage.Stage;

/**
 * Global status and session container matching original Java-Car-Rental-System architecture,
 * synchronized with modern enterprise services and MySQL 8 entities.
 */
public class StatusContainer {

    public static String currentCarChose = "Huracan_EVI";
    public static boolean isFirstViewCar = true;

    public static String locationChose = "Asia Pacific University of Technology & Innovation (APU)";
    public static String pickUpLocation = "Asia Pacific University of Technology & Innovation (APU)";

    public static String pickDateTime = null;
    public static String returnDateTime = null;
    public static long pickUpTimeStamp = 0;
    public static long returnTimeStamp = 0;

    public static String carDetailsEntrance = "catalog";
    public static String loginEntrance = "mainPage";

    public static Controller currentPageController = null;
    public static Stage currentStage = null;

    public static User currentUser = null;
    public static Vehicle currentVehicle = null;
    public static Order currentOrder = null;
    public static int currentOrderID = 1;

    public static String paginationUsage = "order";
    public static boolean ifInCommentPage = false;
    public static int relevantCommentID = 0;

    public static String currentPaymentMethod = "visa";
    public static String currentChooseBankCardNum = "1234567890123456";

    public static boolean isNetworkConnected = true;
    public static boolean isDataFileComplete = true;

    private StatusContainer() {
        // Static holder
    }
}

