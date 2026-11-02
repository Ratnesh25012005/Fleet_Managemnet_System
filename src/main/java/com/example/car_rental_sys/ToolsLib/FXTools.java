package com.example.car_rental_sys.ToolsLib;

import com.example.car_rental_sys.util.SceneManager;

/**
 * FXTools scene switcher adapter delegating to SceneManager.
 */
public class FXTools {

    public static void changeScene(String fxmlFileName) {
        SceneManager.switchScene(fxmlFileName);
    }

    public static void goToSettingPage() {
        SceneManager.switchScene("SettingPage.fxml");
    }
}

