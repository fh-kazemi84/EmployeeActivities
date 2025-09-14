package com.kazemi.EmployeeActivities.Repository;

import com.kazemi.EmployeeActivities.Model.Employee;
import com.kazemi.EmployeeActivities.Specification.EmployeeSpecifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fh.kazemi
 **/
@Repository
public interface EmployeeRepository extends
        JpaRepository<Employee, Integer>,
        JpaSpecificationExecutor<Employee> {

    default List<Employee> findByName(String name) {
        return findAll(EmployeeSpecifications.hasNameLike(name));
    }

    List<Employee> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}