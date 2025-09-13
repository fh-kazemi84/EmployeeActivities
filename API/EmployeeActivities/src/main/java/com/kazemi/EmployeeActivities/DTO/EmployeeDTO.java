package com.kazemi.EmployeeActivities.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fh.kazemi
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private List<ActivityDTO> activityList = new ArrayList<>();
}
