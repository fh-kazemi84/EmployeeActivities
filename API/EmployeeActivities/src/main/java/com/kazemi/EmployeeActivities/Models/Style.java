package com.kazemi.EmployeeActivities.Models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * @author fh.kazemi
 **/
@Setter
@Getter
@NoArgsConstructor
public class Style {
    private Integer foregroundColor;
    private String pattern;
    private Gradient gradient;
    private Border border;
}
