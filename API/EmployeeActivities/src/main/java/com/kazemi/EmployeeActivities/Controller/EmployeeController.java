package com.kazemi.EmployeeActivities.Controller;

import com.kazemi.EmployeeActivities.DTO.EmployeeDTO;
import com.kazemi.EmployeeActivities.Model.Employee;
import com.kazemi.EmployeeActivities.Service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fh.kazemi
 **/
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDTO> getAll() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(employeeService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<EmployeeDTO> searchByName(@RequestParam String name) {
        return employeeService.getByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDto) {
        return employeeService.save(employeeDto);
    }

    @PutMapping("/{id}")
    public EmployeeDTO updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDto) {
        return employeeService.update(id, employeeDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.delete(id);
    }
}
