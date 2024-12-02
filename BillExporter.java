package budgeting;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.file.*;

public class BillExporter {

    public static void exportBills(List<Bill> bills) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bills");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Type");
        headerRow.createCell(0).setCellValue("Bill Name");
        headerRow.createCell(1).setCellValue("Amount");
        headerRow.createCell(2).setCellValue("Due Date");

        Map<String, List<Bill>> billsByType = bills.stream()
                .collect(Collectors.groupingBy(Bill::getType));

        int rowNum = 1;
        for (Map.Entry<String, List<Bill>> entry : billsByType.entrySet()) {
            String type = entry.getKey();
            List<Bill> billsOfType = entry.getValue();

            // Type header row
            Row typeRow = sheet.createRow(rowNum++);
            typeRow.createCell(0).setCellValue(type);

            for (Bill bill : billsOfType) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(bill.getType());
                row.createCell(1).setCellValue(bill.getName());
                row.createCell(2).setCellValue(bill.getAmount());
                row.createCell(3).setCellValue(bill.getDueDate().toString());
            }
            rowNum++; // Blank row between types for readability
        }


        try (FileOutputStream fileOut = new FileOutputStream("bills_"+ LocalDateTime.now() + ".xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
