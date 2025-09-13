package com.kazemi.EmployeeActivities.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author fh.kazemi
 **/
@Setter
@Getter
public class CellInfo {
    private int row;
    private int column;
    private String content;
    private String comment;
    private Style style;
}
