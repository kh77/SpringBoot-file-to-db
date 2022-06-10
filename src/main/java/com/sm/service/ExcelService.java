package com.sm.service;


import com.sm.constant.FileConstant;
import com.sm.orm.model.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService implements FileService {

    private static String[] HEADERS = {"ID", "name", "phone", "age", "department", "active"};
    private static String SHEET = "Employee Data";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!FileConstant.EXCEL_TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public ByteArrayInputStream employeeToFile(List<Employee> employeeList) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header first row
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
            }

            int rowId = 1;
            for (Employee employee : employeeList) {
                Row row = sheet.createRow(rowId++);

                row.createCell(0).setCellValue(employee.getId());
                row.createCell(1).setCellValue(employee.getName());
                row.createCell(2).setCellValue(employee.getPhone());
                row.createCell(3).setCellValue(employee.getAge());
                row.createCell(4).setCellValue(employee.getDepartment());
                row.createCell(5).setCellValue(employee.isActive());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("failed to import data to Excel file: " + e.getMessage());
        }
    }

    public List<Employee> fileToEmployee(InputStream is) {
        try (Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Employee> employeeList = new ArrayList<Employee>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Employee employee = new Employee();

                int cellId = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellId) {
                        case 0: {
                            employee = new Employee();
                            employee.setId((long) currentCell.getNumericCellValue());
                            break;
                        }
                        case 1: {
                            employee.setName(currentCell.getStringCellValue());
                            break;
                        }
                        case 2: {
                            employee.setPhone(String.valueOf((int) currentCell.getNumericCellValue()));
                            break;
                        }
                        case 3: {
                            employee.setAge((int) currentCell.getNumericCellValue());
                            break;
                        }
                        case 4: {
                            employee.setDepartment(currentCell.getStringCellValue());
                            break;
                        }
                        case 5: {
                            boolean flag = false;
                            switch (currentCell.getCellType().name()) {
                                case "BOOLEAN": {
                                    flag = currentCell.getBooleanCellValue();
                                    break;
                                }
                                case "STRING": {
                                    if (currentCell.getStringCellValue().contains("1")) {
                                        flag = true;
                                    }
                                    break;
                                }
                                case "NUMERIC": {
                                    if (currentCell.getNumericCellValue() == 1) {
                                        flag = true;
                                    }
                                    break;
                                }


                            }
                            employee.setActive(flag);
                            break;
                        }
                        default:
                            break;
                    }
                    cellId++;
                }

                employeeList.add(employee);

            }

            return employeeList;
        } catch (IOException e) {
            throw new RuntimeException("failed to parse Excel file: " + e.getMessage());
        }
    }
}