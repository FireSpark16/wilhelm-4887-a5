package ucf.assignments;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.util.Locale;

public class InventoryController {

    ItemModel itemModel = new ItemModel();

    // FXML Components
    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TableColumn<?, ?> serialNumberColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> deleteColumn;

    @FXML
    private TextField messageBox;

    @FXML
    private TextField itemPriceBox;

    @FXML
    private TextField itemSerialNumberBox;

    @FXML
    private TextField itemNameBox;

    @FXML
    void addItemClicked() {
        String price = removeMoneySign(getItemPriceBox());
        String serialNumber = setToCaps(getItemSerialNumberBox());
        String name = getItemNameBox();

        int priceCheck = priceCheck(price);
        int serialNumberCheck = serialNumberCheck(serialNumber);
        int nameCheck = nameCheck(name);
        if (priceCheck == 0 && serialNumberCheck == 0 && nameCheck == 0) {
            boolean addCheck = addItem(price, serialNumber, name);
            if (addCheck == true)
                setMessageBox("Item successfully added. ");
            else
                setMessageBox("Too many items in list. You can only hold 100 items.");
        } else {
            String errorCode = addItemErrorCode(priceCheck, serialNumberCheck, nameCheck);
            setMessageBox(errorCode);
        }
    }

    @FXML
    void newButtonClicked(ActionEvent event) {
        createNewList();
    }

    @FXML
    void loadButtonClicked(ActionEvent event) {

    }

    @FXML
    void saveButtonClicked(ActionEvent event) {

    }

    // Getter and Setter Methods
    private String getMessageBox() {
        return messageBox.getText();
    }

    private void setMessageBox(String input) {
        messageBox.setText(input);
    }
    private String getItemPriceBox() {
        return itemPriceBox.getText();
    }

    private String getItemSerialNumberBox() {
        return itemSerialNumberBox.getText();
    }

    private String getItemNameBox() {
        return itemNameBox.getText();
    }

    // Non-FXML Testable Methods
    public String removeMoneySign(String itemPriceBox) {
        if(itemPriceBox.charAt(0) == '$')
            return itemPriceBox.substring(1);
        return itemPriceBox;
    }

    public String setToCaps(String itemSerialNumberBox) {
        return itemSerialNumberBox.toUpperCase(Locale.ROOT);
    }

    public int priceCheck(String price) {
        if (price.equals(""))
            // If empty
            return 1;
        int size = price.length();
        for (int i = 0; i < size; i++)
        {
            char currentCharacter = price.charAt(i);
            // If the character is not a digit AND is not a decimal 2 places before the end of the number
            if ((!Character.isDigit(currentCharacter)) && !(i == size - 3 && currentCharacter == '.'))
                return 2;
        }
        return 0;
    }

    public int serialNumberCheck(String serialNumber) {
        if (serialNumber.equals(""))
            // If empty
            return 1;
        int size = serialNumber.length();
        if (size == 10)
        {
            for (int i = 0; i < size; i++)
            {
                char currentCharacter = serialNumber.charAt(i);
                // If not a letter or a digit
                if(!Character.isLetterOrDigit(currentCharacter))
                    return 3;
            }
        }
        else
            // If not 10 characters
            return 2;
        for (int i = 0; i < listSize(); i++)
            // If the number is not unique
            if(serialNumber.equals(itemModel.list.get(i).getSerialNumber()))
                return 4;
        return 0;
    }

    public int nameCheck(String name) {
        if (name.equals(""))
            // If empty
            return 1;
        int nameLength = name.length();
        // If not between 2 and 256 characters
        if (!(nameLength >= 2 && nameLength <= 256))
            return 2;
        return 0;
    }

    private String addItemErrorCode(int priceCheck, int serialNumberCheck, int nameCheck) {
        String errorCode = "";
        switch (priceCheck) {
            case 1 -> errorCode += "Price is blank. ";
            case 2 -> errorCode += "Invalid price format. ";
        }
        switch (serialNumberCheck) {
            case 1 -> errorCode += "Serial number is blank. ";
            case 2 -> errorCode += "Serial number is not 10 characters. ";
            case 3 -> errorCode += "Invalid serial number format. ";
            case 4 -> errorCode += "Serial number is not unique. ";
        }
        switch (nameCheck) {
            case 1 -> errorCode += "Name is blank. ";
            case 2 -> errorCode += "Name is not between 2 and 256 characters. ";
        }
        return errorCode;
    }

    public boolean addItem(String price, String serialNumber, String name) {
        if (listSize() < 100) {
            ItemModel.Item item = itemModel.new Item();
            item.setName(name);
            item.setSerialNumber(serialNumber);
            item.setPrice(price);
            itemModel.list.add(item);
            return true;
        }
        else
            return false;
    }

    public int listSize() {
        return itemModel.list.size();
    }

    public void createNewList() {
        itemModel = new ItemModel();
    }

}

