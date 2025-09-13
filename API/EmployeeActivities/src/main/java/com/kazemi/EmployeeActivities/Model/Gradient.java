package com.kazemi.EmployeeActivities.Model;

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
public class Gradient {
    private Integer gradientStartColor;
    private Integer gradientStopColor;
    private double gradientDegree;
}
