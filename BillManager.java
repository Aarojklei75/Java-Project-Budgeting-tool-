package budgeting;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillManager {
    private final List<Bill> bills;

    public BillManager() {
        bills = new ArrayList<>();
    }

    public void addBill(Bill bill) {
        bills.add(bill);
    }

    public List<Bill> getBillsDue(LocalDate date) {
        List<Bill> dueBills = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getDueDate().isEqual(date) ||
                    (bill.getDueDate().isAfter(date) && bill.getDueDate().isBefore(date.plusDays(14)))) {
                dueBills.add(bill);
            }
        }
        return dueBills;
    }

    public void exportToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bills");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Amount");
        headerRow.createCell(2).setCellValue("Due Date");
        headerRow.createCell(3).setCellValue("Recurring Interval");

        // Populate rows with bill data
        int rowNum = 1;
        for (Bill bill : bills) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(bill.getName());
            row.createCell(1).setCellValue(bill.getAmount());
            row.createCell(2).setCellValue(bill.getDueDate().toString());
            row.createCell(3).setCellValue(bill.getRecurringInterval());
        }

        // Auto-size columns for readability
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the file to disk
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            System.out.println("Excel file created successfully at " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Bill> getBills() {
        return new ArrayList<>(bills);
    }
}
