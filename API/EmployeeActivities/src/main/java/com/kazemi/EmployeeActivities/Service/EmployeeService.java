package com.kazemi.EmployeeActivities.Service;

import com.kazemi.EmployeeActivities.DTO.EmployeeDTO;
import com.kazemi.EmployeeActivities.Mapper.EmployeeMapper;
import com.kazemi.EmployeeActivities.Model.Employee;
import com.kazemi.EmployeeActivities.Repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fh.kazemi
 **/
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.mapper = employeeMapper;
    }

    public List<EmployeeDTO> getAll() {
        List<Employee> employeeList = employeeRepository.findAll();
        return mapper.toDtoList(employeeList);
    }

    public EmployeeDTO getById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return mapper.toDto(employee);
    }

    public List<EmployeeDTO> getByName(String name){
        List<Employee> employeeList = employeeRepository.findByName(name);
        return mapper.toDtoList(employeeList);
    }

    public EmployeeDTO save(EmployeeDTO employeeDto) {
        Employee employee = mapper.toEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapper.toDto(savedEmployee);
    }

    public EmployeeDTO update(Integer id, EmployeeDTO employeeDTO) {
        Employee employee = mapper.toEntity(employeeDTO);
        Employee updatedEmployee = employeeRepository.findById(id)
                .map(e -> {
                    e.setFirstName(employee.getFirstName());
                    e.setLastName(employee.getLastName());
                    e.setActivityList(employee.getActivityList());
                    return employeeRepository.save(e);
                }).orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return mapper.toDto(updatedEmployee);
    }

    public void delete(Integer id) {
        employeeRepository.findById(id)
                .ifPresentOrElse(
                        employeeRepository::delete,
                        () -> { throw new RuntimeException("Employee not found with id: " + id); }
                );
    }
}
