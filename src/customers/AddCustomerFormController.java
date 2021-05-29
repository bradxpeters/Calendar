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

/**
 * The type Add customer form controller.
 */
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
     * Prefill.
     *
     * @param cus the cus
     */
    public void prefill(Customer cus) {
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

        ((Node) (event.getSource())).getScene().getWindow().hide();
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

    /**
     * Sets country repository.
     *
     * @param countryRepository the country repository
     */
    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * Sets first level division repository.
     *
     * @param firstLevelDivisionRepository the first level division repository
     */
    public void setFirstLevelDivisionRepository(FirstLevelDivisionRepository firstLevelDivisionRepository) {
        this.firstLevelDivisionRepository = firstLevelDivisionRepository;
    }

    /**
     * Sets customer repository.
     *
     * @param customerRepository the customer repository
     */
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
