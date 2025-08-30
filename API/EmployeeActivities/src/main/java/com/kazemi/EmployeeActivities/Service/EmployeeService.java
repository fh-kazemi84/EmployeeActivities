package com.kazemi.EmployeeActivities.Service;

import com.kazemi.EmployeeActivities.Models.Employee;
import com.kazemi.EmployeeActivities.Repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author fh.kazemi
 **/
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getById(Integer id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> getByName(String name){
        return employeeRepository.findByName(name);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Integer id) {
        employeeRepository.findById(id)
                .ifPresentOrElse(
                        employeeRepository::delete,
                        () -> { throw new RuntimeException("Employee not found with id: " + id); }
                );
    }

    public Employee update(Integer id, Employee updatedEmployee) {
        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setFirstName(updatedEmployee.getFirstName());
                    employee.setLastName(updatedEmployee.getLastName());
                    return employeeRepository.save(employee);
                }).orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }
}
