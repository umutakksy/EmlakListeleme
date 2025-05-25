package com.example.emlak;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EmlakApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/emlak/emlak-main.fxml"));
            Scene scene = new Scene(loader.load(), 900, 650);

            primaryStage.setTitle("Emlak İlan Yönetim Sistemi");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("FXML dosyası yüklenemedi: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}