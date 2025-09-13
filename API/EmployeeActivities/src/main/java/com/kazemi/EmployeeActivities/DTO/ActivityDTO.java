package com.kazemi.EmployeeActivities.DTO;

import com.kazemi.EmployeeActivities.Model.Enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author fh.kazemi
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDTO {
    private Integer id;
    private Date date;
    private String content;
    private String comment;
    private ActivityType type;
}
