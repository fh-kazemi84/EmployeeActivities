package com.kazemi.EmployeeActivities.Repository;

import com.kazemi.EmployeeActivities.Model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * @author fh.kazemi
 **/
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO activity (employee_id, date, content, comment, type)
        VALUES (:employeeId, :date, :content, :comment, :type)
        ON DUPLICATE KEY UPDATE 
            content = VALUES(content),
            comment = VALUES(comment),
            type = VALUES(type)
        """, nativeQuery = true)
    void upsertActivity(@Param("employeeId") Integer employeeId,
                        @Param("date") Date date,
                        @Param("content") String content,
                        @Param("comment") String comment,
                        @Param("type") String type);

    @Query("SELECT a FROM Activity a " +
            "WHERE (" +
            "LOWER(a.employee.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(a.employee.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(CONCAT(a.employee.firstName, ' ', a.employee.lastName)) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(CONCAT(a.employee.firstName, a.employee.lastName)) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(CONCAT(a.employee.lastName, ' ', a.employee.firstName)) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(CONCAT(a.employee.lastName, a.employee.firstName)) LIKE LOWER(CONCAT('%', :name, '%'))" +
            ") AND a.date = :date")
    Optional<Activity> findByEmployeeNameAndDate(@Param("name") String name, @Param("date") Date date);

}

