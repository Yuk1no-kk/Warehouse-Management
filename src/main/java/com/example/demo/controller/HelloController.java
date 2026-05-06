package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.DataInitializer;
import com.example.demo.service.Warehouse;
import com.example.demo.model.*;
import com.example.demo.service.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

public class HelloController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortFieldCombo;
    @FXML private ComboBox<String> sortOrderCombo;
    @FXML private ProgressBar capacityProgressBar;
    @FXML private Label capacityText;
    @FXML private TableView<Item> itemTableView;
    @FXML private TableColumn<Item, String> nameCol;
    @FXML private TableColumn<Item, String> typeCol;
    @FXML private TableColumn<Item, Double> weightCol;
    @FXML private TableColumn<Item, String> statusCol;
    @FXML private TableColumn<Item, String> specialCol;
    @FXML private ComboBox<String> itemTypeCombo;
    @FXML private TextField nameField;
    @FXML private TextField weightField;
    @FXML private Label specialLabel;
    @FXML private TextField specialField;
    @FXML private TextField durabilityField;
    @FXML private Button addBtn;
    @FXML private Button useBtn;
    @FXML private Button removeBtn;
    @FXML private Button saveBtn;
    @FXML private Button loadBtn;


    private final Warehouse warehouse = new Warehouse(100.0);
    private FilteredList<Item> filteredList;


    @FXML
    public void initialize() {

        bindTableColumns();

        bindSearchAndSort();

        bindItemTypeChange();

        refreshCapacity();
        DataInitializer.initializeWithSampleItems(warehouse);
        // 初始化后刷新表格和容量
        refreshCapacity();
        itemTableView.refresh();
    }


    private void bindTableColumns() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        // bind
        statusCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDurabilityOrExpiry())
        );
        // special intensely bind to support dynamic updates for Gun's bullets and Consumable's consumed status
        specialCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSpecialAttribute())
        );

        // put initial data into table with filtering and sorting support
        filteredList = new FilteredList<>(warehouse.getItemList(), p -> true);
        SortedList<Item> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(itemTableView.comparatorProperty());
        itemTableView.setItems(sortedList);
    }

    @FXML
    public void onAddItem() {
        try {

            String type = itemTypeCombo.getValue();
            String name = nameField.getText().trim();
            double weight = Double.parseDouble(weightField.getText().trim());
            String specialValue = specialField.getText().trim();
            String durabilityStr = durabilityField.getText().trim();


            if (type == null || name.isBlank() || weight <= 0) {
                throw new IllegalArgumentException("please fill in all required fields with valid values!");
            }

            Item newItem = switch (type) {
                case "Drink", "Food" -> {
                    LocalDate expiryDate = LocalDate.parse(specialValue);
                    if (type.equals("Drink")) {
                        yield new Drink(name, weight, expiryDate);
                    } else {
                        yield new Food(name, weight, expiryDate);
                    }
                }
                case "Gun" -> {
                    int bullets = Integer.parseInt(specialValue);
                    int durability = Integer.parseInt(durabilityStr);
                    if (bullets < 0 || durability < 0 || durability > 100) {
                        throw new IllegalArgumentException("bullets must be >= 0 and durability must be between 0 and 100");
                    }
                    yield new Gun(name, weight, durability, bullets);
                }
                case "Bomb" -> {
                    int durability = Integer.parseInt(durabilityStr);
                    if (durability < 0 || durability > 100) {
                        throw new IllegalArgumentException("durability must be between 0 and 100");
                    }
                    yield new Bomb(name, weight, durability);
                }
                default -> throw new IllegalArgumentException("unsupported item type: " + type);
            };

            warehouse.addItem(newItem);
            clearForm();
            refreshCapacity();
            showAlert(Alert.AlertType.INFORMATION, "success", "item added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "error", "weight, bullets and durability must be valid numbers!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "failed", e.getMessage());
        }
    }

    @FXML
    public void onUseItem() {
        Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "warning", "please select an item in the table to use!");
            return;
        }

        try {
            if (selectedItem instanceof Consumable consumable) {
                consumable.consume();
            } else if (selectedItem instanceof Weapon weapon) {
                weapon.use();
                if (weapon.getDurability() <= 0) {
                    warehouse.removeItem(selectedItem);
                    showAlert(Alert.AlertType.INFORMATION, "warning", "weapon is broken and removed from warehouse!");
                }
            }
            itemTableView.refresh();
            refreshCapacity();
            showAlert(Alert.AlertType.INFORMATION, "success", "item used successfully!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "failed", e.getMessage());
        }
    }


    private void bindSearchAndSort() {

        Runnable sortAction = () -> {
            String sortField = sortFieldCombo.getValue();
            boolean isAsc = "upper".equals(sortOrderCombo.getValue());

            Comparator<Item> comparator = switch (sortField) {
                case "Name" -> Comparator.comparing(Item::getName);
                case "Weight" -> Comparator.comparing(Item::getWeight);
                case "Itemtype" -> Comparator.comparing(Item::getType);
                default -> Comparator.comparing(Item::getName);
            };

            if (!isAsc) {
                comparator = comparator.reversed();
            }

            itemTableView.setItems(FXCollections.observableArrayList(
                    filteredList.sorted(comparator)
            ));
        };

        sortFieldCombo.setOnAction(e -> sortAction.run());
        sortOrderCombo.setOnAction(e -> sortAction.run());


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(item -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getName().toLowerCase().contains(lowerCaseFilter);
            });
            sortAction.run();
        });
    }


    @FXML
    public void onRemoveItem() {
        // 获取选中的物品 get selected item
        Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "warning", "please select an item in the table to remove!");
            return;
        }

        // 二次确认 confirm
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("confirmation");
        confirmAlert.setHeaderText("make sure to remove[" + selectedItem.getName() + "]？");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // 3. remove
                warehouse.removeItem(selectedItem);
                // 4. reload
                refreshCapacity();
                showAlert(Alert.AlertType.INFORMATION, "success", "item removed successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "failed", e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void refreshCapacity() {
        double usage = warehouse.getCapacityUsage();
        capacityProgressBar.setProgress(Math.min(usage, 1.0));
        capacityText.setText(String.format("%.2f/%.2f kg", warehouse.getCurrentWeight(), warehouse.getMaxWeight()));
        if (usage >= 0.9) {
            capacityProgressBar.setStyle("-fx-accent: red;");
        } else if (usage >= 0.7) {
            capacityProgressBar.setStyle("-fx-accent: orange;");
        } else {
            capacityProgressBar.setStyle("-fx-accent: green;");
        }
    }


    @FXML
    public void onSaveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("warehouse_data.txt"))) {

            writer.write(warehouse.getMaxWeight() + "\n");

            for (Item item : warehouse.getItemList()) {

                String line = switch (item.getType()) {
                    case "Drink", "Food" -> String.format("%s|%s|%.2f|%s",
                            item.getType(), item.getName(), item.getWeight(),
                            ((Consumable) item).getExpiryDate().toString());
                    case "Gun" -> String.format("%s|%s|%.2f|%d|%d",
                            item.getType(), item.getName(), item.getWeight(),
                            ((Gun) item).getBullets(), ((Gun) item).getDurability());
                    case "Bomb" -> String.format("%s|%s|%.2f|%d",
                            item.getType(), item.getName(), item.getWeight(),
                            ((Bomb) item).getDurability());
                    default -> "";
                };
                if (!line.isBlank()) {
                    writer.write(line + "\n");
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "success", "saved to warehouse_data.txt");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "failed", e.getMessage());
        }
    }

    @FXML
    public void onLoadData() {
        File file = new File("warehouse_data.txt");
        if (!file.exists()) {
            showAlert(Alert.AlertType.ERROR, "failed", " warehouse_data.txt did not exist, please save data first!");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            warehouse.getItemList().clear();
            double maxWeight = Double.parseDouble(reader.readLine());
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String type = parts[0];
                String name = parts[1];
                double weight = Double.parseDouble(parts[2]);

                Item item = switch (type) {
                    case "Drink" -> new Drink(name, weight, LocalDate.parse(parts[3]));
                    case "Food" -> new Food(name, weight, LocalDate.parse(parts[3]));
                    case "Gun" -> new Gun(name, weight, Integer.parseInt(parts[4]), Integer.parseInt(parts[3]));
                    case "Bomb" -> new Bomb(name, weight, Integer.parseInt(parts[3]));
                    default -> null;
                };

                if (item != null) {
                    warehouse.getItemList().add(item);
                }
            }
            refreshCapacity();
            itemTableView.refresh();
            showAlert(Alert.AlertType.INFORMATION, "success", "success！");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "failed", "broken：" + e.getMessage());
        }
    }


    private void bindItemTypeChange() {
        itemTypeCombo.setOnAction(e -> {
            String type = itemTypeCombo.getValue();
            if (type == null) return;

            switch (type) {
                case "Drink", "Food" -> {
                    specialLabel.setText("expire date(yyyy-MM-dd)：");
                    specialField.setPromptText("like：2026-12-31");
                    durabilityField.setDisable(true);
                }
                case "Gun" -> {
                    specialLabel.setText("bullet count：");
                    specialField.setPromptText("like：30");
                    durabilityField.setDisable(false);
                }
                case "Bomb" -> {
                    specialLabel.setText("no special attribute, just durability");
                    specialField.clear();
                    specialField.setDisable(true);
                    durabilityField.setDisable(false);
                }
            }
        });
    }

    private void clearForm() {
        itemTypeCombo.setValue(null);
        nameField.clear();
        weightField.clear();
        specialField.clear();
        durabilityField.clear();
        durabilityField.setDisable(false);
        specialField.setDisable(false);
        specialLabel.setText("expired date(yyyy-MM-dd)：");
    }
}