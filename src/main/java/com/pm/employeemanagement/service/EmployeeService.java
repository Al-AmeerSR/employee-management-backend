package com.pm.employeemanagement.service;

import com.pm.employeemanagement.dto.EmployeeDTO;
import com.pm.employeemanagement.dto.EmployeePageResponseDTO;
import com.pm.employeemanagement.entity.Employee;
import com.pm.employeemanagement.exception.EmployeeNotFoundException;
import com.pm.employeemanagement.mapper.EmployeeMapper;
import com.pm.employeemanagement.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepo;

    public EmployeeService(EmployeeRepository employeeRepo){

        this.employeeRepo = employeeRepo;
    }

    public EmployeePageResponseDTO getAllEmployees(String email, String department, int pageNumber, int pageSize){

        List<Employee> employeeList;
        int totalPages;

        boolean noFilter = (email == null || email.isEmpty()) &&
                           (department == null || department.isEmpty());


        if (noFilter) {

            Pageable pageable = PageRequest.of((++pageNumber - (1)), pageSize);
            Page<Employee> page = employeeRepo.findAll(pageable);
            employeeList = page.getContent();
            totalPages = page.getTotalPages();

        } else {
            // In your custom method, ensure pagination is applied properly
            employeeList = employeeRepo.getEmployeeByEmailAndDepartment(email, department, pageNumber, pageSize);
            long totalRecords = employeeRepo.countByEmailAndDepartment(email, department); // Create this method
            totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        }


        List<EmployeeDTO> employeeDTOList = employeeList.stream()
                .map(EmployeeMapper::mapEmployeeToDTO)
                .toList();

        return new EmployeePageResponseDTO(employeeDTOList, totalPages);

    }

    public EmployeeDTO getEmployeeById(int id) {

        return employeeRepo.findById(id)
                .map(EmployeeMapper::mapEmployeeToDTO)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found"));

    }


    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO){

        Employee employee = EmployeeMapper.mapDtoToEmployee(employeeDTO);
        employeeRepo.save(employee);
        return EmployeeMapper.mapEmployeeToDTO(employee);
    }

    public EmployeeDTO updateEmployee(int id, EmployeeDTO employeeDTO) {
        Employee updatedEmployee = employeeRepo.findById(id)
                .map(emp -> {
                    emp.setName(employeeDTO.getName());
                    emp.setEmail(employeeDTO.getEmail());
                    emp.setDepartment(employeeDTO.getDepartment());
                    return employeeRepo.save(emp); // Save and return updated entity
                })
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        return EmployeeMapper.mapEmployeeToDTO(updatedEmployee);
    }


    public void deleteEmployee(int id){

        employeeRepo.deleteById(id);

    }

}
