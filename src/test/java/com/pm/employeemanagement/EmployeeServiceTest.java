package com.pm.employeemanagement;

import com.pm.employeemanagement.dto.EmployeeDTO;
import com.pm.employeemanagement.dto.EmployeePageResponseDTO;
import com.pm.employeemanagement.entity.Employee;
import com.pm.employeemanagement.exception.EmployeeNotFoundException;
import com.pm.employeemanagement.repository.EmployeeRepository;
import com.pm.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    private Employee existingEmployee;

    private EmployeeDTO updateDTO;

    private EmployeeDTO inputDTO;

    private Employee employee1;

    private Employee employee2;

    @BeforeEach
   void setUp() {

        employee = new Employee(1,"Ameer","iamameer37@gmail.com","IT");

        existingEmployee = new Employee(1, "Ameer", "oldmail@example.com", "Finance");

        updateDTO = new EmployeeDTO(1, "Ameer Updated", "ameer.updated@example.com", "IT");

        inputDTO = new EmployeeDTO(0, "Ameer", "ameer@example.com", "IT");

        employee = new Employee(1, "Ameer", "ameer@example.com", "IT");

        employee1 = new Employee(1, "Ameer", "ameer@example.com", "IT");

        employee2 = new Employee(2, "Ali", "ali@example.com", "Finance");
    }


    @Test
    void test_getEmployeeById (){

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        EmployeeDTO employeeDTO = employeeService.getEmployeeById(1);

        assertNotNull(employeeService.getEmployeeById(1));
        assertEquals(employee.getId(),employeeDTO.getId());
        assertEquals(employee.getName(),employeeDTO.getName());
        assertEquals(employee.getEmail(),employeeDTO.getEmail());
        assertEquals(employee.getDepartment(),employeeDTO.getDepartment());

    }

    @Test
    @DisplayName("getEmployeeById")
    void test_getEmployeeById_whenUserNotFound (){
        when(employeeRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(2));

    }

    @Test
    void test_updateEmployee_success() {
        // Mock repo behavior
        when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call service
        EmployeeDTO result = employeeService.updateEmployee(1, updateDTO);

        // Verify changes
        assertNotNull(result);
        assertEquals("Ameer Updated", result.getName());
        assertEquals("ameer.updated@example.com", result.getEmail());
        assertEquals("IT", result.getDepartment());

        // Verify save was called once
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    @Test
    void test_updateEmployee_notFound() {
        // Mock no employee found
        when(employeeRepository.findById(99)).thenReturn(Optional.empty());

        // Assert exception
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(99, updateDTO));

        // Verify save was never called
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void test_deleteEmployee_success() {
        int id = 1;

        // Call service
        employeeService.deleteEmployee(id);

        // Verify repo interaction
        verify(employeeRepository, times(1)).deleteById(id);
    }

    @Test
    void test_deleteEmployee_repositoryThrowsException() {
        int id = 99;

        doThrow(new RuntimeException("DB error"))
                .when(employeeRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployee(id));

        verify(employeeRepository, times(1)).deleteById(id);
    }



    @Test
    void test_createEmployee_happyPath() {
        // Mock static mapper if not static, otherwise call real
        // Here assuming static, so no mocking

        // Mock repository save
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Call service
        EmployeeDTO result = employeeService.createEmployee(inputDTO);

        // Assertions
        assertNotNull(result);
        assertEquals("Ameer", result.getName());
        assertEquals("ameer@example.com", result.getEmail());
        assertEquals("IT", result.getDepartment());

        // Verify repository interaction
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void test_createEmployee_edgeCase_nullInput() {
        // Edge case: inputDTO is null
        assertThrows(NullPointerException.class, () -> employeeService.createEmployee(null));

        // Verify repository save never called
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void test_createEmployee_repositoryThrowsException() {
        when(employeeRepository.save(any(Employee.class))).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> employeeService.createEmployee(inputDTO));

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void test_getAllEmployees_noFilter_happyPath() {
        List<Employee> employees = Arrays.asList(employee1, employee2);
        Page<Employee> page = new PageImpl<>(employees);

        // Fix: Use any(Pageable.class) instead of hardcoding PageRequest.of(0, 10)
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        EmployeePageResponseDTO result = employeeService.getAllEmployees(null, null, 1, 10);

        assertNotNull(result);
        assertEquals(2, result.getEmployees().size());
        assertEquals(1, result.getTotalPages());

        verify(employeeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void test_getAllEmployees_withFilter_happyPath() {
        String email = "ameer@example.com";
        String department = "IT";
        List<Employee> filteredList = Collections.singletonList(employee1);

        when(employeeRepository.getEmployeeByEmailAndDepartment(email, department, 1, 10))
                .thenReturn(filteredList);
        when(employeeRepository.countByEmailAndDepartment(email, department))
                .thenReturn(1L);

        EmployeePageResponseDTO result = employeeService.getAllEmployees(email, department, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getEmployees().size());
        assertEquals("Ameer", result.getEmployees().get(0).getName());
        assertEquals(1, result.getTotalPages());

        verify(employeeRepository, times(1))
                .getEmployeeByEmailAndDepartment(email, department, 1, 10);
        verify(employeeRepository, times(1))
                .countByEmailAndDepartment(email, department);
    }

    @Test
    void test_getAllEmployees_noResults_edgeCase() {
        when(employeeRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        EmployeePageResponseDTO result = employeeService.getAllEmployees(null, null, 1, 10);

        assertNotNull(result);
        assertTrue(result.getEmployees().isEmpty());
        assertEquals(0, result.getTotalPages());
    }

    @Test
    void test_getAllEmployees_invalidPageNumber_edgeCase() {
        // If pageNumber < 1, service increments pageNumber, still should handle gracefully
        when(employeeRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(Collections.singletonList(employee1)));

        EmployeePageResponseDTO result = employeeService.getAllEmployees(null, null, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getEmployees().size());
        assertEquals("Ameer", result.getEmployees().get(0).getName());
    }


}
