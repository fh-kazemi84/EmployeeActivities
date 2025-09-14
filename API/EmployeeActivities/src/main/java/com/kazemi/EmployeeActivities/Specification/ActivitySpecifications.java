package com.kazemi.EmployeeActivities.Specification;

import com.kazemi.EmployeeActivities.Model.Activity;
import com.kazemi.EmployeeActivities.Model.Employee;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author fh.kazemi
 **/
public class ActivitySpecifications {

    public static Specification<Activity> hasEmployeeNameLike(String name) {
        return (Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Join<Activity, Employee> employee = root.join("employee");
            return EmployeePredicates.hasNameLike(employee, cb, name);
        };
    }

    public static Specification<Activity> hasEmployeeNameAndDate(String name, java.util.Date date) {
        return (Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Join<Activity, Employee> employee = root.join("employee");
            Predicate namePredicate = EmployeePredicates.hasNameLike(employee, cb, name);
            Predicate datePredicate = cb.equal(root.get("date"), date);
            return cb.and(namePredicate, datePredicate);
        };
    }

    public static Specification<Activity> hasEmployeeNameAndMonth(String name, int month) {
        return (Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Join<Activity, Employee> employee = root.join("employee");
            Predicate namePredicate = EmployeePredicates.hasNameLike(employee, cb, name);
            Predicate monthPredicate = cb.equal(cb.function("MONTH", Integer.class, root.get("date")), month);
            return cb.and(namePredicate, monthPredicate);
        };
    }
}
