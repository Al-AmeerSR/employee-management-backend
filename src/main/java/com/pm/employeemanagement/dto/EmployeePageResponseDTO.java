package com.pm.employeemanagement.dto;

import java.util.List;

public class EmployeePageResponseDTO {
    // Getters and Setters
    private List<EmployeeDTO> employees;
    private int totalPages;

    public EmployeePageResponseDTO(List<EmployeeDTO> employees, int totalPages) {
        this.employees = employees;
        this.totalPages = totalPages;
    }
    public EmployeePageResponseDTO(){}

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

