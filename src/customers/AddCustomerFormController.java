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
import users.User;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerFormController implements Initializable {

    @FXML
    private TextField customerIdTextField;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField customerAddressTextField;

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

    private CustomerRepository customerRepository;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        firstLevelDivisionComboBox.setDisable(true);

        // Initialize repos
        this.setCountryRepository(new CountryRepository());
        this.setFirstLevelDivisionRepository(new FirstLevelDivisionRepository());
        this.setCustomerRepository(new CustomerRepository());

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
        customerAddressTextField.setText(cus.getAddress());
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
        System.out.print("Customer creation/updating started");
        var tempCustomer = new Customer();
        tempCustomer.setCustomerId(isUpdatingCustomer ? Integer.parseInt(customerIdTextField.getText()) : null);
        tempCustomer.setCustomerName(customerNameTextField.getText());
        tempCustomer.setAddress(customerAddressTextField.getText());
        tempCustomer.setPostalCode(customerPostalCodeTextField.getText());
        tempCustomer.setPhone(customerPhoneTextField.getText());
        tempCustomer.setDivisionId(firstLevelDivisionComboBox.getValue().getDivisionId());

        this.customerRepository.createOrUpdateCustomer(tempCustomer);

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

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
