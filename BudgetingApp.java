package budgeting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class BudgetingApp extends Application {
    private static final Logger logger = LogManager.getLogger(BudgetingApp.class);
    private final BillManager billManager = new BillManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Input fields for adding a bill
        TextField nameField = new TextField();
        nameField.setPromptText("Bill Name");
        nameField.setPrefWidth(200); // Width for field

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Credit Card", "Loan", "Utilities",
                                      "Recurring", "Annual", "Insurance",
                                      "Other");
        typeComboBox.setPromptText("Select Type");
        typeComboBox.setPrefWidth(200);
        typeComboBox.setEditable(true); // Allow custom entries

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setPrefWidth(200); // Width for amount

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPrefWidth(200); // Width for date
        Button addBillButton = new Button("Add Bill");
        addBillButton.setPrefWidth(200); // Width for button

        TextField typeField = new TextField();
        typeField.setPromptText("Type");
        typeField.setPrefWidth(200);

        // Button for getting bills due
        DatePicker reportDatePicker = new DatePicker();
        reportDatePicker.setPrefWidth(200); // Set width for report date picker
        Button submitButton = new Button("Get Bills");
        submitButton.setPrefWidth(200); // Set width for get bills button
        Button exportButton = new Button("Export to Excel");
        exportButton.setPrefWidth(200); // Set width for export button

        Label resultLabel = new Label();

        // TextArea for displaying bills
        TextArea billsLogArea = new TextArea();
        billsLogArea.setEditable(false); // Make the text area read-only
        billsLogArea.setPrefHeight(200); // Set height for the text area

        // Action for adding a bill
        addBillButton.setOnAction(e -> {
            String name = nameField.getText();
            String amountText = amountField.getText();
            LocalDate dueDate = dueDatePicker.getValue();
            String type = typeComboBox.getValue(); // Get selected type

            if (!name.isEmpty() && !amountText.isEmpty() && dueDate != null) {
                try {
                    double amount = Double.parseDouble(amountText);
                    Bill newBill = new Bill(name, amount, dueDate, type);
                    billManager.addBill(newBill);

                    if (!typeComboBox.getItems().contains(type)) {
                        typeComboBox.getItems().add(type);
                    }
                    //Clear Fields
                    nameField.clear();
                    amountField.clear();
                    dueDatePicker.setValue(null);
                    typeComboBox.setValue(null);
                    resultLabel.setText("Bill added successfully!");



                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid amount format.");
                }
            } else {
                resultLabel.setText("Please fill in all fields.");
            }
        });

        // Action for getting bills due
        submitButton.setOnAction(e -> {
            LocalDate selectedDate = reportDatePicker.getValue();
            if (selectedDate != null) {
                List<Bill> billsDue = billManager.getBillsDue(selectedDate);
                StringBuilder billDetails = new StringBuilder("Bills Due:\n");
                for (Bill bill : billsDue) {
                    billDetails.append(bill.toString()).append("\n");
                }
                billsLogArea.setText(billDetails.toString());
            } else {
                resultLabel.setText("Please select a date.");
            }
        });

        // Action for exporting to Excel
        exportButton.setOnAction(e -> {
            LocalDate selectedDate = reportDatePicker.getValue();
            if (selectedDate != null) {
                BillExporter.exportBills(billManager.getBillsDue(selectedDate));
                resultLabel.setText("Bills exported to Excel.");
            } else {
                resultLabel.setText("Please select a date.");
            }
        });

        // Layout
        VBox vbox = new VBox(10, new Label("Add Bill:"), nameField, typeComboBox, amountField, dueDatePicker, addBillButton,
                new Label("Select Date for Report:"), reportDatePicker, submitButton, exportButton, resultLabel, billsLogArea);
        Scene scene = new Scene(vbox, 400, 600);
        primaryStage.setTitle("Budgeting Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
