package ucf.assignments;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class InventoryControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {"$123.45", "123", "$123456", "123456", "123.45"}) // different tests
    void fixPrice(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        String withSignRemoved = controller.fixPrice(testStrings); // run fixPrice
        char firstCharacter = withSignRemoved.charAt(0); // get first character
        char decimalPlace = withSignRemoved.charAt(withSignRemoved.length() - 3); // get 3rd to last character
        assertTrue(firstCharacter != '$'); // test if first character is $
        assertTrue(decimalPlace == '.'); // test if there is decimal place in right spot
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "tEst", "TEST", "abc123", "ABC132"}) // different tests
    void setToCaps(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        String allCaps = controller.setToCaps(testStrings); // run setToCaps
        boolean check = true; // set a check to true
        for (int i = 0; i < allCaps.length(); i++) // for every value in allCaps
            if (Character.isLowerCase(allCaps.charAt(i))) // check if it is lowercase
                check = false; // set a check to false
        assertTrue(check); // test that the check is still true
    }

    @ParameterizedTest
    @ValueSource(strings = {"10", "999", "23.32", "856.38"}) // different tests
    void priceCheckPass(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        int returnVal = controller.priceCheck(testStrings); // run priceCheck
        assertEquals(0,returnVal); // test that 0 was returned (meaning no errors)
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "999.999", "123.3", "856."}) // different tests
    void priceCheckFail(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        int returnVal = controller.priceCheck(testStrings); // run priceCheck
        assertNotEquals(0,returnVal); // test that 0 was not returned (meaning there were errors)
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "A1B2C3D4E5", "STRAWBERRY"}) // different tests
    void serialNumberCheckPass(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        int returnVal = controller.serialNumberCheck(testStrings); // run serialNumberCheck
        assertEquals(0,returnVal); // test that 0 was returned (meaning no errors)
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "A1B2C3D4E5F", "STRAW TIME", "TEST-TEST-"}) // different tests
    void serialNumberCheckFail(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        int returnVal = controller.serialNumberCheck(testStrings); // run serialNumberCheck
        assertNotEquals(0,returnVal); // test that 0 was not returned (meaning there were errors)
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "HELLO", "How are you today?", "Hi", "12345"}) // different tests
    void nameCheckPass(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        int returnVal = controller.nameCheck(testStrings); // run nameCheck
        assertEquals(0,returnVal); // test that 0 was returned (meaning no errors)
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "a", "B"}) // different tests
    void nameCheckFailLessThan2(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        int returnVal = controller.nameCheck(testStrings); // run nameCheck
        assertNotEquals(0,returnVal); // test that 0 was not returned (meaning there were errors)
    }

    @Test
    void nameCheckFailLessOver256() {
        InventoryController controller = new InventoryController(); // create new controller
        String testString = ""; // create blank test string
        for (int i = 0; i <= 257; i++) // add 257 of 'a' to the string to make it bigger than 256
            testString += "a";
        int returnVal = controller.nameCheck(testString); // run nameCheck
        assertNotEquals(0,returnVal); // test that 0 was not returned (meaning there were errors)
    }

    @Test
    void addItemPass() {
        InventoryController controller = new InventoryController(); // create new controller
        boolean returnVal = controller.addItem("122.22", "HELLO12345", "Test"); // run addItem
        assertTrue(returnVal); // test that addItem returned true (no errors)
        assertTrue(controller.itemModel.list.get(0).getName().equals("Test")); //test that there is an item in the list
    }

    @Test
    void addItemFail() {
        InventoryController controller = new InventoryController(); // create new controller
        for (int i = 0; i < 100; i++) // add 100 items with different serial numbers
        {
            String serialNumber = String.valueOf(i);
            for (int j = serialNumber.length(); j < 10; j++)
                serialNumber += "a";
            controller.addItem("122.2", serialNumber, "Test");
        }
        boolean returnVal = controller.addItem("122.22", "HELLO12345", "Test100"); // run addItem
        assertFalse(returnVal); // test that addItem returned false (no more available space for items)
        assertFalse(controller.itemModel.list.get(0).getName().equals("Test100")); // test that an item was not added
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "5", "20", "30"}) // different tests
    void listSize(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        for (int i = 0; i < Integer.valueOf(testStrings); i++) // add different amounts of items based on testStrings
        {
            String serialNumber = String.valueOf(i);
            for (int j = serialNumber.length(); j < 10; j++)
                serialNumber += "a";
            controller.addItem("122.2", serialNumber, "Test"); // add items to list
        }
        int size = controller.listSize(); // run listSize
        assertEquals(Integer.valueOf(testStrings), size); // test that size equals the testString parameters
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "5", "20", "30"}) // different tests
    void createNewList(String testStrings) {
        InventoryController controller = new InventoryController(); // create new controller
        for (int i = 0; i < Integer.valueOf(testStrings); i++) // add a few items to the list
        {
            String serialNumber = String.valueOf(i);
            for (int j = serialNumber.length(); j < 10; j++)
                serialNumber += "a";
            controller.addItem("122.2", serialNumber, "Test");
        }
        controller.createNewList(); // run createNewList
        int size = controller.listSize(); // get the size of the new list
        assertEquals(0, size); // test that the size is 0
    }

    @Test
    void saveAsTsv() {
        InventoryController controller = new InventoryController(); // create new controller
        controller.addItem("24","1234567890","Item 1");  // add an item to the list 
        controller.addItem("36.24", "0987654321", "Item 2");  // add another item to the list
        controller.saveAsTsv(new File("./testTxt1.txt")); // run saveAsTsv
        File newFile = new File("./testTxt1.txt"); // get file from file path
        assertTrue(newFile.exists()); // test that file exists
        boolean deleted = newFile.delete(); // delete the file and get the return value
        assertTrue(deleted); // test that the file was deleted
    }

    @Test
    void loadAsTsv() {
        InventoryController controller = new InventoryController(); // create new controller
        controller.addItem("24","1234567890","Item 1");  // add an item to the list
        controller.addItem("36.24", "0987654321", "Item 2");  // add another item to the list
        controller.saveAsTsv(new File("./testTxt2.txt")); // run saveAsTsv
        File newFile = new File("./testTxt2.txt"); // get file from file path
        boolean deleted = newFile.delete(); // delete the file and get the return value
        assertTrue(deleted); // test that the file was deleted
        controller.loadAsTsv(newFile); // run loadAsTsv
        assertTrue(controller.itemModel.list.size() != 0); // test that the list is not empty
        assertTrue(controller.itemModel.list.get(0).getName().equals("Item 1")); // test that there is an item in the list
    }

    @Test
    void saveAsHtml() {
        InventoryController controller = new InventoryController(); // create new controller
        controller.addItem("24","1234567890","Item 1");  // add an item to the list
        controller.addItem("36.24", "0987654321", "Item 2");  // add another item to the list
        controller.saveAsHtml(new File("./testHtml1.html")); // run saveAsHtml
        File newFile = new File("./testHtml1.html"); // get file from file path
        assertTrue(newFile.exists()); // test that file exists
        boolean deleted = newFile.delete(); // delete the file and get the return value
        assertTrue(deleted); // test that the file was deleted
    }

    @Test
    void loadAsHtml() {
        InventoryController controller = new InventoryController(); // create new controller
        controller.addItem("24","1234567890","Item 1");  // add an item to the list
        controller.addItem("36.24", "0987654321", "Item 2");  // add another item to the list
        controller.saveAsHtml(new File("./testHtml2.html")); // run saveAsHtml
        File newFile = new File("./testHtml2.html"); // get file from file path
        boolean deleted = newFile.delete(); // delete the file and get the return value
        assertTrue(deleted); // test that the file was deleted
        controller.loadAsHtml(newFile); // run loadAsHtml
        assertTrue(controller.itemModel.list.size() != 0); // test that the list is not empty
        assertTrue(controller.itemModel.list.get(0).getName().equals("Item 1")); // test that there is an item in the list
    }

    @Test
    void saveAsJson() {
        InventoryController controller = new InventoryController(); // create new controller
        controller.addItem("24","1234567890","Item 1");  // add an item to the list
        controller.addItem("36.24", "0987654321", "Item 2");  // add another item to the list
        controller.saveAsJson(new File("./testJson1.json")); // run saveAsJson
        File newFile = new File("./testJson1.json"); // get file from file path
        assertTrue(newFile.exists()); // test that file exists
        boolean deleted = newFile.delete(); // delete the file and get the return value
        assertTrue(deleted); // test that the file was deleted
    }

    @Test
    void loadAsJson() {
        InventoryController controller = new InventoryController(); // create new controller
        controller.addItem("24","1234567890","Item 1");  // add an item to the list
        controller.addItem("36.24", "0987654321", "Item 2");  // add another item to the list
        controller.saveAsJson(new File("./testJson2.json")); // run saveAsJson
        File newFile = new File("./testJson2.json"); // get file from file path
        boolean deleted = newFile.delete(); // delete the file and get the return value
        assertTrue(deleted); // test that the file was deleted
        controller.loadAsJson(newFile); // run loadAsJson
        assertTrue(controller.itemModel.list.size() != 0); // test that the list is not empty
        assertTrue(controller.itemModel.list.get(0).getName().equals("Item 1")); // test that there is an item in the list
    }

    @Test
    void editItem() {
        InventoryController controller = new InventoryController(); // create new controller
        controller.addItem("24","1234567890","Item 1");  // add an item to the list
        controller.selected = controller.itemModel.list.get(0); // get newly added item
        controller.editItem("24","1234567890","Item 2"); // run editItem
        assertTrue(controller.itemModel.list.get(0).getName().equals("Item 2")); // test if the name was changed
    }

    @Test
    void deleteItem() {
        InventoryController controller = new InventoryController(); // create new controller
        controller.addItem("24","1234567890","Item 1");  // add an item to the list
        controller.selected = controller.itemModel.list.get(0); // get newly added item
        controller.deleteItem(); // run deleteItem
        assertTrue(controller.itemModel.list.size() == 0); // test if item was deleted
    }

    @Test
    void deselect() {
        InventoryController controller = new InventoryController(); // create new controller
        controller.addItem("24","1234567890","Item 1");  // add an item to the list
        controller.selected = controller.itemModel.list.get(0); // select newly added item
        controller.deselect(); // run deselect
        assertTrue(controller.selected == null); // test that nothing is selected
    }

    @Test
    void itemErrorCode1() {
        InventoryController controller = new InventoryController(); // create new controller
        String errorCode = ""; // new blank error code
        errorCode = controller.itemErrorCode(1,1,1); // run itemErrorCode
        String expected = "Price is blank. Serial number is blank. Name is blank. "; // based on inputs, this is what the error code should be
        assertEquals(expected,errorCode); // test that the expected error code and errorCode match
    }

    @Test
    void itemErrorCode2() {
        InventoryController controller = new InventoryController(); // create new controller
        String errorCode = ""; // new blank error code
        errorCode = controller.itemErrorCode(2,3,2); // run itemErrorCode
        String expected = "Invalid price format. Invalid serial number format. Name is not between 2 and 256 characters. "; // based on inputs, this is what the error code should be
        assertEquals(expected,errorCode); // test that the expected error code and errorCode match
    }
}
