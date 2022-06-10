package com.sm.controller;

import java.util.Date;
import java.util.List;

import com.sm.constant.FileConstant;
import com.sm.orm.model.Employee;
import com.sm.response.ResponseMessage;
import com.sm.service.EmployeeService;
import com.sm.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sm.service.CsvService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFileToDB(@RequestPart("type") String type,
                                                          @RequestPart("file") MultipartFile file) {
        String message = "";
        try {
            message = file.getOriginalFilename() + " Uploaded";

            if (type.equalsIgnoreCase("excel")) {
                if (ExcelService.hasExcelFormat(file)) {
                    employeeService.save(file, type);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
                }
            } else if (!type.equalsIgnoreCase(FileConstant.CSV)) {
                throw new RuntimeException("Extension is not valid");
            }

            if (CsvService.hasCSVFormat(file)) {
                employeeService.save(file, type);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
        message = "Please upload csv/excel file";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployee() {
        List<Employee> employeeList = employeeService.getAllEmployee();
        return new ResponseEntity<>(employeeList, HttpStatus.OK);
    }

    @GetMapping("/download/{type}")
    public ResponseEntity<Resource> getFile(@PathVariable(value = "type") String type) {
        String extension = type;
        String mediaType = FileConstant.CSV_MEDIA_TYPE;
        if (type.equalsIgnoreCase("excel")) {
            extension = FileConstant.EXCEL;
            mediaType = FileConstant.XML_MEDIA_TYPE;
        } else if (!type.equalsIgnoreCase(FileConstant.CSV)) {
            throw new RuntimeException("Extension is not valid");
        }

        String filename = new Date() + " employee." + extension;
        InputStreamResource file = new InputStreamResource(employeeService.load(type));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(mediaType))
                .body(file);
    }

}
