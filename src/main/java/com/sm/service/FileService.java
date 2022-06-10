package com.sm.service;

import com.sm.orm.model.Employee;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    ByteArrayInputStream employeeToFile(List<Employee> employeeList);
    List<Employee> fileToEmployee(InputStream inputStream);
}
