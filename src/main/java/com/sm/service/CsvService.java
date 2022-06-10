package com.sm.service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sm.orm.model.Employee;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvService implements FileService {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equalsIgnoreCase(file.getContentType())) {
            return false;
        }
        return true;
    }

    public List<Employee> fileToEmployee(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Employee> employeeList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                boolean flag = false;
                if(csvRecord.get("active").contains("1") ) {
                    flag = true;
                } else {
                    flag = Boolean.valueOf(csvRecord.get("active"));
                }
                Employee employee = new Employee(
                        Long.valueOf(csvRecord.get("ID").trim()),
                        csvRecord.get("name"),
                        csvRecord.get("phone"),
                        Integer.valueOf(csvRecord.get("age").trim()),
                        csvRecord.get("department"),
                        flag
                );

                employeeList.add(employee);
            }

            return employeeList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public ByteArrayInputStream employeeToFile(List<Employee> employeeList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {

            for (Employee employee : employeeList) {
                List<String> data = Arrays.asList(
                        String.valueOf(employee.getId()),
                        employee.getName(),
                        employee.getPhone(),
                        String.valueOf(employee.getAge()),
                        employee.getDepartment(),
                        String.valueOf(employee.isActive())
                );
                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("failed to import data to CSV file: " + e.getMessage());
        }
    }

}
