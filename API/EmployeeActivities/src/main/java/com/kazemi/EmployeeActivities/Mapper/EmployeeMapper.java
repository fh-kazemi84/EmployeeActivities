package com.kazemi.EmployeeActivities.Mapper;

import com.kazemi.EmployeeActivities.DTO.EmployeeDTO;
import com.kazemi.EmployeeActivities.Model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author fh.kazemi
 **/

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDTO toDto(Employee employee);
    List<EmployeeDTO> toDtoList(List<Employee> employees);

    Employee toEntity(EmployeeDTO employeeDTO);
    List<Employee> toEntityList(List<EmployeeDTO> employeeDTOList);
}
