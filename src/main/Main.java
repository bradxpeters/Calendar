package main;

import database.DatabaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        var db = DatabaseConnector.getInstance();
        PreparedStatement ps = db.prepareStatement("SELECT * FROM appointments");
        ResultSet rs = ps.executeQuery();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
