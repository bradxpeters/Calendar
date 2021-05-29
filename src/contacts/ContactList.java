package contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ContactList {
    private static ContactList INSTANCE;
    private static ObservableList<Contact> allContacts;

    private ContactList() {
    }

    public static ContactList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContactList();
        }

        return INSTANCE;
    }

    public ObservableList<Contact> getContactList() {
        if (allContacts == null) {
            allContacts = FXCollections.observableList(new ArrayList<>());

            var contactRepository = new ContactRepository();
            contactRepository.fetchAll();
        }

        return allContacts;
    }

    public void setAllCustomers(ObservableList<Contact> contacts) {
        allContacts = contacts;
    }
}
