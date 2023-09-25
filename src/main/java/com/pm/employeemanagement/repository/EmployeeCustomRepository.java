package com.pm.employeemanagement.repository;

import com.pm.employeemanagement.entity.Employee;

import java.util.List;

public interface EmployeeCustomRepository {
    List<Employee> getEmployeeByEmailAndDepartment(String email, String department,int pagNumber,int pageSize);

    long countByEmailAndDepartment(String email, String department);
}
