package com.kazemi.EmployeeActivities.Repository;

import com.kazemi.EmployeeActivities.Models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fh.kazemi
 **/
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e " +
            "WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(CONCAT(e.firstName, e.lastName)) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(CONCAT(e.lastName, ' ', e.firstName)) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(CONCAT(e.lastName, e.firstName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> findByName(@Param("name") String name);
}