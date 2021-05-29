package authorization;

import baseInterface.BaseInterfaceController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import users.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    private ResourceBundle bundle;

    @FXML
    private TextField usernameTextfield;

    @FXML
    private TextField passwordTextfield;

    @FXML
    private Button loginButton;

    @FXML
    private Label locationLabel;

    @FXML
    private Label languageLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    public void handleLoginButton(ActionEvent event) throws SQLException, IOException {

        var username = this.usernameTextfield.getText().trim();
        var password = this.passwordTextfield.getText().trim();

        if (this.isValidLoginAttempt(username, password)) {
            this.launchMainApp();
        }
    }

    private void launchMainApp() throws IOException {
        Parent root = FXMLLoader.load(BaseInterfaceController.class.getResource("baseInterface.fxml"));
        var newStage = new Stage();
        newStage.setTitle("World Calendar");
        newStage.setScene(new Scene(root, 1200, 600));
        newStage.show();

        var stage = (Stage) this.loginButton.getScene().getWindow();
        stage.close();
    }

    private boolean isValidLoginAttempt(String username, String password) throws SQLException {
        var userRepo = new UserRepository();
        var user = userRepo.fetchUserByUsername(username);

        if (user != null) {
            if (!user.getPassword().equals(password)) {
                var alert = new Alert(Alert.AlertType.ERROR, this.bundle.getString("PassError"));
                alert.showAndWait();
                return false;
            } else {
                // Successful login
                AuthorizedState.getInstance().setAuthorizedUser(user);
                return true;
            }
        } else {
            var alert = new Alert(Alert.AlertType.ERROR, this.bundle.getString("UserError"));
            alert.showAndWait();
            return false;
        }
    }

    private void handleLoginButtonState() {
        this.loginButton.setDisable(!canSubmitLogin());
    }

    private boolean canSubmitLogin() {
        return this.passwordTextfield.getText() != null
            && !this.passwordTextfield.getText().equalsIgnoreCase("")
            && this.passwordTextfield.getText() != null
            && !this.passwordTextfield.getText().equalsIgnoreCase("");
    }

    public void handleLocalization(Locale localOverride) {
        if (localOverride != null) {
            Locale.setDefault(localOverride);
        }
        this.setBundle(ResourceBundle.getBundle("Language.lang", Locale.getDefault()));
        this.locationLabel.setText(ZoneId.systemDefault().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        this.languageLabel.setText(Locale.getDefault().getDisplayLanguage());
        this.usernameLabel.setText(this.bundle.getString("Username"));
        this.passwordLabel.setText(this.bundle.getString("Password"));
        this.loginButton.setText(this.bundle.getString("Login"));
    }

    public void handleAddListeners() {
        this.passwordTextfield
            .textProperty()
            .addListener((observableValue, oldValue, newValue) -> handleLoginButtonState());

        this.usernameTextfield
            .textProperty()
            .addListener((observableValue, oldValue, newValue) -> handleLoginButtonState());
    }

    public void handleButtonStates() {
        this.loginButton.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            // Buttons
            this.handleButtonStates();

            // Listeners
            this.handleAddListeners();

            // Localization
            var localeOverride = new Locale("fr", "France");
            this.handleLocalization(null);
        });

    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
}
