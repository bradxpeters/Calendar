package customers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class CustomerList {

    private static CustomerList INSTANCE;
    private static ObservableList<Customer> allCustomers;

    private CustomerList() {
    }

    public static CustomerList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomerList();
        }

        return INSTANCE;
    }

    public ObservableList<Customer> getCustomerList() {
        if (allCustomers == null) {
            allCustomers = FXCollections.observableList(new ArrayList<>());

            var customerRepository = new CustomerRepository();
            customerRepository.fetchAll();
        }

        return allCustomers;
    }

    public void setAllCustomers(ObservableList<Customer> customers) {
        allCustomers = customers;
    }
}
