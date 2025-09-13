package com.kazemi.EmployeeActivities.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

/**
 * @author fh.kazemi
 **/
@Entity
@Table(name = "employee")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "activityList")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date ASC")
    private List<Activity> activityList = new ArrayList<>();
}
