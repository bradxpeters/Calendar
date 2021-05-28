package customers;

import countries.Country;
import countries.CountryRepository;
import firstLevelDivisions.FirstLevelDivision;
import firstLevelDivisions.FirstLevelDivisionRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerFormController implements Initializable {

    @FXML
    private TextField customerIdTextField;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField addCustomerTextField;

    @FXML
    private TextField customerPostalCodeTextField;

    @FXML
    private TextField customerPhoneTextField;

    @FXML
    private ComboBox<FirstLevelDivision> firstLevelDivisionComboBox;

    @FXML
    private ComboBox<Country> countryComboBox;

    private boolean isUpdatingCustomer = false;

    private CountryRepository countryRepository;

    private FirstLevelDivisionRepository firstLevelDivisionRepository;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        firstLevelDivisionComboBox.setDisable(true);

        // Initialize repos
        this.setCountryRepository(new CountryRepository());
        this.setFirstLevelDivisionRepository(new FirstLevelDivisionRepository());

        // Initialize form components
        this.setupCountryComboBox();
        this.setupFirstLevelDivisionComboBox();
    }

    private void setupCountryComboBox() {
        Callback<ListView<Country>, ListCell<Country>> factory = lv -> new ListCell<>() {

            @Override
            protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getCountry());
            }

        };

        countryComboBox.setItems(this.countryRepository.fetchAll());
        countryComboBox.setCellFactory(factory);
        countryComboBox.setButtonCell(factory.call(null));

    }

    private void setupFirstLevelDivisionComboBox() {
        Callback<ListView<FirstLevelDivision>, ListCell<FirstLevelDivision>> factory =
            lv -> new ListCell<>() {

            @Override
            protected void updateItem(FirstLevelDivision item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getDivision());
            }
        };

        firstLevelDivisionComboBox.setItems(this.firstLevelDivisionRepository.fetchAll());
        firstLevelDivisionComboBox.setCellFactory(factory);
        firstLevelDivisionComboBox.setButtonCell(factory.call(null));

    }

    /**
     * Prefills the customer menu to allow an user to edit a customer
     * @param cus the customer object to fill the table
     */
    public void prefill (Customer cus) {
        isUpdatingCustomer = true;

        customerIdTextField.setText(String.valueOf(cus.getCustomerId()));
        customerNameTextField.setText(cus.getCustomerName());
        addCustomerTextField.setText(cus.getAddress());
        customerPostalCodeTextField.setText(cus.getPostalCode());
        customerPhoneTextField.setText(cus.getPhone());
        countryComboBox.setValue(
            this.countryRepository.fetchCountryByDivisionId(cus.getDivisionId())
        );
        firstLevelDivisionComboBox.setValue(
            this.firstLevelDivisionRepository.fetchFirstLevelDivisionById(cus.getDivisionId())
        );
    }

    /**
     * Compiles all the menu information when the submit button is hit
     */
    @FXML
    private void handleSubmitButton(ActionEvent event) {
        System.out.print("Submit button hit!\ncreating temp customer...");
        int tempID = 0;
        if (isUpdatingCustomer) tempID = Integer.parseInt(customerIdTextField.getText());
        String tempName = customerNameTextField.getText();
        String tempAdd = addCustomerTextField.getText();
        String tempPost = customerPostalCodeTextField.getText();
        String tempPhone = customerPhoneTextField.getText();
        FirstLevelDivision tempDiv = firstLevelDivisionComboBox.getValue();
        Customer cus = new Customer(tempID, tempName, tempAdd, tempPost, tempPhone, tempDiv.getDivisionId());
        System.out.print(cus.getCustomerName());
        System.out.print(" Done!\n\tPass temp Customer to mysql...\n");
//        try {
//            if (isUpdatingCustomer) mysql.database.updateCustomer(cus);
//            else mysql.database.addCustomer(cus);
//        }
//        catch (SQLException e) {
//            System.out.println("SQL ERROR!!! " + e);
//        }
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    //function that runs when cancel button is hit
    @FXML
    private void handleCancelButton(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    //when the country is selected update the division list
    @FXML
    private void handleCountryChange(ActionEvent event) {
        Country selected = countryComboBox.getValue();

        firstLevelDivisionComboBox.setDisable(false);
        firstLevelDivisionComboBox.setItems(
            this.firstLevelDivisionRepository.fetchFirstLevelDivisionsByCountryId(selected.getCountryId())
        );
    }

    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public void setFirstLevelDivisionRepository(FirstLevelDivisionRepository firstLevelDivisionRepository) {
        this.firstLevelDivisionRepository = firstLevelDivisionRepository;
    }
}
