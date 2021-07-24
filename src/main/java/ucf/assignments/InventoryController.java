package ucf.assignments;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {


    ItemModel itemModel = new ItemModel();
    ItemModel.Item selected = null;

    // FXML Components
    @FXML
    private TableView table;

    @FXML
    private TableColumn<ItemModel.Item, String> priceColumn;

    @FXML
    private TableColumn<ItemModel.Item, String> serialNumberColumn;

    @FXML
    private TableColumn<ItemModel.Item, String> nameColumn;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab addTab;

    @FXML
    private Tab editTab;

    @FXML
    private Tab deleteTab;

    @FXML
    private TextField messageBox;

    @FXML
    private TextField searchBox;

    @FXML
    private TextField priceAddBox;

    @FXML
    private TextField serialNumberAddBox;

    @FXML
    private TextField nameAddBox;

    @FXML
    private TextField priceEditBox;

    @FXML
    private TextField serialNumberEditBox;

    @FXML
    private TextField nameEditBox;

    @FXML
    private TextField priceDeleteBox;

    @FXML
    private TextField serialNumberDeleteBox;

    @FXML
    private TextField nameDeleteBox;

    @FXML
    void addItemClicked() {
        String price = fixPrice(getPriceAddBox());
        String serialNumber = setToCaps(getSerialNumberAddBox());
        String name = getNameAddBox();

        int priceCheck = priceCheck(price);
        int serialNumberCheck = serialNumberCheck(serialNumber);
        int nameCheck = nameCheck(name);
        if (priceCheck == 0 && serialNumberCheck == 0 && nameCheck == 0) {
            boolean addCheck = addItem(price, serialNumber, name);
            if (addCheck == true) {
                setMessageBox("Item successfully added.");
                // table.getItems().add(itemModel.list.get(itemModel.list.size() - 1));
                clearAddBoxes();
                updateTabs();
            }
            else
                setMessageBox("Too many items in list. You can only hold 100 items.");
        } else {
            String errorCode = itemErrorCode(priceCheck, serialNumberCheck, nameCheck);
            setMessageBox(errorCode);
        }
    }

    @FXML
    void deleteItemClicked(ActionEvent event) {
        deleteItem();
        setMessageBox("Item successfully deleted.");
        deselect();
        updateTabs();
    }

    @FXML
    void editItemClicked(ActionEvent event) {
        String price = fixPrice(getPriceEditBox());
        String serialNumber = setToCaps(getSerialNumberEditBox());
        String name = getNameEditBox();

        int priceCheck = priceCheck(price);
        int serialNumberCheck = serialNumberCheck(serialNumber);
        int nameCheck = nameCheck(name);

        if (serialNumberCheck == 4)
            serialNumberCheck = 0;

        if (priceCheck == 0 && (serialNumberCheck == 0 || serialNumberCheck == 4) && nameCheck == 0) {
            editItem(price, serialNumber, name);
            setMessageBox("Item successfully edited.");
            deselect();
            updateTabs();
        } else {
            String errorCode = itemErrorCode(priceCheck, serialNumberCheck, nameCheck);
            setMessageBox(errorCode);
        }
    }

    @FXML
    void newButtonClicked(ActionEvent event) {
        createNewList();
        setMessageBox("New list created.");
        deselect();
        updateTabs();
    }

    @FXML
    void loadButtonClicked(ActionEvent event) {

    }

    @FXML
    void saveButtonClicked(ActionEvent event) {

    }

    // Initialize the Table

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        FilteredList<ItemModel.Item> filteredList = new FilteredList<>(itemModel.list, b -> true);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(itemModel -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(itemModel.getName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(itemModel.getPrice()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(itemModel.getSerialNumber()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<ItemModel.Item> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }
    // FXML Support Methods

    public void updateSelected(MouseEvent mouseEvent) {
        selected = (ItemModel.Item) table.getSelectionModel().getSelectedItem();
        setMessageBox("\"" + selected.getName() + "\" selected.");
        updateTabs();
    }

    private void updateTabs() {
        if (itemModel.list.size() >= 1) {
            searchBox.setDisable(false);
        }
        else
            searchBox.setDisable(true);
        if (selected != null) {
            editTab.setDisable(false);
            deleteTab.setDisable(false);
            priceEditBox.setText(selected.getPrice());
            serialNumberEditBox.setText(selected.getSerialNumber());
            nameEditBox.setText(selected.getName());
            priceDeleteBox.setText(selected.getPrice());
            serialNumberDeleteBox.setText(selected.getSerialNumber());
            nameDeleteBox.setText(selected.getName());
        } else {
            editTab.setDisable(true);
            deleteTab.setDisable(true);
            tabPane.getSelectionModel().select(addTab);
            clearEditBoxes();
            clearDeleteBoxes();
        }
        table.refresh();
    }

    private void clearAddBoxes() {
        priceAddBox.setText("");
        serialNumberAddBox.setText("");
        nameAddBox.setText("");
    }

    private void clearEditBoxes() {
        priceEditBox.setText("");
        serialNumberEditBox.setText("");
        nameEditBox.setText("");
    }

    private void clearDeleteBoxes() {
        priceDeleteBox.setText("");
        serialNumberDeleteBox.setText("");
        nameDeleteBox.setText("");
    }

    private void setMessageBox(String input) {
        messageBox.setText(input);
    }

    private String getPriceAddBox() {
        return priceAddBox.getText();
    }

    private String getSerialNumberAddBox() {
        return serialNumberAddBox.getText();
    }

    private String getNameEditBox() {
        return nameEditBox.getText();
    }

    private String getPriceEditBox() {
        return priceEditBox.getText();
    }

    private String getSerialNumberEditBox() {
        return serialNumberEditBox.getText();
    }

    private String getNameAddBox() {
        return nameAddBox.getText();
    }

    // Non-FXML Testable Methods
    public void editItem(String price, String serialNumber, String name) {
        itemModel.list.get(itemModel.list.indexOf(selected)).setPrice(price);
        itemModel.list.get(itemModel.list.indexOf(selected)).setSerialNumber(serialNumber);
        itemModel.list.get(itemModel.list.indexOf(selected)).setName(name);
    }

    public void deleteItem() {
        itemModel.list.remove(selected);
    }

    public void deselect() {
        selected = null;
    }

    public String fixPrice(String itemPriceBox) {
        String newPrice = itemPriceBox;
        if (newPrice != null && !newPrice.equals("")) {
            if (itemPriceBox.charAt(0) == '$')
                // Remove dollar sign
                newPrice = newPrice.substring(1);
            if (newPrice.length() < 4 || newPrice.charAt(newPrice.length() - 3) != '.')
                // Add cent amount
                newPrice += ".00";
        }
        return newPrice;
    }

    public String setToCaps(String itemSerialNumberBox) {
        return itemSerialNumberBox.toUpperCase(Locale.ROOT);
    }

    public int priceCheck(String price) {
        if (price.isEmpty() || price.equals(""))
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
        if (serialNumber.isEmpty() || serialNumber.equals(""))
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
        if (name.isEmpty() || name.equals(""))
            // If empty
            return 1;
        int nameLength = name.length();
        // If not between 2 and 256 characters
        if (!(nameLength >= 2 && nameLength <= 256))
            return 2;
        return 0;
    }

    private String itemErrorCode(int priceCheck, int serialNumberCheck, int nameCheck) {
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
        int size = listSize();
        itemModel.list.remove(0,size);
    }
}

