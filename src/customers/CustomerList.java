package customers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * The type Customer list.
 */
public class CustomerList {

    private static CustomerList INSTANCE;
    private static ObservableList<Customer> allCustomers;

    private CustomerList() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static CustomerList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomerList();
        }

        return INSTANCE;
    }

    /**
     * Gets customer list.
     *
     * @return the customer list
     */
    public ObservableList<Customer> getCustomerList() {
        if (allCustomers == null) {
            allCustomers = FXCollections.observableList(new ArrayList<>());

            var customerRepository = new CustomerRepository();
            customerRepository.fetchAll();
        }

        return allCustomers;
    }

    /**
     * Sets all customers.
     *
     * @param customers the customers
     */
    public void setAllCustomers(ObservableList<Customer> customers) {
        allCustomers = customers;
    }
}
