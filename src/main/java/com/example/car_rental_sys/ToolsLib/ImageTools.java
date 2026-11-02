package com.example.car_rental_sys.ToolsLib;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Image processing and loading utilities for authentic UI visual components.
 */
public class ImageTools {

    public static Image getImageObjFromPath(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }

        // Clean prefix
        String cleanPath = path;
        if (cleanPath.startsWith("file:")) {
            cleanPath = cleanPath.substring(5);
        }

        // Try file path on disk
        File file = new File(cleanPath);
        if (file.exists()) {
            return new Image(file.toURI().toString());
        }

        // Try classpath resource
        String resPath = path;
        if (resPath.startsWith("src/main/resources")) {
            resPath = resPath.substring("src/main/resources".length());
        }
        if (!resPath.startsWith("/")) {
            resPath = "/" + resPath;
        }

        try (InputStream is = ImageTools.class.getResourceAsStream(resPath)) {
            if (is != null) {
                return new Image(is);
            }
        } catch (Exception ignored) {
        }

        URL url = ImageTools.class.getResource(resPath);
        if (url != null) {
            return new Image(url.toExternalForm());
        }

        // Fallback file attempt
        try {
            return new Image(new File(cleanPath).toURI().toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static void setImageShapeToCircle(ImageView imageView) {
        if (imageView == null) return;
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2.0;
        if (radius <= 0) radius = 20.0;
        Circle clip = new Circle(radius, radius, radius);
        imageView.setClip(clip);
    }

    public static void centerImage(ImageView imageView) {
        if (imageView == null || imageView.getImage() == null) return;
        Image img = imageView.getImage();
        double ratioX = imageView.getFitWidth() / Math.max(1, img.getWidth());
        double ratioY = imageView.getFitHeight() / Math.max(1, img.getHeight());
        double rePosition = Math.min(ratioX, ratioY);

        double w = img.getWidth() * rePosition;
        double h = img.getHeight() * rePosition;

        imageView.setX((imageView.getFitWidth() - w) / 2);
        imageView.setY((imageView.getFitHeight() - h) / 2);
    }

    public static Image getCarImageObjFromCarID(int carID) {
        String path = "/com/example/car_rental_sys/image/cars/Huracan_EVI.png";
        return getImageObjFromPath(path);
    }
}
