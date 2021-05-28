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
    private TextField Customer_ID;

    @FXML
    private TextField Customer_Name;

    @FXML
    private TextField Customer_Add;

    @FXML
    private TextField Customer_Post;

    @FXML
    private TextField Customer_Phone;

    @FXML
    private ComboBox<FirstLevelDivision> firstLevelDivisionComboBox;

    @FXML
    private ComboBox<Country> countryComboBox;

    private boolean update = false;

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

    //This function is used to prefill the form and set it to update

    /**
     * Prefills the customer menu to allow an user to edit a customer
     * @param cus the customer object to fill the table
     */
    public void prefill (Customer cus) {
        update = true;

        Customer_ID.setText(String.valueOf(cus.getCustomerId()));
        Customer_Name.setText(cus.getCustomerName());
        Customer_Add.setText(cus.getAddress());
        Customer_Post.setText(cus.getPostalCode());
        Customer_Phone.setText(cus.getPhone());
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
    private void customerSubmit(ActionEvent event) {
        System.out.print("Submit button hit!\ncreating temp customer...");
        int tempID = 0;
        if (update) tempID = Integer.parseInt(Customer_ID.getText());
        String tempName = Customer_Name.getText();
        String tempAdd = Customer_Add.getText();
        String tempPost = Customer_Post.getText();
        String tempPhone = Customer_Phone.getText();
        FirstLevelDivision tempDiv = firstLevelDivisionComboBox.getValue();
        Customer cus = new Customer(tempID, tempName, tempAdd, tempPost, tempPhone, tempDiv.getDivisionId());
        System.out.print(cus.getCustomerName());
        System.out.print(" Done!\n\tPass temp Customer to mysql...\n");
//        try {
//            if (update) mysql.database.updateCustomer(cus);
//            else mysql.database.addCustomer(cus);
//        }
//        catch (SQLException e) {
//            System.out.println("SQL ERROR!!! " + e);
//        }
        System.out.print("MySQL Done!\n");
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    //function that runs when cancel button is hit
    @FXML
    private void customerCancel(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    //when the country is seleced update the division list
    @FXML
    private void CountryChange(ActionEvent event) {
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
