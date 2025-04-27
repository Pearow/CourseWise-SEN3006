package com.sen3006.coursewise;

import com.sen3006.coursewise.models.CurrentUser;
import com.sen3006.coursewise.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginScreenController {
    
    @FXML
    TextField emailTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    HBox errorBox;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private CurrentUser currentUser;

    public void login(ActionEvent event) throws Exception{

        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        boolean loginState = CurrentUser.login(password, email);

        if (loginState) {
            // If login is successful, proceed to the next scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GuiDeneme1.fxml"));
            root = loader.load();

            // Get the controller of the next scene
            GuiDeneme1 controller = loader.getController();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            if(email.contentEquals("murat.serter@bahcesehir.edu.tr")){
                stage.setTitle("THE BOZZER!!!!");
            } else stage.setTitle("Coursewise - " + email);
            stage.show();

        } else {
            // If login fails, show an error message
            errorBox.getChildren().clear(); // Clear previous error messages
            Label errorLabel = new Label("Invalid username or password");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            errorBox.getChildren().add(errorLabel);
        }

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("GuiDeneme1.fxml"));
//        root = loader.load();
//
//        // Get the controller of the next scene
//        GuiDeneme1 controller = loader.getController();

    }
}
