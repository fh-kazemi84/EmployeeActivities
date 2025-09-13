package com.kazemi.EmployeeActivities.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author fh.kazemi
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Style {
    private Integer foregroundColor;
    private String pattern;
    private Gradient gradient;
    private Border border;
}
