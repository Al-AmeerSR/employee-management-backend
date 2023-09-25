package com.pm.employeemanagement.controller;

import com.pm.employeemanagement.dto.EmployeeDTO;
import com.pm.employeemanagement.dto.EmployeePageResponseDTO;
import com.pm.employeemanagement.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public ResponseEntity<EmployeePageResponseDTO> getEmployees(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String department,
            @RequestParam(required = false , defaultValue = "1") int pageNumber ,
            @RequestParam(required = false , defaultValue = "4") int pageSize
    ) {

        return new ResponseEntity<>(
                employeeService.getAllEmployees(email, department,pageNumber,pageSize),
                HttpStatus.OK
                 );
    }
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable int id) {

        return new ResponseEntity<>(employeeService.getEmployeeById(id),HttpStatus.OK);

    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO){

        return new ResponseEntity<>(
                employeeService.createEmployee(employeeDTO),
                HttpStatus.CREATED
                );
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable int id, @RequestBody EmployeeDTO employeeDTO){

        return new ResponseEntity<>(
                employeeService.updateEmployee(id,employeeDTO),
                HttpStatus.OK
                );
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") int id){

        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
