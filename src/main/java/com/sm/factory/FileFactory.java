package com.sm.factory;

import com.sm.constant.FileConstant;
import com.sm.orm.model.Employee;
import com.sm.service.CsvService;
import com.sm.service.ExcelService;
import com.sm.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileFactory {

    Map<String, FileService> fileServiceMap = new HashMap<>();

    @Autowired
    CsvService csvService;

    @Autowired
    ExcelService excelService;

    @PostConstruct
    public void init() {
        fileServiceMap.put(FileConstant.CSV, csvService);
        fileServiceMap.put(FileConstant.EXCEL, csvService);
    }

    public ByteArrayInputStream employeeToFile(String extension, List<Employee> employeeList) {
        return fileServiceMap.get(extension).employeeToFile(employeeList);
    }

    public List<Employee> fileToEmployee(String extension, InputStream inputStream) {
        return fileServiceMap.get(extension).fileToEmployee(inputStream);
    }
}
