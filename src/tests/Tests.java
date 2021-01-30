package tests;

import users.User;
import users.UserRepository;

import java.sql.SQLException;

public class Tests {

    public void runAll() {
        testFetchUserById();
    }

    public void testFetchUserById() {

        System.out.println("Testing fetching user by id...");

        var testId = 2;
        var userRepo = new UserRepository();
        User testUser = null;

        try {
            testUser = userRepo.fetchUserById(testId);
        } catch (SQLException exception) {
            System.out.println("testFetchUserById failed");
            exception.printStackTrace();
        }

        if (testUser == null) {
            System.out.println("No user found with id " + testId);
        }

        System.out.println("testFetchUserById passed!");
    }
}
