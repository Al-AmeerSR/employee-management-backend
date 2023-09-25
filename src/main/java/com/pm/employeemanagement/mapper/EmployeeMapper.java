package com.pm.employeemanagement.mapper;

import com.pm.employeemanagement.dto.EmployeeDTO;
import com.pm.employeemanagement.entity.Employee;



public class EmployeeMapper {

    private EmployeeMapper() {

    }
    public static Employee mapDtoToEmployee(EmployeeDTO employeeDTO) {

        return new Employee(employeeDTO.getId(),
                            employeeDTO.getName(),
                            employeeDTO.getEmail(),
                            employeeDTO.getDepartment());

    }

    public static EmployeeDTO mapEmployeeToDTO(Employee employee) {

        return new EmployeeDTO(employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment());

    }
}
