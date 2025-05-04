package com.sen3006.coursewise.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

    @Override
    public void start(Stage stage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Coursewise");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println("Welcome to Coursewise!");
        launch(args);
    }
}