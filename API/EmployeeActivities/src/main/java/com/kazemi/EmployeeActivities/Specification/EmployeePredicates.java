package com.kazemi.EmployeeActivities.Specification;

import com.kazemi.EmployeeActivities.Model.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

/**
 * @author fh.kazemi
 **/
public class EmployeePredicates {
    
    public static Predicate hasNameLike(Path<Employee> employee, CriteriaBuilder cb, String name) {
        String pattern = "%" + name.toLowerCase() + "%";
        return cb.or(
                cb.like(cb.lower(employee.get("firstName")), pattern),
                cb.like(cb.lower(employee.get("lastName")), pattern),
                cb.like(cb.lower(cb.concat(cb.concat(employee.get("firstName"), " "), employee.get("lastName"))), pattern),
                cb.like(cb.lower(cb.concat(employee.get("firstName"), employee.get("lastName"))), pattern),
                cb.like(cb.lower(cb.concat(cb.concat(employee.get("lastName"), " "), employee.get("firstName"))), pattern),
                cb.like(cb.lower(cb.concat(employee.get("lastName"), employee.get("firstName"))), pattern)
        );
    }
}
