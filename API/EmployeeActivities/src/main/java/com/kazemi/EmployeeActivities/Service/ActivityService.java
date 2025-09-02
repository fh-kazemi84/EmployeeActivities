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
        if (ruleStyle == null || style == null){
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
        if (ruleStyle.getGradient() == null){
            return true;
        }
        if (style.getGradient() == null){
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
}
