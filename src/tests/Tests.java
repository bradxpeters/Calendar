package tests;

import firstLevelDivisions.FirstLevelDivision;
import firstLevelDivisions.FirstLevelDivisionRepository;
import users.User;
import users.UserRepository;

import java.sql.SQLException;

public class Tests {

    public void runAll() {
        testFetchUserById();
        testFetchFirstLevelDivisionById();
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
        } else {
            System.out.println(testUser.toString());
            System.out.println("testFetchUserById passed!");
        }
    }

    public void testFetchFirstLevelDivisionById() {

        System.out.println("Testing FirstLevelDivision by id...");

        var testId = 10;
        var fldRepo = new FirstLevelDivisionRepository();
        FirstLevelDivision testFld = null;

        testFld = fldRepo.fetchFirstLevelDivisionById(testId);

        if (testFld == null) {
            System.out.println("No FirstLevelDivision found with id " + testId);
        } else {
            System.out.println(testFld.toString());
            System.out.println("FirstLevelDivision passed!");
        }
    }
}
