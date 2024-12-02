package budgeting;

import java.time.LocalDate;

public class Bill {
    private final String name;
    private final double amount;
    private final LocalDate dueDate;
    private String type;
    private String recurringInterval; // Assuming this is a String, could be modified as needed

    public Bill(String name, double amount, LocalDate dueDate, String type) {
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
        this.type = type;
        this.recurringInterval = recurringInterval; // Initialize the recurring interval
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s: $%.2f (Due: %s)", name, amount, dueDate, type);
    }
    public String getRecurringInterval() {
        return recurringInterval; // Add this getter method
    }
}
