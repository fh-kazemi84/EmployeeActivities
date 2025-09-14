package com.kazemi.EmployeeActivities.Service;

import com.kazemi.EmployeeActivities.DTO.ActivityDTO;
import com.kazemi.EmployeeActivities.Mapper.ActivityMapper;
import com.kazemi.EmployeeActivities.Model.*;
import com.kazemi.EmployeeActivities.Model.Enums.ActivityType;
import com.kazemi.EmployeeActivities.Repository.ActivityRepository;
import com.kazemi.EmployeeActivities.Repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author fh.kazemi
 **/
@Service
public class ActivityService {

    private final ExcelService excelService;
    private final ActivityMapper mapper;
    private final EmployeeRepository employeeRepository;
    private final ActivityRepository activityRepository;

    public ActivityService(ExcelService excelService,
                           ActivityMapper activityMapper,
                           EmployeeRepository employeeRepository,
                           ActivityRepository activityRepository) {
        this.excelService = excelService;
        this.mapper = activityMapper;
        this.employeeRepository = employeeRepository;
        this.activityRepository = activityRepository;
    }

    public ActivityDTO getCellActivity(String filePath, String sheetName, String cellAddress) throws Exception {
        excelService.load(filePath, sheetName);
        CellInfo cellInfo = excelService.readCell(cellAddress);
        excelService.close();

        ActivityType activityType = detectActivityType(cellInfo.getStyle());

        Activity activity = new Activity();
        activity.setContent(cellInfo.getContent());
        activity.setComment(cellInfo.getComment());
        activity.setType(activityType);

        return mapper.toDto(activity);
    }

    public ActivityType detectActivityType(Style style) {
        return ActivityTypeRules.RULES.stream()
                .filter(rule -> matches(style, rule.getStyle()))
                .map(StyleRule::getType)
                .findFirst()
                .orElse(ActivityType.NORMAL);
    }

    private boolean matches(Style style, Style ruleStyle) {
        if (ruleStyle == null || style == null) {
            return false;
        }

        return matchesForegroundColor(style, ruleStyle)
                && matchesPattern(style, ruleStyle)
                && matchesGradient(style, ruleStyle)
                && matchesBorder(style, ruleStyle);
    }

    private boolean matchesForegroundColor(Style style, Style ruleStyle) {
        return ruleStyle.getForegroundColor() == null ||
                ruleStyle.getForegroundColor().equals(style.getForegroundColor());
    }

    private boolean matchesPattern(Style style, Style ruleStyle) {
        return ruleStyle.getPattern() == null ||
                ruleStyle.getPattern().equals(style.getPattern());
    }

    private boolean matchesGradient(Style style, Style ruleStyle) {
        if (ruleStyle.getGradient() == null) {
            return true;
        }
        if (style.getGradient() == null) {
            return false;
        }

        Gradient ruleGradient = ruleStyle.getGradient();
        Gradient actualGradient = style.getGradient();

        if (ruleGradient.getGradientStopColor() != null &&
                !ruleGradient.getGradientStopColor().equals(actualGradient.getGradientStopColor())) {
            return false;
        }

        return ruleGradient.getGradientDegree() == actualGradient.getGradientDegree();
    }

    private boolean matchesBorder(Style style, Style ruleStyle) {
        if (ruleStyle.getBorder() == null) {
            return true;
        }
        if (style.getBorder() == null) {
            return false;
        }

        Border ruleBorder = ruleStyle.getBorder();
        Border actualBorder = style.getBorder();

        if (ruleBorder.getBorderColor() != null &&
                !ruleBorder.getBorderColor().equals(actualBorder.getBorderColor())) {
            return false;
        }

        if (ruleBorder.getBorderStyle() != null &&
                !ruleBorder.getBorderStyle().equals(actualBorder.getBorderStyle())) {
            return false;
        }

        return true;
    }

