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
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
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
    private ComboBox<FirstLevelDivision> Customer_Div;
    @FXML
    private ComboBox<Country> Customer_Country;

    private boolean update = false;

    private CountryRepository countryRepository;

    private FirstLevelDivisionRepository firstLevelDivisionRepository;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Customer_Div.setDisable(true);

        this.setCountryRepository(new CountryRepository());
        this.setFirstLevelDivisionRepository(new FirstLevelDivisionRepository());

        Customer_Country.setItems(this.countryRepository.fetchAll());
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
        Customer_Country.setValue(
            this.countryRepository.fetchCountryByDivisionId(cus.getDivisionId())
        );
        Customer_Div.setValue(
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
        FirstLevelDivision tempDiv = Customer_Div.getValue();
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
        Country selected = Customer_Country.getValue();
        Customer_Div.setDisable(false);
        Customer_Div.setItems(
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
