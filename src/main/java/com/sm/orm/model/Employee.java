package com.sm.orm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private Integer age;

    @Column
    private String department;

    @Column
    private boolean active;

    public Employee() {

    }

    public Employee(Long id, String name, String phone, Integer age, String department, boolean active) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.department = department;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
