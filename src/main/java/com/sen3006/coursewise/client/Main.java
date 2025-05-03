package com.sen3006.coursewise.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

//    @Override old code for Main.fxml
//    public void start(Stage primaryStage)throws Exception{
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Coursewise");
//        primaryStage.show();
//    }

    @Override
    public void start(Stage stage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Coursewise");
            stage.setResizable(false);
            stage.getIcons().add(new Image("file:src/main/resources/com/sen3006/coursewise/client/icon.png"));
            stage.setScene(scene);
            stage.show();

//            stage.setOnCloseRequest(event -> {
//                event.consume(); // Prevent the default close action
//                try {
//                    logout(stage);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void logout(Stage stage) throws Exception{
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Logout");
//        alert.setHeaderText("Are you sure you want to logout?");
//        alert.setContentText("You will be logged out of your account.");
//
//        if (alert.showAndWait().get() == ButtonType.OK) {
//            System.out.println("Logging out...");
//            stage.close();
//        }
//    }

    public static void main(String[] args) {
        System.out.println("Welcome to Coursewise!");
        launch(args);
    }
}