package com.kazemi.EmployeeActivities.Models;

import com.kazemi.EmployeeActivities.Models.Enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * @author fh.kazemi
 **/
@Entity
@Table(
        name = "activity",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id", "date"})},
        indexes = {
                @Index(name = "idx_activity_employee_date", columnList = "employee_id, date"),
                @Index(name = "idx_activity_date", columnList = "date")
        }
)
@Setter
@Getter
@AllArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "content", nullable = true)
    private String content;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityType type;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    public Activity()
    {
        type = ActivityType.NORMAL;
    }
}

