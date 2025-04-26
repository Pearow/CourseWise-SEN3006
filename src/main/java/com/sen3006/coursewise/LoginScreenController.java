package com.sen3006.coursewise;

import com.sen3006.coursewise.models.CurrentUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreenController {
    
    @FXML
    TextField emailTextField;
    @FXML
    TextField passwordTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private CurrentUser currentUser;

    public void login(ActionEvent event) throws Exception{

        String username = emailTextField.getText();
        String password = passwordTextField.getText();

        //uncomment the following line when CurrentUser class is implemented
        //currentUser.login(password);

        // Load the next scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GuiDeneme1.fxml"));
        root = loader.load();
        
        // Get the controller of the next scene
        GuiDeneme1 controller = loader.getController();
        
        

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
