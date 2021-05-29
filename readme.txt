World Calender

Purpose: A global scheduling application to track appointments for customers and contacts across the world.

Author: Bradley Peters
Contact: bpet202@wgu.edu

Application Version 1.0
Date: 05-29-2021

Environment:
IDE: IntelliJ Ultimate 2020.3)
JDK: jdk-11.0.9
JavaFX: JavaFX-SDK-15.0.1
MYSQL Connector:mysql-connector-java:8.0.22

How To:
- Open the project in IntelliJ.
- Ensure you have a runtime configuration for JavaFX.
    - Java 11 SDK
    - Build Arguments: --module-path ${PATH_TO_FX} --add-modules=javafx.fxml,javafx.controls,javafx.graphics
    - Target: main.Main
- On the login screen - login using the username and password ("test", "test")
- Tabs
    - Appointments Tab: Create and Update appointments using the buttons in the bottom left.
    - Customers: Create and Update customers using the buttons in the bottom left.
    - Remaining tabs: Various reports (updated in real time when data changes are made)
        -Appointments Summary: Appointments grouped by Type, Month, and Year
        -Contact Summary: This shows all appointments listed out by contacts
        -Customer Locations(Customer Report): This report shows a list of customers by location.