package com.example.rd.autocode.assessment.appliances.auxiliary;

import com.example.rd.autocode.assessment.appliances.user.employee.signUp.Employee;

public class EmployeeBuilder {
    Long id = 0L;
    String name = "";
    String email = "";
    String password = "";
    String department = "";

    public EmployeeBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public EmployeeBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EmployeeBuilder email(String email) {
        this.email = email;
        return this;
    }
    public EmployeeBuilder password(String password) {
        this.password = password;
        return this;
    }
    public EmployeeBuilder department(String department) {
        this.department = department;
        return this;
    }
    public Employee build() {
        return new Employee(id, name, email, password, department, false);
    }
}
