package ucf.assignments;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class ItemModel {
    ObservableList<Item> list = FXCollections.observableArrayList();

    public class Item {
        private StringProperty name;
        private StringProperty serialNumber;
        private BigDecimal price = new BigDecimal(0);

       public void setName(String value) {
           nameProperty().set(value);
       }
       public String getName() {
           return nameProperty().get();
       }
       public StringProperty nameProperty() {
           if (name == null)
               name = new SimpleStringProperty(this, "name");
           return name;
       }

        public void setSerialNumber(String value) {
            serialNumberProperty().set(value);
        }
        public String getSerialNumber() {
            return serialNumberProperty().get();
        }
        public StringProperty serialNumberProperty() {
            if (serialNumber == null)
                serialNumber = new SimpleStringProperty(this, "serialNumber");
            return serialNumber;
        }
    }
}
