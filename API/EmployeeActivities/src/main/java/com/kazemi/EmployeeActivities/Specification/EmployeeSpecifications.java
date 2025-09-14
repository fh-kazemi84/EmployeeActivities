package com.kazemi.EmployeeActivities.Specification;

import com.kazemi.EmployeeActivities.Model.Employee;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;

/**
 * @author fh.kazemi
 **/
public class EmployeeSpecifications {

    public static Specification<Employee> hasNameLike(String name) {
        return (Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                EmployeePredicates.hasNameLike(root, cb, name);
    }
}

