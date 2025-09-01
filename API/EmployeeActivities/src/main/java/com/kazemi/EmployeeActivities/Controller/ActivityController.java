package com.kazemi.EmployeeActivities.Controller;

import com.kazemi.EmployeeActivities.Models.Activity;
import com.kazemi.EmployeeActivities.Service.ActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fh.kazemi
 **/
@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private  final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/get-activity")
    public Activity readCell(@RequestParam String filePath,
                             @RequestParam String sheet,
                             @RequestParam String cell) throws Exception {
        return activityService.getActivity(filePath, sheet, cell);
    }
}
