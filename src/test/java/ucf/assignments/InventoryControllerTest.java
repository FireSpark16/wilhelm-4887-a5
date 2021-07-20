package ucf.assignments;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class InventoryControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {"$123.45", "123", "$123456", "123456"})
    void removeMoneySign(String testStrings) {
        InventoryController controller = new InventoryController();
        String withSignRemoved = controller.removeMoneySign(testStrings);
        char firstCharacter = withSignRemoved.charAt(0);
        assertTrue(firstCharacter != '$');
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "tEst", "TEST", "abc123", "ABC132"})
    void setToCaps(String testStrings) {
        InventoryController controller = new InventoryController();
        String allCaps = controller.setToCaps(testStrings);
        boolean check = true;
        for (int i = 0; i < allCaps.length(); i++)
            if (Character.isLowerCase(allCaps.charAt(i)))
                check = false;
        assertTrue(check);
    }

    @ParameterizedTest
    @ValueSource(strings = {"10", "999", "23.32", "856.38"})
    void priceCheckPass(String testStrings) {
        InventoryController controller = new InventoryController();
        int returnVal = controller.priceCheck(testStrings);
        assertEquals(0,returnVal);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "999.999", "123.3", "856."})
    void priceCheckFail(String testStrings) {
        InventoryController controller = new InventoryController();
        int returnVal = controller.priceCheck(testStrings);
        assertNotEquals(0,returnVal);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "A1B2C3D4E5", "STRAWBERRY"})
    void serialNumberCheckPass(String testStrings) {
        InventoryController controller = new InventoryController();
        int returnVal = controller.serialNumberCheck(testStrings);
        assertEquals(0,returnVal);
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "A1B2C3D4E5F", "STRAW TIME", "TEST-TEST-"})
    void serialNumberCheckFail(String testStrings) {
        InventoryController controller = new InventoryController();
        int returnVal = controller.serialNumberCheck(testStrings);
        assertNotEquals(0,returnVal);
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "HELLO", "How are you today?", "Hi", "12345"})
    void nameCheckPass(String testStrings) {
        InventoryController controller = new InventoryController();
        int returnVal = controller.nameCheck(testStrings);
        assertEquals(0, returnVal);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "a", "B"})
    void nameCheckFailLessThan2(String testStrings) {
        InventoryController controller = new InventoryController();
        int returnVal = controller.nameCheck(testStrings);
        assertNotEquals(0, returnVal);
    }

    @Test
    void nameCheckFailLessOver256() {
        InventoryController controller = new InventoryController();
        String testString = "";
        for (int i = 0; i <= 257; i++)
            testString += "a";
        int returnVal = controller.nameCheck(testString);
        assertNotEquals(0, returnVal);
    }

    @Test
    void addItemPass() {
        InventoryController controller = new InventoryController();
        boolean returnVal = controller.addItem("122.22", "HELLO12345", "Test");
        assertTrue(returnVal);
        assertTrue(controller.itemModel.list.get(0).getName().equals("Test"));
    }

    @Test
    void addItemFail() {
        InventoryController controller = new InventoryController();
        for (int i = 0; i < 100; i++)
        {
            String serialNumber = String.valueOf(i);
            for (int j = serialNumber.length(); j < 10; j++)
                serialNumber += "a";
            controller.addItem("122.2", serialNumber, "Test");
        }
        boolean returnVal = controller.addItem("122.22", "HELLO12345", "Test100");
        assertFalse(returnVal);
        assertFalse(controller.itemModel.list.get(0).getName().equals("Test100"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "5", "20", "30"})
    void listSize(String testStrings) {
        InventoryController controller = new InventoryController();
        for (int i = 0; i < Integer.valueOf(testStrings); i++)
        {
            String serialNumber = String.valueOf(i);
            for (int j = serialNumber.length(); j < 10; j++)
                serialNumber += "a";
            controller.addItem("122.2", serialNumber, "Test");
        }
        int size = controller.listSize();
        assertEquals(Integer.valueOf(testStrings), size);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "5", "20", "30"})
    void createNewList(String testStrings) {
        InventoryController controller = new InventoryController();
        for (int i = 0; i < Integer.valueOf(testStrings); i++)
        {
            String serialNumber = String.valueOf(i);
            for (int j = serialNumber.length(); j < 10; j++)
                serialNumber += "a";
            controller.addItem("122.2", serialNumber, "Test");
        }
        controller.createNewList();
        int size = controller.listSize();
        assertEquals(0, size);
    }
}