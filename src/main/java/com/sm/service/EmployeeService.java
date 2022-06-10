package com.sm.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.sm.orm.model.Employee;
import com.sm.factory.FileFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sm.orm.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private FileFactory fileFactory;

    public void save(MultipartFile file, String type) throws IOException {
        List<Employee> employeeList = fileFactory.fileToEmployee(type.toLowerCase(),file.getInputStream());

        if (employeeList != null)
            repository.saveAll(employeeList);
    }

    public ByteArrayInputStream load(String type) {
        List<Employee> tutorials = repository.findAll();
        return fileFactory.employeeToFile(type.toLowerCase(), tutorials);
    }

    public List<Employee> getAllEmployee() {
        return repository.findAll();
    }
}
