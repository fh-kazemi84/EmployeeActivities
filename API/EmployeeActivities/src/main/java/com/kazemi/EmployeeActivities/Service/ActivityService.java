package com.kazemi.EmployeeActivities.Service;

import com.kazemi.EmployeeActivities.Models.*;
import com.kazemi.EmployeeActivities.Models.Enums.ActivityType;
import org.springframework.stereotype.Service;

/**
 * @author fh.kazemi
 **/
@Service
public class ActivityService {

    private final ExcelService excelService;

    public ActivityService(ExcelService excelService) {
        this.excelService = excelService;
    }

    public Activity getActivity(String filePath, String sheetName, String cellAddress) throws Exception {
        excelService.load(filePath, sheetName);
        CellInfo cellInfo = excelService.readCell(cellAddress);
        excelService.close();

        ActivityType activityType = detectActivityType(cellInfo.getStyle());

        Activity activity = new Activity();
        activity.setContent(cellInfo.getContent());
        activity.setComment(cellInfo.getComment());
        activity.setType(activityType);

        return activity;
    }

    public ActivityType detectActivityType(Style style) {
        return ActivityTypeRules.RULES.stream()
                .filter(rule -> matches(style, rule.getStyle()))
                .map(StyleRule::getType)
                .findFirst()
                .orElse(ActivityType.NORMAL);
    }

    private boolean matches(Style style, Style ruleStyle) {
        if (ruleStyle == null || style == null) return false;

        // ForegroundColor
        if (ruleStyle.getForegroundColor() != null &&
                !ruleStyle.getForegroundColor().equals(style.getForegroundColor())) {
            return false;
        }

        // Pattern
        if (ruleStyle.getPattern() != null &&
                !ruleStyle.getPattern().equals(style.getPattern())) {
            return false;
        }

        // Gradient
        if (ruleStyle.getGradient() != null) {
            if (style.getGradient() == null) return false;
            if (ruleStyle.getGradient().getGradientStopColor() != null &&
                    !ruleStyle.getGradient().getGradientStopColor()
                            .equals(style.getGradient().getGradientStopColor())) {
                return false;
            }
            if (ruleStyle.getGradient().getGradientDegree() != style.getGradient().getGradientDegree()) {
                return false;
            }
        }

        // Border
        if (ruleStyle.getBorder() != null) {
            if (style.getBorder() == null) return false;
            if (ruleStyle.getBorder().getBorderColor() != null &&
                    !ruleStyle.getBorder().getBorderColor()
                            .equals(style.getBorder().getBorderColor())) {
                return false;
            }
            if (ruleStyle.getBorder().getBorderStyle() != null &&
                    !ruleStyle.getBorder().getBorderStyle().equals(style.getBorder().getBorderStyle())) {
                return false;
            }
        }

        return true;
    }
}