    public void importFromExcel(String filePath, String sheetName) throws Exception {
        try {
            excelService.load(filePath, sheetName);
            boolean continueProcess = true;
            int year = Integer.parseInt(sheetName);
            int month = 0;
            int numbersOfMonth = 0;

            int rowIndex = 6;
            Row row;
            while ((row = excelService.getRow(rowIndex)) != null) {
                String firstCell = excelService.getCellContent(row.getCell(0));

                if (firstCell != null && !firstCell.isBlank()) {
                    if (isMonthRow(firstCell)) {
                        month = parseMonth(firstCell);
                        numbersOfMonth = getNumbersOfMonth(row);
                    } else {
                        continueProcess = processMonthBlock(rowIndex, year, month, numbersOfMonth);
                    }
                } else if (month == 12) {
                    continueProcess = false;
                }
                if (continueProcess) {
                    rowIndex++;
                } else {
                    break;
                }
            }
        } finally {
            excelService.close();
        }
    }

    private boolean isMonthRow(String cellValue) {
        return cellValue != null && !cellValue.isBlank() && isMonthName(cellValue);
    }

    private boolean isMonthName(String value) {
        if (value == null) return false;
        try {
            Month.valueOf(value.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean processMonthBlock(int rowIndex, int year, int month, int numbersOfMonth) {
        Row row = excelService.getRow(rowIndex);
        if (row != null) {
            String employeeName = excelService.getCellContent(row.getCell(0));// repeat this line 134
            if (employeeName != null && !employeeName.isBlank()) {// repeat this line 184
                Employee employee = getOrCreateEmployee(employeeName);
                processEmployeeActivity(rowIndex, employee, year, month, numbersOfMonth);
            }
        }
        return true;
    }

    private void processEmployeeActivity(int rowIndex, Employee employee, int year, int month, int numbersOfMonth) {
        Row row = excelService.getRow(rowIndex);// repeat this line
        if (row != null) {
            for (int col = 1; col <= numbersOfMonth; col++) {
                Cell cell = row.getCell(col);
                if (cell == null) {
                    continue;
                }

                CellInfo cellInfo = excelService.readCell(new CellReference(rowIndex, col).formatAsString());
                ActivityType type = detectActivityType(cellInfo.getStyle());

                // Skip saving NORMAL activities to reduce redundant data
                if(type != ActivityType.NORMAL){
                    LocalDate local = LocalDate.of(year, month, col);
                    Date date = java.sql.Date.valueOf(local);

                    Activity activity = new Activity();
                    activity.setEmployee(employee);
                    activity.setDate(date);
                    activity.setContent(cellInfo.getContent());
                    activity.setComment(cellInfo.getComment());
                    activity.setType(type);

                    //activityRepository.save(activity);
                    activityRepository.upsertActivity(
                            employee.getId(),
                            date,
                            cellInfo.getContent(),
                            cellInfo.getComment(),
                            type.name()
                    );
                }

            }
        }
    }

    private Employee getOrCreateEmployee(String fullName) {
        String[] parts = Arrays.stream(fullName.trim().split("\\s+"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? String.join(" ", Arrays.copyOfRange(parts, 1, parts.length)) : "";

        Employee employee;
        List<Employee> found = employeeRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
        if (!found.isEmpty()) {
            employee = found.get(0);
        } else {
            employee = employeeRepository.save(new Employee(null, firstName, lastName, new ArrayList<>()));
        }
        return employee;
    }

    private int parseMonth(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }

    private int getNumbersOfMonth(Row row) {
        int numbersOfMonth = 0;
        for (int col = 1; col < row.getLastCellNum(); col++) {
            String value = excelService.getCellContent(row.getCell(col));
            if (value == null || value.isBlank()) {
                break;
            }
            numbersOfMonth++;
        }
        return numbersOfMonth;
    }

    public ActivityDTO getActivityByEmployeeNameAndDate(String name, Date date) {
        Activity activity = activityRepository.findByEmployeeNameAndDate(name, date)
                .orElseThrow(() -> new RuntimeException("Activity not found for " + name + " on " + date));
        return mapper.toDto(activity);
    }

    public List<ActivityDTO> getActivitiesByEmployeeNameAndMonth(String name, int month) {
        List<Activity> activities = activityRepository.findByEmployeeNameAndMonth(name, month);
        return mapper.toDtoList(activities);
    }
}
