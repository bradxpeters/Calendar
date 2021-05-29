package contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * The type Contact list.
 */
public class ContactList {
    private static ContactList INSTANCE;
    private static ObservableList<Contact> allContacts;

    private ContactList() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ContactList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContactList();
        }

        return INSTANCE;
    }

    /**
     * Gets contact list.
     *
     * @return the contact list
     */
    public ObservableList<Contact> getContactList() {
        if (allContacts == null) {
            allContacts = FXCollections.observableList(new ArrayList<>());

            var contactRepository = new ContactRepository();
            contactRepository.fetchAll();
        }

        return allContacts;
    }

    /**
     * Sets all customers.
     *
     * @param contacts the contacts
     */
    public void setAllCustomers(ObservableList<Contact> contacts) {
        allContacts = contacts;
    }
}
