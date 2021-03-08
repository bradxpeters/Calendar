package authorization;

import calendar.CalendarController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import users.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    @FXML
    public TextField usernameTextfield;

    @FXML
    public TextField passwordTextfield;

    @FXML
    public Button loginButton;

    @FXML
    public void handleLoginButton(ActionEvent event) throws SQLException, IOException {

        var username = this.getUsernameTextfield().getText().trim();
        var password = this.getPasswordTextfield().getText().trim();

        if (this.isValidLoginAttempt(username, password)) {
            this.launchMainApp();
        }
    }

    private void launchMainApp() throws IOException {
        Parent root = FXMLLoader.load(CalendarController.class.getResource("calendar.fxml"));
        var newStage = new Stage();
        newStage.setTitle("Calendar");
        newStage.setScene(new Scene(root, 600, 400));
        newStage.show();

        var stage = (Stage) this.getLoginButton().getScene().getWindow();
        stage.close();
    }

    private boolean isValidLoginAttempt(String username, String password) throws SQLException {
        var userRepo = new UserRepository();
        var user = userRepo.fetchUserByUsername(username);

        if (user != null) {
            if (!user.getPassword().equals(password)) {
                var alert = new Alert(Alert.AlertType.ERROR, "Password is incorrect!");
                alert.showAndWait();
                return false;
            } else {
                return true;
            }
        } else {
            var alert = new Alert(Alert.AlertType.ERROR, "User " + username + " not found!");
            alert.showAndWait();
            return false;
        }
    }

    private void handleLoginButtonState() {
        this.loginButton.setDisable(!canSubmitLogin());
    }

    private boolean canSubmitLogin() {
        return this.getUsernameTextfield().getText() != null
            && !this.getUsernameTextfield().getText().equalsIgnoreCase("")
            && this.getPasswordTextfield().getText() != null
            && !this.getPasswordTextfield().getText().equalsIgnoreCase("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            this.getLoginButton().setDisable(true);

            // listeners
            this.getPasswordTextfield()
                .textProperty()
                .addListener((observableValue, oldValue, newValue) -> handleLoginButtonState());

            this.getUsernameTextfield()
                .textProperty()
                .addListener((observableValue, oldValue, newValue) -> handleLoginButtonState());

        });


    }

    public TextField getUsernameTextfield() {
        return usernameTextfield;
    }

    public void setUsernameTextfield(TextField usernameTextfield) {
        this.usernameTextfield = usernameTextfield;
    }

    public TextField getPasswordTextfield() {
        return passwordTextfield;
    }

    public void setPasswordTextfield(TextField passwordTextfield) {
        this.passwordTextfield = passwordTextfield;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }
}
