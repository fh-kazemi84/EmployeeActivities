package com.kazemi.EmployeeActivities.Model;

import com.kazemi.EmployeeActivities.Model.Enums.ActivityType;

import java.util.List;

/**
 * @author fh.kazemi
 **/
public class ActivityTypeRules {
    public static final List<StyleRule> RULES = List.of(
            new StyleRule(new Style(16777215, "NO_FILL", null, null), ActivityType.NORMAL),
            new StyleRule(new Style(43520, "SOLID_FOREGROUND", null, null), ActivityType.LEAVE),
            new StyleRule(new Style(64000, "SOLID_FOREGROUND", null, null), ActivityType.LEAVE_REQUEST),
            new StyleRule(new Style(16755310, "SOLID_FOREGROUND", null, null), ActivityType.ONSITE_CLIENT_MEETING),
            new StyleRule(new Style(16777010, "SOLID_FOREGROUND", null, null), ActivityType.ONLINE_CLIENT_TRAINING),
            new StyleRule(new Style(16384000, "SOLID_FOREGROUND", null, null), ActivityType.BUSINESS_TRIP),
            new StyleRule(new Style(250, "SOLID_FOREGROUND", null, null), ActivityType.UNI),
            new StyleRule(new Style(11205370, "SOLID_FOREGROUND", null, null), ActivityType.SCHOOL_HOLIDAY),
            new StyleRule(new Style(16777215, "THICK_BACKWARD_DIAG", null, null), ActivityType.VACATION),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 43520, 180.0), null), ActivityType.LEAVE_MORNING),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 43520, 0.0), null), ActivityType.LEAVE_AFTERNOON),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 64000, 180.0), null), ActivityType.LEAVE_REQUEST_MORNING),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 64000, 0.0), null), ActivityType.LEAVE_REQUEST_AFTERNOON),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 16755310, 180.0), null), ActivityType.ONSITE_CLIENT_MEETING_MORNING),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 16755310, 0.0), null), ActivityType.ONSITE_CLIENT_MEETING_AFTERNOON),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 16777010, 180.0), null), ActivityType.ONLINE_CLIENT_TRAINING_MORNING),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 16777010, 0.0), null), ActivityType.ONLINE_CLIENT_TRAINING_AFTERNOON),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 16711680, 180.0), null), ActivityType.BUSINESS_TRIP_MORNING),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 16711680, 0.0), null), ActivityType.BUSINESS_TRIP_AFTERNOON),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 250, 180.0), null), ActivityType.UNI_MORNING),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 250, 0.0), null), ActivityType.UNI_AFTERNOON),
            new StyleRule(new Style(16777215, "NO_FILL", null, new Border("THICK", 16750848)), ActivityType.HOME_OFFICE),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 43520, 180.0), new Border("THICK", 16750848)), ActivityType.HOME_OFFICE_LEAVE_MORNING),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 43520, 0.0), new Border("THICK", 16750848)), ActivityType.HOME_OFFICE_LEAVE_AFTERNOON),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 16777010, 180.0), new Border("THICK", 16750848)), ActivityType.HOME_OFFICE_ONLINE_CLIENT_TRAINING_MORNING),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 16777010, 0.0), new Border("THICK", 16750848)), ActivityType.HOME_OFFICE_ONLINE_CLIENT_TRAINING_AFTERNOON),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 250, 180.0), new Border("THICK", 16750848)), ActivityType.HOME_OFFICE_UNI_MORNING),
            new StyleRule(new Style(null, "NO_FILL", new Gradient(null, 250, 0.0), new Border("THICK", 16750848)), ActivityType.HOME_OFFICE_UNI_AFTERNOON)
    );
}
