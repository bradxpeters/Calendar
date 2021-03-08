package main;

import authorization.AuthController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tests.Tests;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(AuthController.class.getResource("login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 420, 315));
        primaryStage.show();

//        var tests = new Tests();
//        tests.runAll();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
