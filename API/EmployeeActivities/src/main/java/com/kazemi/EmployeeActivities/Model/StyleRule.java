package com.kazemi.EmployeeActivities.Model;

import com.kazemi.EmployeeActivities.Model.Enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author fh.kazemi
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StyleRule {
    private Style style;
    private ActivityType type;
}
